package seedu.interntrackr.parser;

import org.junit.jupiter.api.Test;

import seedu.interntrackr.command.DeadlineListCommand;
import seedu.interntrackr.exception.InternTrackrException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineListCommandParserTest {

    private static final String DEADLINE_LIST_USAGE =
            "Invalid format. Usage: deadline list INDEX";

    @Test
    public void parse_validArguments_returnsDeadlineListCommand() throws InternTrackrException {
        assertInstanceOf(DeadlineListCommand.class,
                DeadlineCommandParser.parse("list 1"));
    }

    @Test
    public void parse_blankArguments_throwsInternTrackrExceptionWithListUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("list "));

        assertEquals(DEADLINE_LIST_USAGE, exception.getMessage());
    }

    @Test
    public void parse_nonNumericIndex_throwsInternTrackrExceptionWithNumericMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("list a"));

        assertEquals("The application index must be a valid positive number.",
                exception.getMessage());
    }

    @Test
    public void parse_negativeIndex_throwsInternTrackrExceptionWithNumericMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("list -1"));

        assertEquals("The application index must be a positive number.",
                exception.getMessage());
    }
}
