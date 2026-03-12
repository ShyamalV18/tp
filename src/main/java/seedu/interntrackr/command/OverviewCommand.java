package seedu.interntrackr.command;

import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Displays a summary of all tracked internship applications.
 */
public class OverviewCommand extends Command {

    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        ui.showMessage("Overview:");
        ui.showMessage("You are currently tracking " + applications.getSize() + " applications.");
        ui.showMessage("Keep up the momentum!");
    }
}
