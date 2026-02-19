package chimi.tasks;

/**
 * Represents a Todo task.
 * A Todo is a task without any specific date attached to it.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task.
     *
     * @param description The description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Formats the Todo task for file storage.
     *
     * @return The formatted string for storage.
     */
    @Override
    public String toFileString() {
        return "T" + super.toFileString();
    }

    /**
     * Returns the string representation of the Todo task.
     *
     * @return String representation with [T] prefix.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        // Todo description equality is handled by super.equals() since Todo adds no new fields
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}