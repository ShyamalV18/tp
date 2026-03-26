package seedu.interntrackr.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

/**
 * Tests for {@link DeadlineCommand}.
 */
public class DeadlineCommandTest {

    @TempDir
    Path tempDir;

    private ApplicationList applications;
    private CapturingUi ui;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        applications = new ApplicationList();
        applications.addApplication(new Application("Google", "SWE Intern"));
        applications.addApplication(new Application("Meta", "Product Intern"));

        ui = new CapturingUi();
        storage = new Storage(tempDir.resolve("interntrackr.txt").toString());
    }

    @Test
    public void constructor_indexLessThanOne_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeadlineCommand(0, "OA", LocalDate.of(2026, 3, 15)));
    }

    @Test
    public void execute_indexGreaterThanApplicationListSize_throwsInternTrackrException() {
        DeadlineCommand command = new DeadlineCommand(3, "OA", LocalDate.of(2026, 3, 15));

        InternTrackrException exception = assertThrows(InternTrackrException.class,
                () -> command.execute(applications, ui, storage));

        assertEquals("Invalid application index.", exception.getMessage());
        assertNull(applications.getApplication(1).getDeadline());
        assertNull(applications.getApplication(2).getDeadline());
        assertNull(ui.lastMessage);
    }

    @Test
    public void constructor_nullDeadlineType_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new DeadlineCommand(1, null, LocalDate.of(2026, 3, 15)));
    }

    @Test
    public void constructor_blankDeadlineType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeadlineCommand(1, "   ", LocalDate.of(2026, 3, 15)));
    }

    @Test
    public void constructor_nullDueDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new DeadlineCommand(1, "OA", null));
    }

    @Test
    public void execute_validIndex_setsDeadlineOnSpecifiedApplication() {
        DeadlineCommand command = new DeadlineCommand(1, "OA", LocalDate.of(2026, 3, 15));

        command.execute(applications, ui, storage);

        assertEquals(
                "Deadline Type: OA | Due Date: 2026-03-15 | Done: [ ]",
                applications.getApplication(1).getDeadline().toString());
    }

    @Test
    public void execute_validIndex_doesNotModifyOtherApplications() {
        DeadlineCommand command = new DeadlineCommand(1, "OA", LocalDate.of(2026, 3, 15));

        command.execute(applications, ui, storage);

        assertNull(applications.getApplication(2).getDeadline());
    }

    @Test
    public void execute_validIndex_showsCorrectSuccessMessage() {
        DeadlineCommand command = new DeadlineCommand(1, "OA", LocalDate.of(2026, 3, 15));

        command.execute(applications, ui, storage);

        assertEquals(
                "Deadline updated! Google's SWE Intern's OA due date is now on the [2026-03-15]",
                ui.lastMessage);
    }

    @Test
    public void execute_validIndex_savesUpdatedApplicationsToStorage() throws Exception {
        DeadlineCommand command = new DeadlineCommand(1, "OA", LocalDate.of(2026, 3, 15));

        command.execute(applications, ui, storage);

        List<String> lines = Files.readAllLines(tempDir.resolve("interntrackr.txt"));

        assertEquals(2, lines.size());
        assertEquals(
                "Google | SWE Intern | Applied | OA | 2026-03-15 | false",
                lines.get(0));
        assertEquals(
                "Meta | Product Intern | Applied",
                lines.get(1));
    }

    @Test
    public void execute_nullApplicationList_throwsAssertionError() {
        DeadlineCommand command = new DeadlineCommand(1, "OA", LocalDate.of(2026, 3, 15));

        assertThrows(AssertionError.class, () -> command.execute(null, ui, storage));
    }

    @Test
    public void execute_nullUi_throwsAssertionError() {
        DeadlineCommand command = new DeadlineCommand(1, "OA", LocalDate.of(2026, 3, 15));

        assertThrows(AssertionError.class, () -> command.execute(applications, null, storage));
    }

    @Test
    public void execute_nullStorage_throwsAssertionError() {
        DeadlineCommand command = new DeadlineCommand(1, "OA", LocalDate.of(2026, 3, 15));

        assertThrows(AssertionError.class, () -> command.execute(applications, ui, null));
    }

    /**
     * Test double for capturing UI messages.
     */
    private static class CapturingUi extends Ui {
        private String lastMessage;

        @Override
        public void showMessage(String message) {
            this.lastMessage = message;
        }
    }
}
