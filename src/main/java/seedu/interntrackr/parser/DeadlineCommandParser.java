package seedu.interntrackr.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.logging.Logger;

import seedu.interntrackr.command.Command;
import seedu.interntrackr.command.DeadlineAddCommand;
import seedu.interntrackr.command.DeadlineDeleteCommand;
import seedu.interntrackr.command.DeadlineDoneCommand;
import seedu.interntrackr.command.DeadlineListCommand;
import seedu.interntrackr.command.DeadlineUndoneCommand;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Parses user input arguments for deadline subcommands.
 */
public class DeadlineCommandParser {
    private static final Logger logger = Logger.getLogger(DeadlineCommandParser.class.getName());

    private static final String PREFIX_TYPE = " t/";
    private static final String PREFIX_DATE = " d/";
    private static final String DATE_FORMAT = "dd-MM-uuuu";
    private static final String PREFIX_DEADLINE_INDEX = " i/";

    private static final String DEADLINE_ADD_USAGE =
            "Invalid format. Usage: deadline add INDEX t/TYPE d/DD-MM-YYYY";
    private static final String DEADLINE_DONE_USAGE =
            "Invalid format. Usage: deadline done INDEX i/DEADLINE_INDEX";
    private static final String DEADLINE_UNDONE_USAGE =
            "Invalid format. Usage: deadline undone INDEX i/DEADLINE_INDEX";
    private static final String DEADLINE_DELETE_USAGE =
            "Invalid format. Usage: deadline delete INDEX i/DEADLINE_INDEX";
    private static final String DEADLINE_LIST_USAGE =
            "Invalid format. Usage: deadline list INDEX";

    private static final String DEADLINE_USAGE =
            "Invalid command format!\n"
                    + "Available deadline commands:\n"
                    + "  deadline add INDEX t/TYPE d/DD-MM-YYYY\n"
                    + "  deadline list INDEX\n"
                    + "  deadline done INDEX i/DEADLINE_INDEX\n"
                    + "  deadline undone INDEX i/DEADLINE_INDEX\n"
                    + "  deadline delete INDEX i/DEADLINE_INDEX";

    /**
     * Parses the given arguments and returns the corresponding deadline command.
     *
     * @param arguments The argument string following the "deadline" keyword.
     * @return The parsed deadline command.
     * @throws InternTrackrException If the format is invalid.
     */
    public static Command parse(String arguments) throws InternTrackrException {
        if (arguments == null || arguments.isBlank()) {
            throw new InternTrackrException(DEADLINE_USAGE);
        }

        String[] parts = arguments.trim().split(" ", 2);
        String subcommandWord = parts[0].toLowerCase();
        String subcommandArgs = parts.length > 1 ? parts[1].trim() : "";

        switch (subcommandWord) {
        case "add":
            return parseAddCommand(subcommandArgs);
        case "done":
            return parseDoneCommand(subcommandArgs);
        case "undone":
            return parseUndoneCommand(subcommandArgs);
        case "delete":
            return parseDeleteCommand(subcommandArgs);
        case "list":
            return parseListCommand(subcommandArgs);
        default:
            throw new InternTrackrException(DEADLINE_USAGE);
        }
    }

