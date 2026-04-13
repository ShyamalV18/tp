package seedu.interntrackr.parser;

import org.junit.jupiter.api.Test;

import seedu.interntrackr.command.DeadlineUndoneCommand;
import seedu.interntrackr.exception.InternTrackrException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineUndoneCommandParserTest {

    private static final String DEADLINE_UNDONE_USAGE =
            "Invalid format. Usage: deadline undone INDEX i/DEADLINE_INDEX";

    @Test
    public void parse_validArguments_returnsDeadlineUndoneCommand() throws InternTrackrException {
        assertInstanceOf(DeadlineUndoneCommand.class,
                DeadlineCommandParser.parse("undone 1 i/2"));
    }

    @Test
    public void parse_missingIndices_throwsInternTrackrExceptionWithUndoneUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("undone i/"));

        assertEquals(DEADLINE_UNDONE_USAGE, exception.getMessage());
    }

    @Test
    public void parse_nonNumericIndices_throwsInternTrackrExceptionWithNumericMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("undone one i/two"));

        assertEquals("The application index and deadline index must be valid positive numbers.",
                exception.getMessage());
    }
}
