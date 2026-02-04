package chimi.tasks;

/**
 * Represents a generic task in the Chimi application.
 * A task has a description and a completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the given description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Formats the task as a string for file storage.
     *
     * @return A string representation of the task for file storage.
     */
    public String toFileString() {
        return " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the status icon depending on whether the task is done.
     *
     * @return "X" if done, " " otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the string representation of the task.
     *
     * @return The string representation of the task, including status icon and description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}