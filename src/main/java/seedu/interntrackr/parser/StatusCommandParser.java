package seedu.interntrackr.parser;

import java.util.logging.Logger;

import seedu.interntrackr.command.StatusCommand;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Parses user input arguments for the status command.
 */
public class StatusCommandParser {
    private static final Logger logger = Logger.getLogger(StatusCommandParser.class.getName());

    private static final String PREFIX_STATUS = " s/";

    /**
     * Parses the given arguments and returns a StatusCommand.
     *
     * @param arguments The argument string following the "status" keyword.
     * @return A new StatusCommand with the parsed index and status.
     * @throws InternTrackrException If the format is invalid or the index is non-numeric.
     */
    public static StatusCommand parse(String arguments) throws InternTrackrException {
        if (!arguments.contains(PREFIX_STATUS)) {
            logger.warning("Status command missing s/ parameter.");
            throw new InternTrackrException("Invalid format. Usage: status INDEX s/STATUS");
        }
        String[] statusArgs = arguments.split(PREFIX_STATUS, 2);
        try {
            int index = Integer.parseInt(statusArgs[0].trim());
            String parsedStatus = statusArgs[1].replace("\"", "").trim();
            logger.fine("Parsed: StatusCommand index=" + index + " status=" + parsedStatus);
            return new StatusCommand(index, parsedStatus);
        } catch (NumberFormatException e) {
            logger.warning("Status index is not a number: \"" + statusArgs[0].trim() + "\"");
            throw new InternTrackrException("The application index must be a number.");
        }
    }
}
