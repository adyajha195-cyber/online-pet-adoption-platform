package backend;

import database.UserDAO;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import model.Status;
import model.User;

public class UserServiceImpl implements UserService {

    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

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

        validateCommonFields(name, email, role);

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        try {
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
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException(
                    "An account with this email already exists.", e
            );
        }
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
            throw new IllegalArgumentException("Invalid user ID.");
        }

        validateCommonFields(name, email, role);

        try {
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
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException(
                    "Another account already uses this email.", e
            );
        }
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        try {
            return userDAO.deleteUser(userId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException(
                    "This user still has pets, applications or messages "
                            + "and cannot be deleted.", e
            );
        }
    }

    // Shared validation for addUser / updateUser
    private void validateCommonFields(String name, String email, String role) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (!email.trim().matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException(
                    "Please enter a valid email address."
            );
        }

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }

        // Admin / Shelter / Adopter only (matches the DB CHECK constraint)
        Status.require(Status.ROLES, role.trim(), "role");
    }
}
