import java.util.Scanner;

public class Ui {
    private final Scanner scanner;
    private static final String LINE = "____________________________________________________________";

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Chimi");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    public void showLoadingError() {
        System.out.println("Problem loading file. Starting with an empty list.");
    }

    public void showMessage(String message) {
        System.out.println(" " + message);
    }
}