    private static DeadlineAddCommand parseAddCommand(String arguments) throws InternTrackrException {
        if (!arguments.contains(PREFIX_TYPE) || !arguments.contains(PREFIX_DATE)) {
            logger.warning("Deadline add command missing t/ or d/ parameter.");
            throw new InternTrackrException(DEADLINE_ADD_USAGE);
        }

        try {
            int typeIndex = arguments.indexOf(PREFIX_TYPE);
            int dateIndex = arguments.indexOf(PREFIX_DATE);

            if (typeIndex == -1 || dateIndex == -1 || typeIndex > dateIndex) {
                logger.warning("Deadline add command has incorrect parameter ordering.");
                throw new InternTrackrException(DEADLINE_ADD_USAGE);
            }

            int index = Integer.parseInt(arguments.substring(0, typeIndex).trim());
            if (index <= 0) {
                throw new InternTrackrException("The application index must be a positive number.");
            }

            String deadlineType = arguments.substring(
                    typeIndex + PREFIX_TYPE.length(), dateIndex).trim().replace("\"", "");
            String dueDateStr = arguments.substring(dateIndex + PREFIX_DATE.length()).trim().replace("\"", "");

            if (deadlineType.isEmpty() || dueDateStr.isEmpty()) {
                logger.warning("Deadline type or due date is empty.");
                throw new InternTrackrException("Deadline type and due date cannot be empty.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate dueDate = LocalDate.parse(dueDateStr, formatter);

            if (dueDate.isBefore(LocalDate.now())) {
                throw new InternTrackrException("Deadline date cannot be in the past.");
            }

            logger.fine("Parsed: DeadlineAddCommand index=" + index + " type=" + deadlineType);
            return new DeadlineAddCommand(index, deadlineType, dueDate);

        } catch (NumberFormatException e) {
            logger.warning("Deadline index is not a valid number.");
            throw new InternTrackrException("The application index must be a valid positive number.");
        } catch (DateTimeParseException e) {
            logger.warning("Deadline date format invalid.");
            throw new InternTrackrException("Invalid date format or non-existent date. Use valid DD-MM-YYYY.");
        }
    }

    private static DeadlineDoneCommand parseDoneCommand(String arguments) throws InternTrackrException {
        int[] indices = parseTwoIndices(arguments, PREFIX_DEADLINE_INDEX, DEADLINE_DONE_USAGE);
        return new DeadlineDoneCommand(indices[0], indices[1]);
    }

    private static DeadlineUndoneCommand parseUndoneCommand(String arguments) throws InternTrackrException {
        int[] indices = parseTwoIndices(arguments, PREFIX_DEADLINE_INDEX, DEADLINE_UNDONE_USAGE);
        return new DeadlineUndoneCommand(indices[0], indices[1]);
    }

    private static DeadlineDeleteCommand parseDeleteCommand(String arguments) throws InternTrackrException {
        int[] indices = parseTwoIndices(arguments, PREFIX_DEADLINE_INDEX, DEADLINE_DELETE_USAGE);
        return new DeadlineDeleteCommand(indices[0], indices[1]);
    }

    private static int[] parseTwoIndices(String arguments, String prefix, String usageMsg)
            throws InternTrackrException {
        if (arguments.isBlank() || !arguments.contains(prefix)) {
            logger.warning("Command missing prefix: " + prefix);
            throw new InternTrackrException(usageMsg);
        }

        try {
            int prefixPos = arguments.indexOf(prefix);
            if (prefixPos == -1) {
                throw new InternTrackrException(usageMsg);
            }

            String appIndexStr = arguments.substring(0, prefixPos).trim();
            String deadlineIndexStr = arguments.substring(prefixPos + prefix.length()).trim();

            if (appIndexStr.isEmpty() || deadlineIndexStr.isEmpty()) {
                throw new InternTrackrException(usageMsg);
            }

            int appIndex = Integer.parseInt(appIndexStr);
            int deadlineIndex = Integer.parseInt(deadlineIndexStr);

            if (appIndex <= 0 || deadlineIndex <= 0) {
                throw new InternTrackrException("Indices must be valid positive numbers.");
            }

            return new int[]{appIndex, deadlineIndex};

        } catch (NumberFormatException e) {
            logger.warning("Indices are not valid numbers.");
            throw new InternTrackrException("The application index and deadline index must be valid positive numbers.");
        }
    }

    private static DeadlineListCommand parseListCommand(String arguments) throws InternTrackrException {
        if (arguments.isBlank()) {
            throw new InternTrackrException(DEADLINE_LIST_USAGE);
        }

        try {
            int index = Integer.parseInt(arguments.trim());
            if (index <= 0) {
                throw new InternTrackrException("The application index must be a positive number.");
            }
            logger.fine("Parsed: DeadlineListCommand index=" + index);
            return new DeadlineListCommand(index);
        } catch (NumberFormatException e) {
            logger.warning("Deadline list index is not a number.");
            throw new InternTrackrException("The application index must be a valid positive number.");
        }
    }
}
