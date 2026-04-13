//@@author eugenia-cnl-lee
package seedu.interntrackr.parser;

import org.junit.jupiter.api.Test;

import seedu.interntrackr.command.DeadlineDoneCommand;
import seedu.interntrackr.exception.InternTrackrException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineDoneCommandParserTest {

    private static final String COMBINED_USAGE =
            "Invalid command format!\n"
                    + "Available deadline commands:\n"
                    + "  deadline add INDEX t/TYPE d/DD-MM-YYYY\n"
                    + "  deadline list INDEX\n"
                    + "  deadline done INDEX i/DEADLINE_INDEX\n"
                    + "  deadline undone INDEX i/DEADLINE_INDEX\n"
                    + "  deadline delete INDEX i/DEADLINE_INDEX";

    private static final String DEADLINE_DONE_USAGE =
            "Invalid format. Usage: deadline done INDEX i/DEADLINE_INDEX";

    @Test
    public void parse_validArguments_returnsDeadlineDoneCommand() throws InternTrackrException {
        assertInstanceOf(DeadlineDoneCommand.class,
                DeadlineCommandParser.parse("done 1 i/1"));
    }

    @Test
    public void parse_validArgumentsWithExtraSpacing_returnsDeadlineDoneCommand() throws InternTrackrException {
        assertInstanceOf(DeadlineDoneCommand.class,
                DeadlineCommandParser.parse("done   2 i/3"));
    }

    @Test
    public void parse_blankArguments_throwsInternTrackrExceptionWithCombinedUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse(""));

        assertEquals(COMBINED_USAGE, exception.getMessage());
    }

    @Test
    public void parse_missingDoneSubcommand_throwsInternTrackrExceptionWithCombinedUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("1 i/1"));

        assertEquals(COMBINED_USAGE, exception.getMessage());
    }

    @Test
    public void parse_unknownSubcommand_throwsInternTrackrExceptionWithCombinedUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("mark 1 i/1"));

        assertEquals(COMBINED_USAGE, exception.getMessage());
    }

    @Test
    public void parse_missingDeadlineIndexPrefix_throwsInternTrackrExceptionWithDoneUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("done 1 1"));

        assertEquals(DEADLINE_DONE_USAGE, exception.getMessage());
    }

    @Test
    public void parse_missingApplicationIndex_throwsInternTrackrExceptionWithDoneUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("done i/1"));

        assertEquals(DEADLINE_DONE_USAGE, exception.getMessage());
    }

    @Test
    public void parse_missingDeadlineIndex_throwsInternTrackrExceptionWithDoneUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("done 1 i/"));

        assertEquals(DEADLINE_DONE_USAGE, exception.getMessage());
    }

    @Test
    public void parse_blankDoneArguments_throwsInternTrackrExceptionWithDoneUsageMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("done"));

        assertEquals(DEADLINE_DONE_USAGE, exception.getMessage());
    }

    @Test
    public void parse_nonNumericApplicationIndex_throwsInternTrackrExceptionWithNumericMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("done one i/1"));

        assertEquals("The application index and deadline index must be valid positive numbers.",
                exception.getMessage());
    }

    @Test
    public void parse_nonNumericDeadlineIndex_throwsInternTrackrExceptionWithNumericMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("done 1 i/one"));

        assertEquals("The application index and deadline index must be valid positive numbers.",
                exception.getMessage());
    }

    @Test
    public void parse_nonNumericBothIndices_throwsInternTrackrExceptionWithNumericMessage() {
        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("done one i/two"));

        assertEquals("The application index and deadline index must be valid positive numbers.",
                exception.getMessage());
    }
}
