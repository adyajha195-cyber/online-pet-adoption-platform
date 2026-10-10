package backend;

import model.User;
import java.sql.SQLException;

public interface LoginService {

    User login(String email, String password) throws SQLException;

    String getUserRole(User user);
}
