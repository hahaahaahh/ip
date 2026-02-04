package chimi.parser;

import chimi.ChimiException;
import chimi.commands.Command; // Import Command enum
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the {@link Parser} class.
 * Verifies that commands are parsed correctly and exceptions are thrown for invalid inputs.
 */
public class ParserTest {

    /**
     * Tests that valid command strings are parsed into the correct Command enum values.
     *
     * @throws ChimiException If a command parsing error occurs unexpectedly.
     */
    @Test
    public void parseValidCommandSuccess() throws ChimiException {
        // Test simple parsing
        assertEquals(Command.TODO, Parser.parseCommand("todo read book"));
        assertEquals(Command.BYE, Parser.parseCommand("bye"));
    }

    /**
     * Tests that invalid command strings throw a {@link ChimiException}.
     */
    @Test
    public void parseInvalidCommandExceptionThrown() {
        // Test that nonsense inputs throw exceptions
        try {
            Parser.parseCommand("blah blah blah");
            fail(); // Should not reach here
        } catch (ChimiException e) {
            assertEquals("I'm sorry, but I don't know what that means :-(", e.getMessage());
        }
    }
}