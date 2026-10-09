package model;

import java.util.Set;

/** The allowed values for every status/role column, so the whole team uses the same strings. */
public final class Status {

    private Status() { }

    public static final String PENDING = "Pending";
    public static final String APPROVED = "Approved";
    public static final String REJECTED = "Rejected";
    public static final String AVAILABLE = "Available";
    public static final String ADOPTED = "Adopted";
    public static final String SENT = "Sent";
    public static final String DELIVERED = "Delivered";
    public static final String READ = "Read";

    public static final String ADMIN = "Admin";
    public static final String SHELTER = "Shelter";
    public static final String ADOPTER = "Adopter";

    public static final Set<String> ROLES = Set.of(ADMIN, SHELTER, ADOPTER);
    public static final Set<String> APPLICATION = Set.of(PENDING, APPROVED, REJECTED);
    public static final Set<String> LISTING = Set.of(PENDING, APPROVED, REJECTED);
    public static final Set<String> PET = Set.of(AVAILABLE, ADOPTED);
    public static final Set<String> DELIVERY = Set.of(SENT, DELIVERED, READ);

    /** Throws IllegalArgumentException if value is not one of the allowed values. */
    public static void require(Set<String> allowed, String value, String what) {
        if (value == null || !allowed.contains(value)) {
            throw new IllegalArgumentException(
                "Invalid " + what + ": " + value + " (allowed: " + allowed + ")");
        }
    }
}
