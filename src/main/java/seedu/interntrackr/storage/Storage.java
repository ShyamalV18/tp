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
import java.util.List;
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
                Application app = parseLine(line, lineNumber);
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
     * Parses a single line from the data file into an Application object.
     *
     * @param line       The raw line string.
     * @param lineNumber The line number for error reporting.
     * @return The parsed Application object.
     * @throws InternTrackrException If the line is malformed.
     */
    private Application parseLine(String line, int lineNumber) throws InternTrackrException {
        String[] parts = line.split(" \\| ", -1);

        validateMinimumFields(parts, lineNumber, line);

        String company = parts[0].trim();
        String role = parts[1].trim();
        String status = parseStatus(parts[2].trim(), lineNumber);
        String contactName = parts[3].trim();
        String contactEmail = parts[4].trim();
        Double salary = parseSalary(parts[5].trim(), lineNumber);
        String note = parseNote(parts[6].trim());
        DeadlineList deadlineList = parseDeadlines(parts, lineNumber, line);

        Application app = new Application(company, role, status, contactName, contactEmail, deadlineList);
        app.setSalary(salary);
        app.setNote(note);
        return app;
    }

    /**
     * Validates that a line has the minimum required number of fields.
     *
     * @param parts      The split line parts.
     * @param lineNumber The line number for error reporting.
     * @param line       The original line string.
     * @throws InternTrackrException If there are fewer than 7 fields.
     */
    private void validateMinimumFields(String[] parts, int lineNumber, String line) throws InternTrackrException {
        if (parts.length < 7) {
            logger.warning("Corrupted data at line " + lineNumber + ": " + line);
            throw new InternTrackrException("Corrupted data at line " + lineNumber + ": " + line
                    + "\n(Note: If you are using an old save file, please delete it and start fresh).");
        }
    }

    /**
     * Parses and validates the status field.
     *
     * @param statusStr  The raw status string.
     * @param lineNumber The line number for error reporting.
     * @return The normalized status string.
     * @throws InternTrackrException If the status is not valid.
     */
    private String parseStatus(String statusStr, int lineNumber) throws InternTrackrException {
        if (!Application.isValidStatus(statusStr)) {
            logger.warning("Invalid status at line " + lineNumber + ": " + statusStr);
            throw new InternTrackrException("Corrupted data at line " + lineNumber
                    + ": Invalid status '" + statusStr + "'");
        }
        return Application.getNormalizedStatus(statusStr);
    }

    /**
     * Parses the salary field, returning null if the field is a dash.
     *
     * @param salaryStr  The raw salary string.
     * @param lineNumber The line number for error reporting.
     * @return The parsed salary, or null if not set.
     * @throws InternTrackrException If the salary string is not a valid number.
     */
    private Double parseSalary(String salaryStr, int lineNumber) throws InternTrackrException {
        if (salaryStr.equals("-")) {
            return null;
        }
        try {
            return Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            logger.warning("Invalid salary at line " + lineNumber + ": " + salaryStr);
            throw new InternTrackrException("Corrupted salary data at line " + lineNumber
                    + ": '" + salaryStr + "'");
        }
    }

    /**
     * Parses the note field, returning null if the field is a dash.
     *
     * @param noteStr The raw note string.
     * @return The note string, or null if not set.
     */
    private String parseNote(String noteStr) {
        return noteStr.equals("-") ? null : noteStr;
    }

    /**
     * Parses deadline fields from the remaining parts array.
     *
     * @param parts      The full split line parts.
     * @param lineNumber The line number for error reporting.
     * @param line       The original line string.
     * @return A DeadlineList containing all parsed deadlines.
     * @throws InternTrackrException If the deadline data is malformed.
     */
    private DeadlineList parseDeadlines(String[] parts, int lineNumber, String line) throws InternTrackrException {
        DeadlineList deadlineList = new DeadlineList();
        if (parts.length <= 7) {
            return deadlineList;
        }

        if ((parts.length - 7) % 3 != 0) {
            logger.warning("Corrupted deadline data at line " + lineNumber + ": " + line);
            throw new InternTrackrException("Corrupted deadline data at line " + lineNumber + ": " + line);
        }

        try {
            for (int i = 7; i < parts.length; i += 3) {
                String deadlineType = parts[i].trim();
                LocalDate dueDate = LocalDate.parse(parts[i + 1].trim());
                boolean isDone = Boolean.parseBoolean(parts[i + 2].trim());
                deadlineList.addDeadline(new Deadline(deadlineType, dueDate, isDone));
            }
        } catch (DateTimeParseException e) {
            logger.warning("Invalid deadline date at line " + lineNumber + ": " + line);
            throw new InternTrackrException("Corrupted deadline date at line " + lineNumber + ": " + line);
        }

        return deadlineList;
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
