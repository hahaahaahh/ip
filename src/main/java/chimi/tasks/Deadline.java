package chimi.tasks;

import java.time.LocalDate;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import chimi.ChimiException;

/**
 * Represents a Deadline task.
 * A Deadline is a task that needs to be done before a specific date.
 */
public class Deadline extends Task {

    protected LocalDate byDate; // Store as LocalDate

    /**
     * Constructs a Deadline task.
     *
     * @param description The description of the deadline.
     * @param by          The date by which the task must be done (in yyyy-mm-dd format).
     * @throws ChimiException If the date format is invalid.
     */
    public Deadline(String description, String by) throws ChimiException {
        super(description);
        try {
            this.byDate = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new ChimiException("Invalid date format. Please use yyyy-mm-dd (e.g., 2019-10-15).");
        }
    }

    /**
     * Returns the string representation of the Deadline task.
     *
     * @return String representation with [D] prefix and formatted date.
     */
    @Override
    public String toString() {
        String formattedDate = byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH));
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }

    /**
     * Formats the Deadline task for file storage.
     *
     * @return The formatted string for storage.
     */
    @Override
    public String toFileString() {
        return "D" + super.toFileString() + " | " + byDate;
    }
}