//@@author eugenia-cnl-lee
package seedu.interntrackr.parser;

import org.junit.jupiter.api.Test;

import seedu.interntrackr.command.DeadlineAddCommand;
import seedu.interntrackr.command.DeadlineListCommand;
import seedu.interntrackr.exception.InternTrackrException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeadlineAddCommandParserTest {

    @Test
    public void parse_validArguments_returnsDeadlineCommand() throws InternTrackrException {
        assertTrue(DeadlineCommandParser.parse("add 1 t/Technical d/01-06-2026") instanceof DeadlineAddCommand);
    }

    @Test
    public void parse_extraArgumentsInDate_throwsInternTrackrException() {
        assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("add 1 t/Interview d/15-07-2026 n/Prepare for coding round"));
    }

    @Test
    public void parse_validListArguments_returnsDeadlineListCommand() throws InternTrackrException {
        assertTrue(DeadlineCommandParser.parse("list 1") instanceof DeadlineListCommand);
    }

    @Test
    public void parse_missingAddSubcommand_throwsInternTrackrException() {
        assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("1 t/Technical d/01-06-2026"));
    }

    @Test
    public void parse_blankArguments_throwsInternTrackrException() {
        assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse(""));
    }

    @Test
    public void parse_invalidDateFormat_throwsInternTrackrException() {
        assertThrows(InternTrackrException.class,
                () -> DeadlineCommandParser.parse("add 1 t/Technical d/2026-06-01"));
    }
}
