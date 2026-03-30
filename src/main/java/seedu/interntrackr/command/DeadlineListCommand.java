package seedu.interntrackr.command;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.model.DeadlineList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

/**
 * Lists deadlines linked to a specific application.
 */
public class DeadlineListCommand extends Command {

    private final int index;

    public DeadlineListCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the deadline list command and displays deadlines for the specified application.
     *
     * @param applications           The list of internship applications.
     * @param ui                     The user interface for output.
     * @param storage                The storage handler.
     * @throws InternTrackrException If an error occurs during execution.
     */
    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {

        Application app = applications.getApplication(index);
        DeadlineList deadlines = app.getDeadlines();

        if (deadlines.getSize() == 0) {
            ui.showMessage("No deadlines found for this application.");
            return;
        }

        ui.showMessage("Here are the deadlines for this application:");

        for (int i = 0; i < deadlines.getSize(); i++) {
            ui.showMessage((i + 1) + ". " + deadlines.getDeadlines().get(i).toString());
        }
    }
}