package seedu.interntrackr.parser;

import java.util.logging.Logger;

import seedu.interntrackr.command.DeleteCommand;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Parses user input arguments for the delete command.
 */
public class DeleteCommandParser {
    private static final Logger logger = Logger.getLogger(DeleteCommandParser.class.getName());

    /**
     * Parses the given arguments and returns a DeleteCommand.
     *
     * @param arguments The argument string following the "delete" keyword.
     * @return A new DeleteCommand with the parsed index.
     * @throws InternTrackrException If the index is missing, non-numeric, or non-positive.
     */
    public static DeleteCommand parse(String arguments) throws InternTrackrException {
        if (arguments.isEmpty()) {
            logger.warning("Delete command missing index.");
            throw new InternTrackrException("Invalid format. Usage: delete INDEX");
        }
        try {
            int index = Integer.parseInt(arguments.trim());
            if (index <= 0) {
                logger.warning("Delete index is non-positive: " + index);
                throw new InternTrackrException("Index must be a positive integer.");
            }
            logger.fine("Parsed: DeleteCommand index=" + index);
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            logger.warning("Delete index is not a number: \"" + arguments.trim() + "\"");
            throw new InternTrackrException("The application index must be a number.");
        }
    }
}
