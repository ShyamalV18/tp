package seedu.interntrackr.storage;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Handles reading from and writing to the local human-editable text file.
 */
public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    private String filePath;

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param filePath Path to the data file.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path cannot be null or empty";
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
            logger.info("No data file found at " + filePath + ". Starting fresh.");
            return applications;
        }

        logger.info("Loading data from " + filePath);
        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                lineNumber++;
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(" \\| ");
                if (parts.length < 3) {
                    logger.warning("Corrupted data at line " + lineNumber + ": " + line);
                    throw new InternTrackrException("Corrupted data at line " + lineNumber + ": " + line);
                }

                String status = parts[2].trim();
                if (!Application.isValidStatus(status)) {
                    logger.warning("Invalid status at line " + lineNumber + ": " + status);
                    throw new InternTrackrException("Corrupted data at line " + lineNumber
                            + ": Invalid status '" + status + "'");
                }

                status = Application.getNormalizedStatus(status);
                applications.add(new Application(parts[0], parts[1], status));
            }
        } catch (IOException e) {
            logger.severe("Failed to read file: " + e.getMessage());
            throw new InternTrackrException("Error reading file: " + e.getMessage());
        }

        logger.info("Loaded " + applications.size() + " applications.");
        assert applications != null : "Loaded applications list should not be null";
        return applications;
    }

    /**
     * Saves the current list of applications to disk.
     *
     * @param applications The list to save.
     * @throws InternTrackrException If the file cannot be written.
     */
    public void save(ArrayList<Application> applications) throws InternTrackrException {
        assert applications != null : "Applications list cannot be null";
        logger.info("Saving " + applications.size() + " applications to " + filePath);
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            for (Application app : applications) {
                writer.write(app.toStorageString() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            logger.severe("Failed to save file: " + e.getMessage());
            throw new InternTrackrException("Error saving file: " + e.getMessage());
        }
        logger.info("Save successful.");
    }
}
