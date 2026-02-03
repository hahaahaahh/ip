package chimi.tasks;

import chimi.ChimiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DeadlineTest {

    @Test
    public void testStringConversion() throws ChimiException {
        // Test that valid input produces the correct output string
        Deadline d = new Deadline("return book", "2019-12-02");
        assertEquals("[D][ ] return book (by: Dec 2 2019)", d.toString());
    }

    @Test
    public void testFileStringConversion() throws ChimiException {
        // Test that it saves to file format correctly
        Deadline d = new Deadline("return book", "2019-12-02");
        assertEquals("D | 0 | return book | 2019-12-02", d.toFileString());
    }

    @Test
    public void testInvalidDate() {
        // Test that invalid dates throw an exception
        try {
            new Deadline("return book", "Monday"); // This should fail
            fail(); // If the line above didn't throw an exception, the test failed
        } catch (ChimiException e) {
            // We expected this exception, so the test passed!
            assertEquals("Invalid date format. Please use yyyy-mm-dd (e.g., 2019-10-15).", e.getMessage());
        }
    }
}