import java.util.Scanner;
public class Chimi {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String logo = "____________________________________________________________\n"
        + "Hello! I'm Chimi.\n"
        + "What can I do for you?\n"
        + "____________________________________________________________";
        System.out.println(logo);

        while(true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }
            System.out.println("____________________________________________________________");
            System.out.println(" " + input);
            System.out.println("____________________________________________________________");
        }

        scanner.close();
    }
}
