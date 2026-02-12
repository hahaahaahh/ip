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
                ui.showLine();
                Command command = Parser.parseCommand(fullCommand);

                if (command == Command.BYE) {
                    isExit = true;
                }

                String response = getResponse(fullCommand);
                ui.showMessage(response);

            } catch (ChimiException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parseCommand(input);
            return switch (command) {
                case BYE -> "Bye. Hope to see you again soon!";
                case LIST -> handleList();
                case MARK -> handleMark(input, true);
                case UNMARK -> handleMark(input, false);
                case DELETE -> handleDelete(input);
                case FIND -> handleFind(input);
                case TODO, DEADLINE, EVENT -> handleAdd(input, command);
                default -> throw new ChimiException("Unknown command.");
            };
        } catch (ChimiException e) {
            return "Error: " + e.getMessage();
        }
    }

    // --- Command Handlers ---

    /**
     * Handles the list command.
     *
     * @return The formatted list of tasks.
     * @throws ChimiException If there is an error retrieving tasks.
     */
    private String handleList() throws ChimiException {
        if (tasks.size() == 0) {
            return "No tasks added yet!";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(".").append(tasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Handles the delete command.
     *
     * @param input The full user input.
     * @return The response message.
     * @throws ChimiException If the index is invalid.
     */
    private String handleDelete(String input) throws ChimiException {
        int index = parseIndex(input);
        Task deleted = tasks.delete(index);
        storage.save(tasks.getAllTasks());
        return "Noted. I've removed this task:\n" +
               "  " + deleted + "\n" +
               "Now you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the find command.
     *
     * @param input The full user input.
     * @return The response message with matching tasks.
     * @throws ChimiException If the keyword is missing.
     */
    private String handleFind(String input) throws ChimiException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            throw new ChimiException("Please specify a keyword to search for.");
        }
        String keyword = parts[1].trim();
        ArrayList<Task> found = tasks.findTasks(keyword);

        if (found.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < found.size(); i++) {
            sb.append(i + 1).append(".").append(found.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Handles the mark and unmark commands.
     *
     * @param input The full user input.
     * @param isDone True to mark as done, false to unmark.
     * @return The response message.
     * @throws ChimiException If the index is invalid.
     */
    private String handleMark(String input, boolean isDone) throws ChimiException {
        int index = parseIndex(input);
        Task task = tasks.get(index);

        String statusMsg;
        if (isDone) {
            task.markAsDone();
            statusMsg = "Nice! I've marked this task as done:\n";
        } else {
            task.markAsUndone();
            statusMsg = "OK, I've marked this task as not done yet:\n";
        }

        storage.save(tasks.getAllTasks());
        return statusMsg + "  " + task;
    }

    private int parseIndex(String input) throws ChimiException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new ChimiException("Please specify which task to operate on.");
        }
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new ChimiException("Please enter a valid number.");
        }
    }

    /**
     * Handles adding new tasks (todo, deadline, event).
     *
     * @param input The full user input.
     * @param command The command type.
     * @return The response message.
     * @throws ChimiException If the task details are invalid.
     */
    private String handleAdd(String input, Command command) throws ChimiException {
        assert command == Command.TODO || command == Command.DEADLINE || command == Command.EVENT
               : "handleAdd should only be called for TODO, DEADLINE, or EVENT commands";

        Task newTask = switch (command) {
            case TODO -> createTodo(input);
            case DEADLINE -> createDeadline(input);
            case EVENT -> createEvent(input);
            default -> throw new ChimiException("Unknown task type for add.");
        };

        tasks.add(newTask);
        storage.save(tasks.getAllTasks());
        return "Got it. I've added this task:\n" +
               "  " + newTask + "\n" +
               "Now you have " + tasks.size() + " tasks in the list.";
    }

    // --- Task Creation Helpers ---

    private Todo createTodo(String input) throws ChimiException {
        String description = input.substring(4).trim();
        if (description.isEmpty()) {
            throw new ChimiException("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    private Deadline createDeadline(String input) throws ChimiException {
        int byIndex = input.indexOf("/by");
        if (byIndex == -1) {
            throw new ChimiException("Deadlines must have a /by date.");
        }
        String description = input.substring(8, byIndex).trim();
        String by = input.substring(byIndex + 4).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new ChimiException("Description and date cannot be empty.");
        }
        return new Deadline(description, by);
    }

    private Event createEvent(String input) throws ChimiException {
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");
        if (fromIndex == -1 || toIndex == -1) {
            throw new ChimiException("Events must have both /from and /to times.");
        }
        String description = input.substring(5, fromIndex).trim();
        String from = input.substring(fromIndex + 6, toIndex).trim();
        String to = input.substring(toIndex + 4).trim();
        if (description.isEmpty()) {
            throw new ChimiException("Description cannot be empty.");
        }
        return new Event(description, from, to);
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

