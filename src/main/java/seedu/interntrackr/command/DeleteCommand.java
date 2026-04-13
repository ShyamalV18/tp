package seedu.interntrackr.command;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deletes an existing internship application from the tracker.
 * Supports deleting both active and archived applications.
 */
public class DeleteCommand extends Command {
    private static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());

    private final int index;
    private final boolean isArchived;

    /**
     * Creates a DeleteCommand targeting the application at the specified 1-based
     * display index (as shown in the {@code list} output).
     *
     * @param index The 1-based display index of the active application to delete.
     * @throws IllegalArgumentException If the index is not a positive integer.
     */
    public DeleteCommand(int index) {
        this(index, false);
    }

    /**
     * Creates a DeleteCommand targeting either an active or archived application
     * at the specified 1-based display index.
     *
     * @param index      The 1-based display index of the application to delete.
     * @param isArchived True to delete from the archived list; false for the active list.
     * @throws IllegalArgumentException If the index is not a positive integer.
     */
    public DeleteCommand(int index, boolean isArchived) {
        if (index <= 0) {
            throw new IllegalArgumentException("Index must be a positive integer (1-based).");
        }
        this.index = index;
        this.isArchived = isArchived;
        logger.fine("DeleteCommand created for display index: " + index + " (archived=" + isArchived + ")");
    }

    /**
     * Executes the delete command by removing the application at the stored display
     * index from either the active or archived list, then saving the result.
     *
     * @param applications The current list of applications.
     * @param ui           The UI object used to display output to the user.
     * @param storage      The storage object used to persist the updated list.
     * @throws InternTrackrException If the display index is out of range.
     */
    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        assert applications != null : "ApplicationList must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";
        assert index > 0 : "Index must be positive at execution time";

        logger.info("Executing DeleteCommand for display index: " + index + " (archived=" + isArchived + ")");

        Application appToRemove = isArchived
                ? applications.getArchivedApplication(index)
                : applications.getActiveApplication(index);

        // Find the application's position in the backing list by object identity
        List<Application> all = new ArrayList<>(applications.getApplications());
        int backingIndex = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i) == appToRemove) {
                backingIndex = i + 1; // convert to 1-based for deleteApplication()
                break;
            }
        }

        assert backingIndex > 0 : "Application must be found in the backing list";
        applications.deleteApplication(backingIndex);

        assert applications.getSize() >= 0 : "List size must be non-negative after deletion";

        ui.showMessage("Noted. I've removed this application:");
        ui.showMessage("  " + appToRemove.toString());
        ui.showMessage("Now you have " + applications.countActive() + " active application(s) in the list.");

        storage.save(applications.getApplications());
        logger.fine("DeleteCommand executed and saved. Total applications: " + applications.getSize());
    }
}
