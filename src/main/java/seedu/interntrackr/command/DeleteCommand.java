package seedu.interntrackr.command;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

import java.util.logging.Logger;

/**
 * Deletes an existing internship application from the tracker.
 */
public class DeleteCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());

    private final int index;

    /**
     * Creates a DeleteCommand targeting the application at the specified 1-based index.
     *
     * @param index The 1-based index of the application to delete.
     * @throws IllegalArgumentException If the index is not a positive integer.
     */
    public DeleteCommand(int index) {
        if (index <= 0) {
            throw new IllegalArgumentException("Index must be a positive integer (1-based).");
        }

        this.index = index;
        logger.fine("DeleteCommand created for index: " + index);
    }

    /**
     * Executes the delete command by removing the application at the stored index and saving the result.
     *
     * @param applications The current list of applications.
     * @param ui The UI object used to display output to the user.
     * @param storage The storage object used to persist the updated list.
     * @throws InternTrackrException If the index is out of range.
     */
    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        assert applications != null : "ApplicationList must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";
        assert index > 0 : "Index must be positive at execution time";

        if (index < 1 || index > applications.getSize()) {
            logger.warning("Delete index " + index + " out of range. List size: " + applications.getSize());
            throw new InternTrackrException("Invalid application index. Please provide a valid number.");
        }

        logger.info("Executing DeleteCommand for index: " + index);

        Application appToRemove = applications.getApplication(index);
        applications.deleteApplication(index);

        assert applications.getSize() >= 0 : "List size must be non-negative after deletion";

        ui.showMessage("Noted. I've removed this application:");
        ui.showMessage("  " + appToRemove.toString());
        ui.showMessage("Now you have " + applications.getSize() + " application(s) in the list.");

        storage.save(applications.getApplications());
        logger.fine("DeleteCommand executed and saved. Total applications: " + applications.getSize());
    }
}
