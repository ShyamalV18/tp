package seedu.interntrackr.command;

import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;

/**
 * Lists all internship applications currently tracked.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command and prints all applications to the console.
     *
     * @param applications The list of internship applications.
     * @param ui           The user interface for output.
     * @param storage      The storage handler.
     * @throws InternTrackrException If the list cannot be displayed.
     */
    @Override
    public void execute(ApplicationList applications, Ui ui, Storage storage) throws InternTrackrException {
        if (applications.getSize() == 0) {
            System.out.println("No applications found. Start adding some!");
            return;
        }
        System.out.println("Here are your internship applications:");
        for (int i = 1; i <= applications.getSize(); i++) {
            Application app = applications.getApplication(i);
            System.out.println(i + ". " + app.toString());
        }
    }
}
