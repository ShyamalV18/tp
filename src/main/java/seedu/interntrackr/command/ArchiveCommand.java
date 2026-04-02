package seedu.interntrackr.command;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

import java.util.logging.Logger;

/**
 * Archives an internship application, hiding it from the default list view.
 */
public class ArchiveCommand extends Command {
    private static final Logger logger = Logger.getLogger(ArchiveCommand.class.getName());

    private final int index;

    /**
     * Creates an ArchiveCommand targeting the application at the specified 1-based
     * display index (as shown in the {@code list} output).
     *
     * @param index The 1-based display index of the active application to archive.
     * @throws IllegalArgumentException If the index is not a positive integer.
     */
    public ArchiveCommand(int index) {
        if (index <= 0) {
            throw new IllegalArgumentException("Index must be a positive integer (1-based).");
        }
        this.index = index;
        logger.fine("ArchiveCommand created for display index: " + index);
    }

    /**
     * Executes the archive command by marking the active application at the stored
     * display index as archived.
     *
     * <p>The index is resolved against the active (non-archived) entries only,
     * so it always matches what the user sees in the {@code list} output.</p>
     *
     * @param applications The current list of applications.
     * @param ui The UI object used to display output to the user.
     * @param storage The storage object used to persist the updated list.
     * @throws InternTrackrException If the display index is out of range.
     */
    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        assert applications != null : "ApplicationList must not be null";
        assert ui != null : "Ui must not be null";
        assert storage != null : "Storage must not be null";
        assert index > 0 : "Index must be positive at execution time";

        logger.info("Executing ArchiveCommand for display index: " + index);

        // Resolve display index against active entries only — matches what the user sees in `list`
        Application app = applications.getActiveApplication(index);

        app.setArchived(true);

        ui.showMessage("Got it. I've archived this application:");
        ui.showMessage("  " + app.toString());
        ui.showMessage("It will no longer appear in the default list. Use 'list archive' to view it.");

        storage.save(applications.getApplications());
        logger.fine("ArchiveCommand executed and saved for: " + app.getCompany());
    }
}
