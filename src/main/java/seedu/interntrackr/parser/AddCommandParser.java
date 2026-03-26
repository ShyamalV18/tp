package seedu.interntrackr.parser;

import java.util.logging.Logger;

import seedu.interntrackr.command.AddCommand;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Parses user input arguments for the add command.
 */
public class AddCommandParser {
    private static final Logger logger = Logger.getLogger(AddCommandParser.class.getName());

    private static final String PREFIX_COMPANY = "c/";
    private static final String PREFIX_ROLE = "r/";

    /**
     * Parses the given arguments and returns an AddCommand.
     *
     * @param arguments The argument string following the "add" keyword.
     * @return A new AddCommand with the parsed company and role.
     * @throws InternTrackrException If the format is invalid or company/role values are empty.
     */
    public static AddCommand parse(String arguments) throws InternTrackrException {
        assert arguments != null : "arguments must not be null";

        if (!arguments.contains(PREFIX_COMPANY) || !arguments.contains(PREFIX_ROLE)) {
            logger.warning("Add command missing c/ or r/ parameter.");
            throw new InternTrackrException("Invalid format. Usage: add c/COMPANY r/ROLE");
        }

        logger.fine("Parsing add command args: \"" + arguments + "\"");

        String company;
        String role;

        try {
            int cIndex = arguments.indexOf(PREFIX_COMPANY);
            int rIndex = arguments.indexOf(PREFIX_ROLE);

            if (cIndex < rIndex) {
                company = arguments.substring(cIndex + PREFIX_COMPANY.length(), rIndex).trim().replace("\"", "");
                role = arguments.substring(rIndex + PREFIX_ROLE.length()).trim().replace("\"", "");
            } else {
                role = arguments.substring(rIndex + PREFIX_ROLE.length(), cIndex).trim().replace("\"", "");
                company = arguments.substring(cIndex + PREFIX_COMPANY.length()).trim().replace("\"", "");
            }

            if (company.isEmpty() || role.isEmpty()) {
                logger.warning("Add command has blank company or role after parsing.");
                throw new InternTrackrException("Company and role cannot be empty.");
            }

            logger.fine("Parsed AddCommand: company=\"" + company + "\", role=\"" + role + "\"");
            return new AddCommand(company, role);
        } catch (InternTrackrException e) {
            throw e;
        } catch (Exception e) {
            logger.warning("Unexpected error parsing add command: " + e.getMessage());
            throw new InternTrackrException("Error parsing add command.");
        }
    }
}
