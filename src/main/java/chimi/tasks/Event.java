package chimi.tasks;

/**
 * Represents an Event task.
 * An Event is a task that starts at a specific time and ends at a specific time.
 */
public class Event extends Task {

    protected String from;
    protected String to;

    /**
     * Constructs an Event task.
     *
     * @param description The description of the event.
     * @param from        The start time of the event.
     * @param to          The end time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Formats the Event task for file storage.
     *
     * @return The formatted string for storage.
     */
    @Override
    public String toFileString() {
        return "E" + super.toFileString() + " | " + from + " | " + to;
    }

    /**
     * Returns the string representation of the Event task.
     *
     * @return String representation with [E] prefix and time range.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}