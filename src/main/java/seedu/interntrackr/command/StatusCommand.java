package seedu.interntrackr.command;

import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Updates the status of an internship application.
 */
public class StatusCommand extends Command {
    private int index;
    private String status;

    public StatusCommand(int index, String status) {
        this.index = index;
        this.status = status;
    }

    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        if (index < 1 || index > applications.getSize()) {
            throw new InternTrackrException("Invalid application index.");
        }

        // ApplicationList.getApplication() already handles the 0-based index conversion
        Application app = applications.getApplication(index);
        app.setStatus(this.status);

        ui.showMessage("Status updated! " + app.getCompany() + " is now marked as [" + this.status + "]");

        storage.save(applications.getApplications());
    }
}
