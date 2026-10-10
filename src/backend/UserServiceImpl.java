package backend;

import database.UserDAO;
import model.User;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAO();
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    @Override
    public User getUserById(int userId) throws SQLException {

        if (userId <= 0) {
            return null;
        }

        return userDAO.getUserById(userId);
    }

    @Override
    public int addUser(
            String name,
            String email,
            String password,
            String role,
            String contact,
            String address,
            String city,
            String state
    ) throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Name cannot be empty."
            );
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Email cannot be empty."
            );
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                "Please enter a valid email address."
            );
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                "Password cannot be empty."
            );
        }

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Role cannot be empty."
            );
        }

        return userDAO.addUser(
            name.trim(),
            email.trim(),
            password,
            role.trim(),
            contact,
            address,
            city,
            state
        );
    }

    @Override
    public boolean updateUser(
            int userId,
            String name,
            String email,
            String role,
            String contact,
            String address,
            String city,
            String state
    ) throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                "Invalid user ID."
            );
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Name cannot be empty."
            );
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Email cannot be empty."
            );
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                "Please enter a valid email address."
            );
        }

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Role cannot be empty."
            );
        }

        return userDAO.updateUser(
            userId,
            name.trim(),
            email.trim(),
            role.trim(),
            contact,
            address,
            city,
            state
        );
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                "Invalid user ID."
            );
        }

        return userDAO.deleteUser(userId);
    }
}