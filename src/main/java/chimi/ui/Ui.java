package chimi.ui;

import java.util.Scanner;

/**
 * Handles interactions with the user.
 * Manages input reading and output display.
 */
public class Ui {
    private final Scanner scanner;
    private static final String LINE = "____________________________________________________________";

    /**
     * Constructs a Ui object and initializes the scanner for user input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads a command line from the user.
     *
     * @return The string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome message to the user.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Chimi");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Displays a horizontal separator line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    /**
     * Displays an error message when loading the file fails.
     */
    public void showLoadingError() {
        System.out.println("Problem loading file. Starting with an empty list.");
    }

    /**
     * Displays a generic message to the user.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        System.out.println(" " + message);
    }
}