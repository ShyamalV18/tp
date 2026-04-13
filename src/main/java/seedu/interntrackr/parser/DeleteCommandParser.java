package seedu.interntrackr.parser;

import java.util.logging.Logger;

import seedu.interntrackr.command.DeleteCommand;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Parses user input arguments for the delete command.
 * Supports:
 *   delete INDEX             — delete an active application
 *   delete archive INDEX     — delete an archived application directly
 */
public class DeleteCommandParser {
    private static final Logger logger = Logger.getLogger(DeleteCommandParser.class.getName());

    /**
     * Parses the given arguments and returns a DeleteCommand.
     *
     * <p>Accepts either {@code delete INDEX} for active applications or
     * {@code delete archive INDEX} for archived applications.</p>
     *
     * @param arguments The argument string following the "delete" keyword.
     * @return A new DeleteCommand with the parsed index and archive flag.
     * @throws InternTrackrException If the index is missing, non-numeric, non-positive, or has trailing text.
     */
    public static DeleteCommand parse(String arguments) throws InternTrackrException {
        if (arguments.isEmpty()) {
            logger.warning("Delete command missing index.");
            throw new InternTrackrException("Invalid format. Usage: delete INDEX  OR  delete archive INDEX");
        }

        boolean isArchived = false;
        String indexPart = arguments.trim();

        if (indexPart.toLowerCase().startsWith("archive")) {
            isArchived = true;
            indexPart = indexPart.substring("archive".length()).trim();
            if (indexPart.isEmpty()) {
                logger.warning("Delete archive command missing index.");
                throw new InternTrackrException("Invalid format. Usage: delete archive INDEX");
            }
        }

        if (indexPart.contains(" ")) {
            logger.warning("Delete command has trailing text: \"" + indexPart + "\"");
            String usage = isArchived
                    ? "Invalid format. Usage: delete archive INDEX"
                    : "Invalid format. Usage: delete INDEX  OR  delete archive INDEX";
            throw new InternTrackrException(usage);
        }

        try {
            int index = Integer.parseInt(indexPart);
            if (index <= 0) {
                logger.warning("Delete index is non-positive: " + index);
                throw new InternTrackrException("Index must be a positive integer.");
            }
            logger.fine("Parsed: DeleteCommand index=" + index + " archived=" + isArchived);
            return new DeleteCommand(index, isArchived);
        } catch (NumberFormatException e) {
            logger.warning("Delete index is not a number: \"" + indexPart + "\"");
            String usage = isArchived
                    ? "Invalid format. Usage: delete archive INDEX"
                    : "Invalid format. Usage: delete INDEX  OR  delete archive INDEX";
            throw new InternTrackrException(usage);
        }
    }
}
