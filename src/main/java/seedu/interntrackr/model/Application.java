package seedu.interntrackr.model;

/**
 * Represents a single internship application.
 */
public class Application {
    private String company;
    private String role;
    private String status;

    /**
     * Constructs an Application with the given company and role.
     *
     * @param company The name of the company.
     * @param role    The role applied for.
     */
    public Application(String company, String role) {
        this.company = company;
        this.role = role;
        this.status = "Applied";
    }

    /**
     * Constructs an Application with the given company, role, and status.
     *
     * @param company The name of the company.
     * @param role    The role applied for.
     * @param status  The current application status.
     */
    public Application(String company, String role, String status) {
        this.company = company;
        this.role = role;
        this.status = status;
    }

    /**
     * Returns the company name of this application.
     *
     * @return The company name.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Returns the role of this application.
     *
     * @return The role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns the status of this application.
     *
     * @return The status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of this application.
     *
     * @param status The new status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a formatted string representation of this application.
     *
     * @return A human-readable string.
     */
    @Override
    public String toString() {
        return "Company: " + company + " | Role: " + role + " | Status: " + status;
    }

    /**
     * Returns a pipe-delimited string for saving to the storage file.
     *
     * @return A storage-formatted string.
     */
    public String toStorageString() {
        return company + " | " + role + " | " + status;
    }
}
