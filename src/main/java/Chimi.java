import java.util.Scanner;
public class Chimi {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println(line);
        System.out.println(" Hello! I'm Chimi");
        System.out.println(" What can I do for you?");
        System.out.println(line);

        while(true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println(line);
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            }   else if (input.equals("list")) {
                System.out.println(line);
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                System.out.println(line);
            }   else if (input.startsWith("mark")) {
                String[] parts = input.split(" ");
                System.out.println(line);
                int index = Integer.parseInt(parts[1]) - 1; // Convert 1-based to 0-based

                tasks[index].markAsDone();

                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[index]);
                System.out.println(line);
            } else if (input.startsWith("unmark")) {
                System.out.println(line);
                String[] parts = input.split(" ");
                int index = Integer.parseInt(parts[1]) - 1;

                tasks[index].markAsUndone();

                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[index]);
                System.out.println(line);
            } else {
                Task newTask = null;

                if (input.startsWith("todo")) {
                    String description = input.substring(5).trim();
                    newTask = new Todo(description);
                } else if (input.startsWith("deadline")) {
                    // split "deadline return book /by Sunday"
                    // index of /by is used to slice the string
                    int byIndex = input.indexOf("/by");
                    String description = input.substring(9, byIndex).trim(); // Remove "deadline "
                    String by = input.substring(byIndex + 4).trim(); // Remove "/by "
                    newTask = new Deadline(description, by);
                } else if (input.startsWith("event")) {
                    // split "event meeting /from Mon 2pm /to 4pm"
                    int fromIndex = input.indexOf("/from");
                    int toIndex = input.indexOf("/to");

                    String description = input.substring(6, fromIndex).trim(); // Remove "event "
                    String from = input.substring(fromIndex + 6, toIndex).trim(); // Remove "/from "
                    String to = input.substring(toIndex + 4).trim(); // Remove "/to "

                    newTask = new Event(description, from, to);
                }

                if (newTask != null) {
                    tasks[taskCount] = newTask;
                    taskCount++;
                    System.out.println(line);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println(line);
                }
            }
        }

        scanner.close();
    }
}
