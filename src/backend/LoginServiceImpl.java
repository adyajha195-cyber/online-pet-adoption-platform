package backend;

import database.LoginDAO;
import java.sql.SQLException;
import model.User;

public class LoginServiceImpl implements LoginService {

    private final LoginDAO loginDAO;

    public LoginServiceImpl() {
        this.loginDAO = new LoginDAO();
    }

    @Override
    public User login(String email, String password)
            throws SQLException {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Email cannot be empty."
            );
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                "Password cannot be empty."
            );
        }

        return loginDAO.login(email.trim(), password);
    }

    @Override
    public String getUserRole(User user) {

        if (user == null) {
            return null;
        }

        return user.getRole();
    }
}
