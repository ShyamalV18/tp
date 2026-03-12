package seedu.interntrackr.storage;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles reading from and writing to the local human-editable text file.
 */
public class Storage {
    private String filePath;

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param filePath Path to the data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads applications from the data file on disk.
     *
     * @return ArrayList of Application objects.
     * @throws InternTrackrException If the file cannot be read or data is corrupted.
     */
    public ArrayList<Application> load() throws InternTrackrException {
        ArrayList<Application> applications = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return applications;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(" \\| ");
                if (parts.length < 3) {
                    throw new InternTrackrException("Corrupted data line: " + line);
                }
                applications.add(new Application(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            throw new InternTrackrException("Error reading file: " + e.getMessage());
        }

        return applications;
    }

    /**
     * Saves the current list of applications to disk.
     *
     * @param applications The list to save.
     * @throws InternTrackrException If the file cannot be written.
     */
    public void save(ArrayList<Application> applications) throws InternTrackrException {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            for (Application app : applications) {
                writer.write(app.toStorageString() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new InternTrackrException("Error saving file: " + e.getMessage());
        }
    }
}
