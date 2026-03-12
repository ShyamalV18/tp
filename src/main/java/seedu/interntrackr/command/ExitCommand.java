package seedu.interntrackr.command;

import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

/**
 * Exits the application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) {
        ui.showMessage("Bye! Good luck with your internship hunt.");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
