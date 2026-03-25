package seedu.interntrackr.command;

import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;
import seedu.interntrackr.exception.InternTrackrException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Updates the status of an internship application.
 */
public class StatusCommand extends Command {
    private static final Logger logger = Logger.getLogger(StatusCommand.class.getName());
    private final int index;
    private final String status;

    public StatusCommand(int index, String status) {
        this.index = index;
        this.status = (status == null) ? "" : status.trim();
    }

    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        assert applications != null : "ApplicationList dependency is missing";
        assert ui != null : "Ui dependency is missing";
        assert storage != null : "Storage dependency is missing";

        logger.log(Level.INFO, "Executing StatusCommand for index " + index + " with status: " + status);

        if (index < 1 || index > applications.getSize()) {
            logger.log(Level.WARNING, "Status update failed: index " + index + " out of bounds.");
            throw new InternTrackrException("Invalid application index. Please provide a number between 1 and "
                    + applications.getSize());
        }

        if (this.status.isEmpty()) {
            logger.log(Level.WARNING, "Attempted to set an empty status.");
            throw new InternTrackrException("The new status cannot be empty.");
        }

        if (!Application.isValidStatus(this.status)) {
            logger.log(Level.WARNING, "Invalid status assigned: " + this.status);
            throw new InternTrackrException("Invalid status assigned. Please use one of the following:\n"
                    + "Applied\n"
                    + "Pending\n"
                    + "Interview\n"
                    + "Offered\n"
                    + "Rejected\n"
                    + "Accepted");
        }

        String normalizedStatus = Application.getNormalizedStatus(this.status);
        Application app = applications.getApplication(index);

        if (app == null) {
            logger.log(Level.SEVERE, "Retrieved null application at valid index " + index);
            throw new InternTrackrException("Critical Error: The application at index " + index + " is missing.");
        }

        app.setStatus(normalizedStatus);
        ui.showMessage("Status updated! " + app.getCompany() + " is now marked as [" + normalizedStatus + "]");

        try {
            storage.save(applications.getApplications());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save data after status update.", e);
            throw new InternTrackrException("Status updated locally, but failed to save to disk: " + e.getMessage());
        }
    }
}
