package chimi.parser;

import chimi.ChimiException;
import chimi.commands.Command;

public class Parser {
    public static Command parseCommand(String fullCommand) throws ChimiException {
        String[] parts = fullCommand.split(" ", 2);
        try {
            return Command.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ChimiException("I'm sorry, but I don't know what that means :-(");
        }
    }
}