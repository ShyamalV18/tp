package seedu.interntrackr.parser;

import org.junit.jupiter.api.Test;

import seedu.interntrackr.command.DeadlineDeleteCommand;
import seedu.interntrackr.exception.InternTrackrException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineDeleteCommandParserTest {

    private static final String DEADLINE_DELETE_USAGE =
            "Invalid format. Usage: deadline delete INDEX i/DEADLINE_INDEX";

    @Test
    public void parse_validArguments_returnsDeadlineDeleteCommand() throws InternTrackrException {
        assertInstanceOf(DeadlineDeleteCommand.class,
                DeadlineCommandParser.parse("delete 2 i/1"));
    }

    @Test
    public void parse_missingIndices_throwsInternTrackrExceptionWithDeleteUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("delete i/"));

        assertEquals(DEADLINE_DELETE_USAGE, exception.getMessage());
    }

    @Test
    public void parse_nonNumericIndices_throwsInternTrackrExceptionWithNumericMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("delete a i/b"));

        assertEquals("The application index and deadline index must be valid positive numbers.",
                exception.getMessage());
    }
}
