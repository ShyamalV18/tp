package seedu.interntrackr.storage;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.Deadline;
import seedu.interntrackr.model.DeadlineList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.List;

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
     * Note: This strictly expects the updated format containing the salary field.
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

                String[] parts = line.split(" \\| ", -1);

                if (parts.length < 6) {
                    logger.warning("Corrupted data at line " + lineNumber + " (Missing base fields): " + line);
                    throw new InternTrackrException("Corrupted data at line " + lineNumber + ": " + line
                            + "\n(Note: If you are using an old save file, please delete it and start fresh).");
                }

                String company = parts[0].trim();
                String role = parts[1].trim();
                String status = parts[2].trim();
                String contactName = parts[3].trim();
                String contactEmail = parts[4].trim();

                if (!Application.isValidStatus(status)) {
                    logger.warning("Invalid status at line " + lineNumber + ": " + status);
                    throw new InternTrackrException("Corrupted data at line " + lineNumber
                            + ": Invalid status '" + status + "'");
                }

                status = Application.getNormalizedStatus(status);

                String salaryStr = parts[5].trim();
                Double salary = null;
                if (!salaryStr.equals("-")) {
                    try {
                        salary = Double.parseDouble(salaryStr);
                    } catch (NumberFormatException e) {
                        logger.warning("Invalid salary format at line " + lineNumber + ": " + salaryStr);
                        throw new InternTrackrException("Corrupted salary data at line " + lineNumber
                                + ": '" + salaryStr + "'");
                    }
                }

                if ((parts.length - 6) % 3 != 0) {
                    logger.warning("Corrupted deadline data at line " + lineNumber + ": " + line);
                    throw new InternTrackrException("Corrupted deadline data at line " + lineNumber + ": " + line);
                }

                DeadlineList deadlineList = new DeadlineList();
                if (parts.length > 6) {
                    try {
                        for (int i = 6; i < parts.length; i += 3) {
                            String deadlineType = parts[i].trim();
                            LocalDate dueDate = LocalDate.parse(parts[i + 1].trim());
                            boolean isDone = Boolean.parseBoolean(parts[i + 2].trim());

                            Deadline deadline = new Deadline(deadlineType, dueDate, isDone);
                            deadlineList.addDeadline(deadline);
                        }
                    } catch (DateTimeParseException e) {
                        logger.warning("Invalid deadline date at line " + lineNumber + ": " + line);
                        throw new InternTrackrException("Corrupted deadline date at line "
                                + lineNumber + ": " + line);
                    }
                }

                Application app = new Application(company, role, status, contactName, contactEmail, deadlineList);
                app.setSalary(salary);
                applications.add(app);
                logger.fine("Loaded application at line " + lineNumber);
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
    public void save(List<Application> applications) throws InternTrackrException {
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
