package seedu.interntrackr;

import seedu.interntrackr.command.Command;
import seedu.interntrackr.model.ApplicationList;
import seedu.interntrackr.parser.Parser;
import seedu.interntrackr.storage.Storage;
import seedu.interntrackr.ui.Ui;
import seedu.interntrackr.exception.InternTrackrException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the InternTrackr application.
 * Wires together the UI, storage, and application list,
 * then drives the read-parse-execute loop until the user exits.
 */
public class InternTrackr {
    private static final Logger logger = Logger.getLogger(InternTrackr.class.getName());

    private Storage storage;
    private ApplicationList applications;
    private Ui ui;

    /**
     * Constructs an InternTrackr instance and loads existing data from the given file path.
     * If the file does not exist or contains corrupted data, a fresh empty list is used instead.
     *
     * @param filePath Path to the persistent data file; must not be null or blank.
     */
    public InternTrackr(String filePath) {
        assert filePath != null : "filePath must not be null";
        assert !filePath.isBlank() : "filePath must not be blank";

        logger.info("Initializing InternTrackr with file path: " + filePath);

        ui = new Ui();
        storage = new Storage(filePath);

        try {
            applications = new ApplicationList(storage.load());
            logger.info("Successfully loaded application data from storage.");
        } catch (InternTrackrException e) {
            logger.warning("Failed to load data from storage: " + e.getMessage());
            ui.showLoadingError();
            applications = new ApplicationList();
        }

        assert ui != null : "Ui must be initialized";
        assert storage != null : "Storage must be initialized";
        assert applications != null : "ApplicationList must be initialized";
    }

    /**
     * Starts the main application loop, repeatedly reading and executing user commands
     * until an {@link seedu.interntrackr.command.ExitCommand} is issued.
     * Handles both expected {@link InternTrackrException} errors and unexpected runtime errors
     * by displaying an error message and continuing the loop.
     */
    public void run() {
        assert ui != null : "Ui must not be null before run()";
        assert applications != null : "ApplicationList must not be null before run()";
        assert storage != null : "Storage must not be null before run()";

        logger.info("Starting InternTrackr main loop.");
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();

                assert fullCommand != null : "readCommand() must not return null";
                logger.fine("User input received: " + fullCommand);

                ui.showLine();
                Command c = Parser.parse(fullCommand);

                assert c != null : "Parser.parse() must not return null";
                logger.fine("Parsed command: " + c.getClass().getSimpleName());

                c.execute(applications, ui, storage);
                isExit = c.isExit();

                logger.fine("Command executed. isExit=" + isExit);
            } catch (InternTrackrException e) {
                logger.warning("InternTrackrException caught in main loop: " + e.getMessage());
                ui.showError(e.getMessage());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error in main loop: " + e.getMessage(), e);
                ui.showError("An unexpected error occurred: " + e.getMessage());
            } finally {
                ui.showLine();
            }
        }

        logger.info("InternTrackr exiting cleanly.");
    }

    /**
     * Entry point for the InternTrackr application.
     * Creates an instance with the default data file path and starts the main loop.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        logger.info("Application starting.");
        new InternTrackr("data/interntrackr.txt").run();
    }
}
