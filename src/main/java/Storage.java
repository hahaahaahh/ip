import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
                            if (parts.length >= 4) task = new Deadline(parts[2], parts[3]);
                            break;
                        case "E":
                            if (parts.length >= 5) task = new Event(parts[2], parts[3], parts[4]);
                            break;
                    }

                    if (task != null) {
                        if (parts[1].equals("1")) task.markAsDone();
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

    public void save(ArrayList<Task> tasks) {
        try {
            File file = new File(filePath);
            File directory = file.getParentFile();
            if (!directory.exists()) directory.mkdirs();

            FileWriter fw = new FileWriter(file);
            for (Task task : tasks) {
                fw.write(task.toFileString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}