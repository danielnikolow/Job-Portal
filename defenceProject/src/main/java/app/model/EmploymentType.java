package app.model;

public enum EmploymentType {
    FULL_TIME("Full time"),
    PART_TIME("Part time"),
    CONTRACT("Contract"),
    INTERNSHIP("Internship"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    private final String displayName;

    EmploymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
