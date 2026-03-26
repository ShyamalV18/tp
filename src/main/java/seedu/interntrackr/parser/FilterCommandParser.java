package seedu.interntrackr.parser;

import java.util.logging.Logger;

import seedu.interntrackr.command.FilterCommand;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Parses user input arguments for the filter command.
 */
public class FilterCommandParser {
    private static final Logger logger = Logger.getLogger(FilterCommandParser.class.getName());

    private static final String PREFIX_FILTER_STATUS = "s/";

    /**
     * Parses the given arguments and returns a FilterCommand.
     *
     * @param arguments The argument string following the "filter" keyword.
     * @return A new FilterCommand configured for either clearing or filtering by status.
     * @throws InternTrackrException If the arguments are empty or the format is invalid.
     */
    public static FilterCommand parse(String arguments) throws InternTrackrException {
        if (arguments.isEmpty()) {
            logger.warning("Filter command missing arguments.");
            throw new InternTrackrException("Invalid format. Usage: filter s/STATUS or filter clear");
        }
        if (arguments.equalsIgnoreCase("clear")) {
            logger.fine("Parsed: FilterCommand (clear)");
            return new FilterCommand(true);
        }
        if (!arguments.startsWith(PREFIX_FILTER_STATUS)) {
            logger.warning("Filter command missing s/ prefix.");
            throw new InternTrackrException("Invalid format. Usage: filter s/STATUS");
        }
        String filterStatus = arguments.substring(PREFIX_FILTER_STATUS.length()).replace("\"", "").trim();
        logger.fine("Parsed: FilterCommand status=" + filterStatus);
        return new FilterCommand(filterStatus);
    }
}
