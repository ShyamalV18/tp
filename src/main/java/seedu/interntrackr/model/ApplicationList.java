package seedu.interntrackr.model;

import java.util.ArrayList;

/**
 * Manages the in-memory list of internship applications.
 */
public class ApplicationList {
    private ArrayList<Application> applications;

    /**
     * Constructs an empty ApplicationList.
     */
    public ApplicationList() {
        this.applications = new ArrayList<>();
    }

    /**
     * Constructs an ApplicationList from an existing list.
     *
     * @param applications The pre-loaded list of applications.
     */
    public ApplicationList(ArrayList<Application> applications) {
        this.applications = applications;
    }

    /**
     * Adds a new application to the list.
     *
     * @param application The application to add.
     */
    public void addApplication(Application application) {
        applications.add(application);
    }

    /**
     * Deletes the application at the given 1-based index.
     *
     * @param index The 1-based index of the application to remove.
     */
    public void deleteApplication(int index) {
        applications.remove(index - 1);
    }

    /**
     * Returns the application at the given 1-based index.
     *
     * @param index The 1-based index.
     * @return The Application object.
     */
    public Application getApplication(int index) {
        return applications.get(index - 1);
    }

    /**
     * Returns the number of applications in the list.
     *
     * @return The size of the list.
     */
    public int getSize() {
        return applications.size();
    }

    /**
     * Returns all applications as an ArrayList.
     *
     * @return The full list of applications.
     */
    public ArrayList<Application> getApplications() {
        return applications;
    }
}
