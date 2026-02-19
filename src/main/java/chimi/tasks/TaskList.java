package chimi.tasks;

import java.util.ArrayList;
import java.util.stream.Collectors;

import chimi.ChimiException;

/**
 * Represents a list of tasks.
 * Provides operations to add, delete, and retrieve tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with an existing list of tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     * @throws ChimiException If the task already exists in the list.
     */
    public void add(Task task) throws ChimiException {
        if (tasks.contains(task)) {
            throw new ChimiException("Hold on! This exact task already exists in your list.");
        }
        tasks.add(task);
    }

    /**
     * Deletes a task from the list at the specified index.
     *
     * @param index The 0-based index of the task to delete.
     * @return The task that was deleted.
     * @throws ChimiException If the index is out of range.
     */
    public Task delete(int index) throws ChimiException {
        if (index < 0 || index >= tasks.size()) {
            throw new ChimiException("Task number is out of range.");
        }
        return tasks.remove(index);
    }

    /**
     * Retrieves a task at the specified index.
     *
     * @param index The 0-based index of the task to retrieve.
     * @return The task at the specified index.
     * @throws ChimiException If the index is out of range.
     */
    public Task get(int index) throws ChimiException {
        if (index < 0 || index >= tasks.size()) {
            throw new ChimiException("Task number is out of range.");
        }
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves all tasks in the list.
     *
     * @return An ArrayList containing all tasks.
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> findTasks(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}