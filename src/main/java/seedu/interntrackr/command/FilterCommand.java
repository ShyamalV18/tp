package seedu.interntrackr.command;

import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;
import seedu.interntrackr.exception.InternTrackrException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Filters applications based on status.
 */
public class FilterCommand extends Command {
    private static final Logger logger = Logger.getLogger(FilterCommand.class.getName());
    private final String status;
    private final boolean isClear;

    public FilterCommand(String status) {
        this.status = (status == null) ? "" : status.trim();
        this.isClear = false;
    }

    public FilterCommand(boolean isClear) {
        this.status = "";
        this.isClear = isClear;
    }

    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        assert applications != null : "ApplicationList cannot be null during execution";
        assert ui != null : "Ui cannot be null during execution";

        logger.log(Level.INFO, "Executing FilterCommand. isClear: " + isClear + ", status: " + status);

        if (isClear) {
            handleClearFilter(applications, ui);
            return;
        }

        if (this.status.isEmpty()) {
            logger.log(Level.WARNING, "Filter attempt with empty status.");
            throw new InternTrackrException("Filter status cannot be empty. Use 'filter clear' to reset.");
        }

        if (!Application.isValidStatus(this.status)) {
            logger.log(Level.WARNING, "Invalid filter status: " + this.status);
            throw new InternTrackrException("Invalid status assigned. Please use one of the following:\n"
                    + "Applied\n"
                    + "Pending\n"
                    + "Interview\n"
                    + "Offered\n"
                    + "Rejected\n"
                    + "Accepted");
        }

        String searchStatus = Application.getNormalizedStatus(this.status);
        ui.showMessage("Here are the applications with status: " + searchStatus);
        int matchCount = 0;

        for (int i = 1; i <= applications.getSize(); i++) {
            Application app = applications.getApplication(i);
            if (app != null && app.getStatus().equals(searchStatus)) {
                ui.showMessage(i + ". " + app.toString());
                matchCount++;
            }
        }

        if (matchCount == 0) {
            ui.showMessage("No applications found with status: " + searchStatus);
        }
    }

    private void handleClearFilter(ApplicationList applications, Ui ui) {
        ui.showMessage("Filter cleared. Showing all applications:");
        for (int i = 1; i <= applications.getSize(); i++) {
            Application app = applications.getApplication(i);
            if (app != null) {
                ui.showMessage(i + ". " + app.toString());
            }
        }
    }
}
