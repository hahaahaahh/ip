package chimi.parser;

import chimi.ChimiException;
import chimi.commands.Command; // Import Command enum
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {

    @Test
    public void parse_validCommand_success() throws ChimiException {
        // Test simple parsing
        assertEquals(Command.TODO, Parser.parseCommand("todo read book"));
        assertEquals(Command.BYE, Parser.parseCommand("bye"));
    }

    @Test
    public void parse_invalidCommand_exceptionThrown() {
        // Test that nonsense inputs throw exceptions
        try {
            Parser.parseCommand("blah blah blah");
            fail(); // Should not reach here
        } catch (ChimiException e) {
            assertEquals("I'm sorry, but I don't know what that means :-(", e.getMessage());
        }
    }
}