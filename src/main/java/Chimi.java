import java.util.Scanner;
import java.util.ArrayList;

public class Chimi {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println(line);
        System.out.println(" Hello! I'm Chimi");
        System.out.println(" What can I do for you?");
        System.out.println(line);

        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);

            String[] parts = input.split(" ", 2);
            String commandWord = parts[0].toUpperCase();

            try {
                Command command = Command.valueOf(commandWord);

                switch (command) {
                    case BYE:
                        System.out.println(" Bye. Hope to see you again soon!");
                        System.out.println(line);
                        scanner.close();
                        return; // Exit the program immediately

                    case LIST:
                        if (tasks.isEmpty()) {
                            System.out.println(" No tasks added yet!");
                        } else {
                            System.out.println(" Here are the tasks in your list:");
                            for (int i = 0; i < tasks.size(); i++) {
                                System.out.println(" " + (i + 1) + "." + tasks.get(i));
                            }
                        }
                        System.out.println(line);
                        break;

                    case MARK:
                        handleMark(input, tasks, true);
                        System.out.println(line);
                        break;

                    case UNMARK:
                        handleMark(input, tasks, false);
                        System.out.println(line);
                        break;

                    case DELETE:
                        handleDelete(input, tasks);
                        System.out.println(line);
                        break;

                    case TODO:
                    case DEADLINE:
                    case EVENT:
                        handleAddTask(input, tasks, command);
                        System.out.println(line);
                        break;
                }

            } catch (IllegalArgumentException e) {
                // This catches "blah", "hello", etc. (Commands not in the Enum)
                System.out.println(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                System.out.println(line);
            } catch (ChimiException e) {
                System.out.println(" OOPS!!! " + e.getMessage());
                System.out.println(line);
            }
        }
    }

    // Helper Methods to keep main() clean ---

    private static void handleMark(String input, ArrayList<Task> tasks, boolean isDone) throws ChimiException {
        String[] parts = input.split(" ");
        if (parts.length < 2) throw new ChimiException("Please specify which task to mark/unmark.");
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.size()) throw new ChimiException("Task number is out of range.");

            if (isDone) {
                tasks.get(index).markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
            } else {
                tasks.get(index).markAsUndone();
                System.out.println(" OK, I've marked this task as not done yet:");
            }
            System.out.println("   " + tasks.get(index));
        } catch (NumberFormatException e) {
            throw new ChimiException("Please enter a valid number.");
        }
    }

    private static void handleDelete(String input, ArrayList<Task> tasks) throws ChimiException {
        String[] parts = input.split(" ");
        if (parts.length < 2) throw new ChimiException("Please specify which task to delete.");
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.size()) throw new ChimiException("Task number is out of range.");
            Task removed = tasks.remove(index);
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + removed);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new ChimiException("Please enter a valid number.");
        }
    }

    private static void handleAddTask(String input, ArrayList<Task> tasks, Command command) throws ChimiException {
        Task newTask = null;

        switch (command) {
            case TODO:
                String tDesc = input.substring(4).trim();
                if (tDesc.isEmpty()) throw new ChimiException("The description of a todo cannot be empty.");
                newTask = new Todo(tDesc);
                break;
            case DEADLINE:
                int byIndex = input.indexOf("/by");
                if (byIndex == -1) throw new ChimiException("Deadlines must have a /by date.");
                String dDesc = input.substring(8, byIndex).trim();
                String by = input.substring(byIndex + 4).trim();
                if (dDesc.isEmpty() || by.isEmpty()) throw new ChimiException("Description and date cannot be empty.");
                newTask = new Deadline(dDesc, by);
                break;
            case EVENT:
                int fromIndex = input.indexOf("/from");
                int toIndex = input.indexOf("/to");
                if (fromIndex == -1 || toIndex == -1) throw new ChimiException("Events must have both /from and /to times.");
                String eDesc = input.substring(5, fromIndex).trim();
                String from = input.substring(fromIndex + 6, toIndex).trim();
                String to = input.substring(toIndex + 4).trim();
                if (eDesc.isEmpty()) throw new ChimiException("Description cannot be empty.");
                newTask = new Event(eDesc, from, to);
                break;
        }

        if (newTask != null) {
            tasks.add(newTask);
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + newTask);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        }
    }
}