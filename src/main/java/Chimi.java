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

            try {
                if (input.equals("bye")) {
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                } else if (input.equals("list")) {
                    if (tasks.isEmpty()) {
                        System.out.println(" No tasks added yet!");
                    } else {
                        System.out.println(" Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println(" " + (i + 1) + "." + tasks.get(i));
                        }
                    }
                    System.out.println(line);
                } else if (input.startsWith("mark")) {
                    String[] parts = input.split(" ");
                    if (parts.length < 2) throw new ChimiException("Please specify which task to mark.");

                    try {
                        int index = Integer.parseInt(parts[1]) - 1;
                        if (index < 0 || index >= tasks.size()) throw new ChimiException("Task number is out of range.");
                        tasks.get(index).markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks.get(index));
                    } catch (NumberFormatException e) {
                        throw new ChimiException("Please enter a valid number.");
                    }
                    System.out.println(line);
                } else if (input.startsWith("unmark")) {
                    String[] parts = input.split(" ");
                    if (parts.length < 2) throw new ChimiException("Please specify which task to unmark.");

                    try {
                        int index = Integer.parseInt(parts[1]) - 1;
                        if (index < 0 || index >= tasks.size()) throw new ChimiException("Task number is out of range.");
                        tasks.get(index).markAsUndone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks.get(index));
                    } catch (NumberFormatException e) {
                        throw new ChimiException("Please enter a valid number.");
                    }
                    System.out.println(line);
                } else {
                    Task newTask = null;

                    if (input.startsWith("todo")) {
                        String description = input.substring(4).trim();
                        if (description.isEmpty()) {
                            throw new ChimiException("The description of a todo cannot be empty.");
                        }
                        newTask = new Todo(description);
                    } else if (input.startsWith("deadline")) {
                        int byIndex = input.indexOf("/by");
                        if (byIndex == -1) {
                            throw new ChimiException("Deadlines must have a /by date.");
                        }

                        String description = input.substring(8, byIndex).trim();
                        if (description.isEmpty()) {
                            throw new ChimiException("The description cannot be empty.");
                        }

                        String by = input.substring(byIndex + 4).trim();
                        if (by.isEmpty()) {
                            throw new ChimiException("The deadline date cannot be empty.");
                        }

                        newTask = new Deadline(description, by);
                    } else if (input.startsWith("event")) {
                        int fromIndex = input.indexOf("/from");
                        int toIndex = input.indexOf("/to");

                        if (fromIndex == -1 || toIndex == -1) {
                            throw new ChimiException("Events must have both /from and /to times.");
                        }

                        String description = input.substring(5, fromIndex).trim();
                        if (description.isEmpty()) {
                            throw new ChimiException("The event description cannot be empty.");
                        }

                        String from = input.substring(fromIndex + 6, toIndex).trim();
                        String to = input.substring(toIndex + 4).trim();

                        newTask = new Event(description, from, to);
                    } else if (input.startsWith("delete")) {
                        String[] parts = input.split(" ");
                        if (parts.length < 2) throw new ChimiException("Please specify which task to delete.");

                        try {
                            int index = Integer.parseInt(parts[1]) - 1;
                            if (index < 0 || index >= tasks.size())
                                throw new ChimiException("Task number is out of range.");

                            Task removedTask = tasks.remove(index);

                            System.out.println(" Noted. I've removed this task:");
                            System.out.println("   " + removedTask);
                            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        } catch (NumberFormatException e) {
                            throw new ChimiException("Please enter a valid number.");
                        }
                        System.out.println(line);
                    } else {
                        throw new ChimiException("I'm sorry, but I don't know what that means :-(");
                    }

                    if (newTask != null) {
                        tasks.add(newTask);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + newTask);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println(line);
                    }
                }
            } catch (ChimiException e) {
                System.out.println(" OOPS!!! " + e.getMessage());
                System.out.println(line);
            }
        }

        scanner.close();
    }
}