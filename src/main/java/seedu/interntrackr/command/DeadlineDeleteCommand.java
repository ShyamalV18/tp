package seedu.interntrackr.command;

import java.util.logging.Logger;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.model.Deadline;
import seedu.interntrackr.model.DeadlineList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

/**
 * Deletes a specific deadline linked to an application.
 */
public class DeadlineDeleteCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeadlineDeleteCommand.class.getName());

    private final int applicationIndex;
    private final int deadlineIndex;

    /**
     * Constructs a DeadlineDeleteCommand for the specified application and deadline index.
     *
     * @param applicationIndex The 1-based index of the application.
     * @param deadlineIndex The 1-based index of the deadline.
     * @throws IllegalArgumentException If either index is not positive.
     */
    public DeadlineDeleteCommand(int applicationIndex, int deadlineIndex) {
        if (applicationIndex <= 0) {
            throw new IllegalArgumentException("Application index must be a positive integer.");
        }
        if (deadlineIndex <= 0) {
            throw new IllegalArgumentException("Deadline index must be a positive integer.");
        }

        this.applicationIndex = applicationIndex;
        this.deadlineIndex = deadlineIndex;

        assert this.applicationIndex > 0 : "Application index should be positive";
        assert this.deadlineIndex > 0 : "Deadline index should be positive";
    }

    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        assert applications != null : "Application list should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";

        logger.info("Executing DeadlineDeleteCommand for application index "
                + applicationIndex + " and deadline index " + deadlineIndex);

        if (applicationIndex < 1 || applicationIndex > applications.countActive()) {
            logger.warning("Invalid application index: " + applicationIndex);
            throw new InternTrackrException("Invalid application index.");
        }

        Application app = applications.getActiveApplication(applicationIndex);
        assert app != null : "Application should not be null";

        DeadlineList deadlines = app.getDeadlines();
        assert deadlines != null : "Deadline list should not be null";

        if (deadlineIndex < 1 || deadlineIndex > deadlines.getSize()) {
            logger.warning("Invalid deadline index: " + deadlineIndex
                    + " for application index " + applicationIndex);
            throw new InternTrackrException("Invalid deadline index.");
        }

        Deadline removedDeadline = deadlines.deleteDeadline(deadlineIndex);
        assert removedDeadline != null : "Removed deadline should not be null";

        ui.showMessage("Noted. I've removed this deadline:");
        ui.showMessage(removedDeadline.toString());
        ui.showMessage("Now you have " + deadlines.getSize() + " deadlines for this application.");

        storage.save(applications.getApplications());
        logger.info("Deadline deleted and saved successfully for application index "
                + applicationIndex + " and deadline index " + deadlineIndex);
    }
}
