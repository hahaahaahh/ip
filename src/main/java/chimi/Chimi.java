package chimi;

import java.util.ArrayList;

import chimi.commands.Command;       // 'c' comes first
import chimi.parser.Parser;          // 'p' comes next
import chimi.storage.Storage;        // 's' comes next
import chimi.tasks.Deadline;         // 't', then 'd'
import chimi.tasks.Event;            // 't', then 'e'
import chimi.tasks.Task;             // 't', then 'Task'
import chimi.tasks.TaskList;         // 't', then 'TaskL'
import chimi.tasks.Todo;             // 't', then 'To'
import chimi.ui.Ui;                  // 'u' comes last

/**
 * The main entry point for the Chimi application.
 * Chimi is a Personal Assistant Chatbot that helps manage tasks.
 */
public class Chimi {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a new Chimi application instance.
     * Initializes the UI, Storage, and attempts to load existing tasks.
     *
     * @param filePath The file path where tasks are stored.
     */
    public Chimi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (ChimiException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
     * Handles user input, parses commands, executes them, and updates storage until the user exits.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // Show the divider line _________
                Command command = Parser.parseCommand(fullCommand);

                switch (command) {
                    case BYE:
                        isExit = true;
                        ui.showMessage("Bye. Hope to see you again soon!");
                        break;

                    case LIST:
                        if (tasks.size() == 0) {
                            ui.showMessage("No tasks added yet!");
                        } else {
                            ui.showMessage("Here are the tasks in your list:");
                            for (int i = 0; i < tasks.size(); i++) {
                                ui.showMessage((i + 1) + "." + tasks.get(i));
                            }
                        }
                        break;

                    case MARK:
                        handleMark(fullCommand, true);
                        break;

                    case UNMARK:
                        handleMark(fullCommand, false);
                        break;

                    case DELETE:
                        String[] deleteCommandParts = fullCommand.split(" ");
                        if (deleteCommandParts.length < 2) {
                            throw new ChimiException("Please specify which task to delete.");
                        }
                        try {
                            int deleteIndex = Integer.parseInt(deleteCommandParts[1]) - 1;
                            Task deleted = tasks.delete(deleteIndex);
                            ui.showMessage("Noted. I've removed this task:");
                            ui.showMessage("  " + deleted);
                            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
                            storage.save(tasks.getAllTasks());
                        } catch (NumberFormatException e) {
                            throw new ChimiException("Please enter a valid number.");
                        }
                        break;

                    case TODO:
                    case DEADLINE:
                    case EVENT:
                        handleAdd(fullCommand, command);
                        break;

                    case FIND:
                        String[] findCommandParts = fullCommand.split(" ", 2);
                        if (findCommandParts.length < 2) {
                            throw new ChimiException("Please specify a keyword to search for.");
                        }
                        String keyword = findCommandParts[1].trim();
                        ArrayList<Task> found = tasks.findTasks(keyword);

                        if (found.isEmpty()) {
                            ui.showMessage("No matching tasks found.");
                        } else {
                            ui.showMessage("Here are the matching tasks in your list:");
                            for (int i = 0; i < found.size(); i++) {
                                // Note: We use the task's original index if we want,
                                // but for simple search, just listing them 1, 2, 3 is fine.
                                ui.showMessage((i + 1) + "." + found.get(i));
                            }
                        }
                        break;

                default:
                    // This should not happen as Parser.parseCommand() validates commands
                    throw new ChimiException("Unknown command.");

                }

            } catch (ChimiException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    // Helper Methods

    private void handleMark(String fullCommand, boolean isDone) throws ChimiException {
        String[] markCommandParts = fullCommand.split(" ");
        if (markCommandParts.length < 2) {
            throw new ChimiException("Please specify which task to mark/unmark.");
        }
        try {
            int index = Integer.parseInt(markCommandParts[1]) - 1;
            Task task = tasks.get(index); // tasks.get() throws exception if out of range

            if (isDone) {
                task.markAsDone();
                ui.showMessage("Nice! I've marked this task as done:");
            } else {
                task.markAsUndone();
                ui.showMessage("OK, I've marked this task as not done yet:");
            }
            ui.showMessage("  " + task);
            storage.save(tasks.getAllTasks());
        } catch (NumberFormatException e) {
            throw new ChimiException("Please enter a valid number.");
        }
    }

    private void handleAdd(String fullCommand, Command command) throws ChimiException {
        Task newTask = null;
        switch (command) {
            case TODO:
                String todoDescription = fullCommand.substring(4).trim();
                if (todoDescription.isEmpty()) {
                    throw new ChimiException("The description of a todo cannot be empty.");
                }
                newTask = new Todo(todoDescription);
                break;
            case DEADLINE:
                int byIndex = fullCommand.indexOf("/by");
                if (byIndex == -1) {
                    throw new ChimiException("Deadlines must have a /by date.");
                }
                String deadlineDescription = fullCommand.substring(8, byIndex).trim();
                String deadlineBy = fullCommand.substring(byIndex + 4).trim();
                if (deadlineDescription.isEmpty() || deadlineBy.isEmpty()) {
                    throw new ChimiException("Description and date cannot be empty.");
                }
                newTask = new Deadline(deadlineDescription, deadlineBy);
                break;
            case EVENT:
                int fromIndex = fullCommand.indexOf("/from");
                int toIndex = fullCommand.indexOf("/to");
                if (fromIndex == -1 || toIndex == -1) {
                    throw new ChimiException("Events must have both /from and /to times.");
                }
                String eventDescription = fullCommand.substring(5, fromIndex).trim();
                String eventFrom = fullCommand.substring(fromIndex + 6, toIndex).trim();
                String eventTo = fullCommand.substring(toIndex + 4).trim();
                if (eventDescription.isEmpty()) {
                    throw new ChimiException("Description cannot be empty.");
                }
                newTask = new Event(eventDescription, eventFrom, eventTo);
                break;
            default:
                // This should not happen if called correctly, but required for checkstyle
                throw new ChimiException("Unknown task type.");
        }

        if (newTask != null) {
            tasks.add(newTask);
            ui.showMessage("Got it. I've added this task:");
            ui.showMessage("  " + newTask);
            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
            storage.save(tasks.getAllTasks());
        }
    }

    /**
    * The main method to start the Chimi application.
    *
    * @param args Command line arguments (not used).
    */
    public static void main(String[] args) {
        new Chimi("data/chimi.txt").run();
    }
}