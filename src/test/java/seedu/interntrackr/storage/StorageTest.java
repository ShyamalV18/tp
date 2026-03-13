package seedu.interntrackr.storage;

import org.junit.jupiter.api.Test;
import seedu.interntrackr.exception.InternTrackrException;
import seedu.interntrackr.model.Application;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageTest {

    /**
     * Returns a temporary file path for testing purposes.
     *
     * @return A string file path in the system temp directory.
     */
    private String getTempFilePath() {
        return System.getProperty("java.io.tmpdir") + "/test_interntrackr.txt";
    }

    @Test
    void saveAndLoad_validData_success() throws InternTrackrException {
        Storage storage = new Storage(getTempFilePath());

        ArrayList<Application> toSave = new ArrayList<>();
        toSave.add(new Application("Shopee", "Backend Intern", "Applied"));
        toSave.add(new Application("Google", "SWE Intern", "Interview"));

        storage.save(toSave);
        ArrayList<Application> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("Shopee", loaded.get(0).getCompany());
        assertEquals("Backend Intern", loaded.get(0).getRole());
        assertEquals("Applied", loaded.get(0).getStatus());
        assertEquals("Google", loaded.get(1).getCompany());
        assertEquals("Interview", loaded.get(1).getStatus());
    }

    @Test
    void load_nonExistentFile_returnsEmptyList() throws InternTrackrException {
        Storage storage = new Storage("data/nonexistent_xyz_file.txt");
        ArrayList<Application> result = storage.load();
        assertTrue(result.isEmpty());
    }

    @Test
    void saveAndLoad_emptyList_returnsEmptyList() throws InternTrackrException {
        Storage storage = new Storage(getTempFilePath());

        storage.save(new ArrayList<>());
        ArrayList<Application> loaded = storage.load();

        assertTrue(loaded.isEmpty());
    }

    @Test
    void load_corruptedData_throwsInternTrackrException() throws InternTrackrException {
        // Write a corrupted line directly to the temp file
        String path = System.getProperty("java.io.tmpdir") + "/corrupted_test.txt";
        Storage storage = new Storage(path);

        // Save a valid app first then manually corrupt the file
        ArrayList<Application> bad = new ArrayList<>();
        bad.add(new Application("OnlyCompany", "missingStatus"));
        storage.save(bad);

        // Now overwrite file with corrupted content
        try {
            java.io.FileWriter fw = new java.io.FileWriter(path);
            fw.write("corruptedlinewithoutseparators\n");
            fw.close();
        } catch (java.io.IOException e) {
            // ignore for test setup
        }

        assertThrows(InternTrackrException.class, () -> storage.load());
    }

    @Test
    void save_createsFileIfNotExists() throws InternTrackrException {
        String path = System.getProperty("java.io.tmpdir") + "/newfile_test.txt";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        Storage storage = new Storage(path);
        ArrayList<Application> list = new ArrayList<>();
        list.add(new Application("Meta", "Data Intern", "Applied"));
        storage.save(list);

        assertTrue(file.exists());
    }
}
