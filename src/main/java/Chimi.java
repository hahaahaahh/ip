public class Chimi {

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

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
                        String[] dParts = fullCommand.split(" ");
                        if (dParts.length < 2) throw new ChimiException("Please specify which task to delete.");
                        try {
                            int dIdx = Integer.parseInt(dParts[1]) - 1;
                            Task deleted = tasks.delete(dIdx);
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
        String[] parts = fullCommand.split(" ");
        if (parts.length < 2) throw new ChimiException("Please specify which task to mark/unmark.");
        try {
            int index = Integer.parseInt(parts[1]) - 1;
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
                String tDesc = fullCommand.substring(4).trim();
                if (tDesc.isEmpty()) throw new ChimiException("The description of a todo cannot be empty.");
                newTask = new Todo(tDesc);
                break;
            case DEADLINE:
                int byIndex = fullCommand.indexOf("/by");
                if (byIndex == -1) throw new ChimiException("Deadlines must have a /by date.");
                String dDesc = fullCommand.substring(8, byIndex).trim();
                String by = fullCommand.substring(byIndex + 4).trim();
                if (dDesc.isEmpty() || by.isEmpty()) throw new ChimiException("Description and date cannot be empty.");
                newTask = new Deadline(dDesc, by);
                break;
            case EVENT:
                int fromIndex = fullCommand.indexOf("/from");
                int toIndex = fullCommand.indexOf("/to");
                if (fromIndex == -1 || toIndex == -1) throw new ChimiException("Events must have both /from and /to times.");
                String eDesc = fullCommand.substring(5, fromIndex).trim();
                String from = fullCommand.substring(fromIndex + 6, toIndex).trim();
                String to = fullCommand.substring(toIndex + 4).trim();
                if (eDesc.isEmpty()) throw new ChimiException("Description cannot be empty.");
                newTask = new Event(eDesc, from, to);
                break;
        }

        if (newTask != null) {
            tasks.add(newTask);
            ui.showMessage("Got it. I've added this task:");
            ui.showMessage("  " + newTask);
            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
            storage.save(tasks.getAllTasks());
        }
    }

    public static void main(String[] args) {
        new Chimi("data/chimi.txt").run();
    }
}