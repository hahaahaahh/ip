package chimi.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chimi.ChimiException;
import chimi.tasks.Deadline;
import chimi.tasks.Event;
import chimi.tasks.Task;
import chimi.tasks.Todo;

/**
 * Handles the loading and saving of tasks to a file.
 */
public class Storage {
    private final String filePath;

    private static final String SEPARATOR = " \\| ";

    /**
     * Constructs a Storage object with a specific file path.
     *
     * @param filePath The file path where tasks are stored.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path cannot be null or empty";
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the file.
     * Parses the content of the file and converts it into a list of tasks.
     *
     * @return An ArrayList of Task objects loaded from the file.
     * @throws ChimiException If an error occurs while reading the file.
     */
    public ArrayList<Task> load() throws ChimiException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks; // Return empty list if file doesn't exist
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                try {
                    Task task = parseTask(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (ChimiException | IllegalArgumentException e) {
                    System.err.println("Warning: Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            throw new ChimiException("Error loading file: " + e.getMessage());
        }
        return tasks;
    }

    private Task parseTask(String line) throws ChimiException {
        assert line != null : "Line to parse cannot be null";
        String[] fileParts = line.split(SEPARATOR);
        if (fileParts.length < 3) {
            throw new ChimiException("Invalid line format.");
        }

        String type = fileParts[0];
        boolean isDone = fileParts[1].equals("1");
        String taskDescription = fileParts[2];

        Task task;
        switch (type) {
            case "T":
                task = new Todo(taskDescription);
                break;
            case "D":
                if (fileParts.length < 4) {
                    throw new ChimiException("Invalid deadline format.");
                }
                assert fileParts[3] != null : "Deadline date cannot be null";
                task = new Deadline(taskDescription, fileParts[3]);
                break;
            case "E":
                if (fileParts.length < 5) {
                    throw new ChimiException("Invalid event format.");
                }
                assert fileParts[3] != null && fileParts[4] != null : "Event dates cannot be null";
                task = new Event(taskDescription, fileParts[3], fileParts[4]);
                break;
            default:
                throw new ChimiException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Saves the current list of tasks to the file.
     * Overwrites the existing file with the current task data.
     *
     * @param tasks The list of tasks to be saved.
     */
    public void save(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list to save cannot be null";

        try {
            File file = new File(filePath);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            List<String> lines = tasks.stream()
                    .map(Task::toFileString)
                    .collect(Collectors.toList());
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
}