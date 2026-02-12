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

    /**
     * Constructs a Storage object with a specific file path.
     *
     * @param filePath The file path where tasks are stored.
     */
    public Storage(String filePath) {
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

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");

                try {
                    Task task = null;
                    switch (parts[0]) {
                        case "T":
                            task = new Todo(parts[2]);
                            break;
                        case "D":
                            if (parts.length >= 4) {
                                task = new Deadline(parts[2], parts[3]);
                            }
                            break;
                        case "E":
                            if (parts.length >= 5) {
                                task = new Event(parts[2], parts[3], parts[4]);
                            }
                            break;
                        default:
                            System.out.println("Warning: Unknown task type '" + parts[0] + "' in file.");
                            break;
                    }

                    if (task != null) {
                        if (parts[1].equals("1")) {
                            task.markAsDone();
                        }
                        tasks.add(task);
                    }
                } catch (ChimiException e) {
                    System.out.println("Warning: Skipping corrupted line.");
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            throw new ChimiException("Error loading file.");
        }
        return tasks;
    }

    /**
     * Saves the current list of tasks to the file.
     * Overwrites the existing file with the current task data.
     *
     * @param tasks The list of tasks to be saved.
     */
    public void save(ArrayList<Task> tasks) {
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
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}