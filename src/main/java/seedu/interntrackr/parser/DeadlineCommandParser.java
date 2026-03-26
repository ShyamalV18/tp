package seedu.interntrackr.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import seedu.interntrackr.command.DeadlineCommand;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Parses user input arguments for the deadline command.
 */
public class DeadlineCommandParser {
    private static final Logger logger = Logger.getLogger(DeadlineCommandParser.class.getName());

    private static final String PREFIX_TYPE = " t/";
    private static final String PREFIX_DATE = " d/";
    private static final String PREFIX_NOTES = " n/";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String DEADLINE_SUBCOMMAND = "add ";

    /**
     * Parses the given arguments and returns a DeadlineCommand.
     *
     * @param arguments The argument string following the "deadline" keyword.
     * @return A new DeadlineCommand with the parsed index, type, and due date.
     * @throws InternTrackrException If the format is invalid, the index is non-numeric,
     *     or the date format is incorrect.
     */
    public static DeadlineCommand parse(String arguments) throws InternTrackrException {
        if (!arguments.startsWith(DEADLINE_SUBCOMMAND)) {
            logger.warning("Deadline command missing 'add' subcommand.");
            throw new InternTrackrException(
                    "Invalid format. Usage: deadline add INDEX t/TYPE d/DD-MM-YYYY [n/NOTES]");
        }

        String subArgs = arguments.substring(DEADLINE_SUBCOMMAND.length()).trim();

        if (!subArgs.contains(PREFIX_TYPE) || !subArgs.contains(PREFIX_DATE)) {
            logger.warning("Deadline add command missing t/ or d/ parameter.");
            throw new InternTrackrException(
                    "Invalid format. Usage: deadline add INDEX t/TYPE d/DD-MM-YYYY [n/NOTES]");
        }

        try {
            int typeIndex = subArgs.indexOf(PREFIX_TYPE);
            int dateIndex = subArgs.indexOf(PREFIX_DATE);

            if (typeIndex == -1 || dateIndex == -1 || typeIndex > dateIndex) {
                logger.warning("Deadline add command has incorrect parameter ordering.");
                throw new InternTrackrException(
                        "Invalid format. Usage: deadline add INDEX t/TYPE d/DD-MM-YYYY");
            }

            int index = Integer.parseInt(subArgs.substring(0, typeIndex).trim());
            String deadlineType = subArgs.substring(
                    typeIndex + PREFIX_TYPE.length(), dateIndex).trim().replace("\"", "");
            String dueDateStr = extractDueDateStr(subArgs, dateIndex, deadlineType);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate dueDate = LocalDate.parse(dueDateStr, formatter);

            logger.fine("Parsed: DeadlineCommand index=" + index + " type=" + deadlineType);
            return new DeadlineCommand(index, deadlineType, dueDate);

        } catch (NumberFormatException e) {
            logger.warning("Deadline index is not a number.");
            throw new InternTrackrException("The application index must be a number.");
        } catch (DateTimeParseException e) {
            logger.warning("Deadline date format invalid.");
            throw new InternTrackrException("Invalid date format. Use DD-MM-YYYY.");
        }
    }

    /**
     * Extracts and returns the due date string from the deadline subcommand arguments.
     *
     * @param subArgs The full argument string for the deadline subcommand.
     * @param dateIndex The index of the d/ prefix within subArgs.
     * @param deadlineType The already-parsed deadline type, used for empty validation.
     * @return The trimmed due date string.
     * @throws InternTrackrException If the deadline type or due date string is empty.
     */
    private static String extractDueDateStr(String subArgs, int dateIndex, String deadlineType)
            throws InternTrackrException {
        assert subArgs != null : "subArgs must not be null";
        assert dateIndex >= 0 : "dateIndex must be non-negative";

        String dueDateStr = subArgs.substring(dateIndex + PREFIX_DATE.length()).trim().replace("\"", "");

        int notesIndex = dueDateStr.indexOf(PREFIX_NOTES);
        if (notesIndex != -1) {
            dueDateStr = dueDateStr.substring(0, notesIndex).trim();
        }

        if (deadlineType.isEmpty() || dueDateStr.isEmpty()) {
            logger.warning("Deadline type or due date is empty.");
            throw new InternTrackrException("Deadline type and due date cannot be empty.");
        }
        return dueDateStr;
    }
}
