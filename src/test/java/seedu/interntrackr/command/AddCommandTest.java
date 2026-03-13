package seedu.interntrackr.command;

import org.junit.jupiter.api.Test;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.ui.Ui;
import seedu.interntrackr.storage.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCommandTest {

    @Test
    public void execute_validApplication_addedToList() {
        ApplicationList applications = new ApplicationList();
        AddCommand command = new AddCommand("Google", "Software Engineer");

        // Initialize a dummy storage for testing
        Storage storage = new Storage("data/test_add.txt");

        // Pass the storage object instead of null
        command.execute(applications, new Ui(), storage);

        assertEquals(1, applications.getSize());
        assertEquals("Google", applications.getApplication(1).getCompany());
    }
}
