package backend;

import model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    // Retrieve all users
    List<User> getAllUsers() throws SQLException;

    // Retrieve a user by ID
    User getUserById(int userId) throws SQLException;

    // Register a new user
    int addUser(
            String name,
            String email,
            String password,
            String role,
            String contact,
            String address,
            String city,
            String state
    ) throws SQLException;

    // Update existing user details
    boolean updateUser(
            int userId,
            String name,
            String email,
            String role,
            String contact,
            String address,
            String city,
            String state
    ) throws SQLException;

    // Delete a user
    boolean deleteUser(int userId) throws SQLException;
}ṇ