package seedu.interntrackr.command;

import org.junit.jupiter.api.Test;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineListCommandTest {

    @Test
    public void execute_invalidApplicationIndex_throwsInternTrackrException() {
        ApplicationList applications = new ApplicationList();
        Ui ui = new Ui();
        Storage storage = new Storage("test_data.txt");

        DeadlineListCommand command = new DeadlineListCommand(1);

        assertThrows(InternTrackrException.class, () -> command.execute(applications, ui, storage));
    }

    @Test
    public void execute_zeroIndex_throwsInternTrackrException() {
        ApplicationList applications = new ApplicationList();
        applications.addApplication(new Application("Google", "Software Engineer"));
        Ui ui = new Ui();
        Storage storage = new Storage("test_data.txt");

        DeadlineListCommand command = new DeadlineListCommand(0);

        assertThrows(InternTrackrException.class, () -> command.execute(applications, ui, storage));
    }
}
