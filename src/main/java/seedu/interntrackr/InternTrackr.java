package seedu.interntrackr;

import seedu.interntrackr.command.Command;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.parser.Parser;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;
import seedu.interntrackr.exception.InternTrackrException;

/**
 * Initializes and runs the InternTrackr application.
 */
public class InternTrackr {
    private Storage storage;
    private ApplicationList applications;
    private Ui ui;

    public InternTrackr(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            applications = new ApplicationList(storage.load());
        } catch (InternTrackrException e) {
            ui.showLoadingError();
            applications = new ApplicationList();
        }
    }

    /**
     * Executes the main application loop until the user inputs the exit command.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // Prints a divider before the output
                Command c = Parser.parse(fullCommand);
                c.execute(applications, ui, storage);
                isExit = c.isExit();
            } catch (InternTrackrException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine(); // Prints a divider after the output
            }
        }
    }

    public static void main(String[] args) {
        new InternTrackr("data/interntrackr.txt").run();
    }
}
