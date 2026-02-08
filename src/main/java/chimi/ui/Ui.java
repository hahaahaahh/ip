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
        showLine();
        showMessage("Hello! I'm Chimi", "What can I do for you?");
        showLine();
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
     * Displays generic messages to the user.
     *
     * @param messages The messages to display.
     */
    public void showMessage(String... messages) {
        for (String message : messages) {
            System.out.println(" " + message);
        }
    }
}