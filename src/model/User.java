package model;

/** Plain data object for one row of the User table. Immutable. */
public class User {

    private final int userId;
    private final String name;
    private final String email;
    private final String role;
    private final String contact;
    private final String address;
    private final String city;
    private final String state;

    public User(int userId, String name, String email, String role, String contact, String address, String city, String state) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.contact = contact;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }

    @Override
    public String toString() {
        return userId + " | " + name + " | " + email + " | " + role + " | " + contact + " | " + address + " | " + city + " | " + state;
    }
}
