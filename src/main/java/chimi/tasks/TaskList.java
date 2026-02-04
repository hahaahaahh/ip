package chimi.tasks;

import java.util.ArrayList;
import chimi.ChimiException;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) throws ChimiException {
        if (index < 0 || index >= tasks.size()) throw new ChimiException("Task number is out of range.");
        return tasks.remove(index);
    }

    public Task get(int index) throws ChimiException {
        if (index < 0 || index >= tasks.size()) throw new ChimiException("Task number is out of range.");
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) { // Basic string matching
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}