package chimi.parser;

import chimi.ChimiException;
import chimi.commands.Command;

/**
 * Parses user input into commands.
 */
public class Parser {
    /**
     * Parses the full command string entered by the user.
     * Extracts the command keyword and returns the corresponding Command enum.
     *
     * @param fullCommand The full user input string.
     * @return The Command corresponding to the user input.
     * @throws ChimiException If the command is not recognized.
     */
    public static Command parseCommand(String fullCommand) throws ChimiException {
        String[] parts = fullCommand.split(" ", 2);
        try {
            return Command.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ChimiException("I'm sorry, but I don't know what that means :-(");
        }
    }
}