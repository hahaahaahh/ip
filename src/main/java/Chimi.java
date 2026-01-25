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
            }   else if (input.startsWith("list")) {
                System.out.println(line);
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
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
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(line);
                System.out.println(" added: " + input);
                System.out.println(line);
            }
        }

        scanner.close();
    }
}
