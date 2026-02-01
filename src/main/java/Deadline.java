import java.time.LocalDate;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {

    protected LocalDate byDate; // Store as LocalDate

    public Deadline(String description, String by) throws ChimiException {
        super(description);
        try {
            this.byDate = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new ChimiException("Invalid date format. Please use yyyy-mm-dd (e.g., 2019-10-15).");
        }
    }

    @Override
    public String toString() {
        String formattedDate = byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH));
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }

    @Override
    public String toFileString() {
        return "D" + super.toFileString() + " | " + byDate;
    }
}