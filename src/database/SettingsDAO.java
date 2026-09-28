package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsDAO {

    public void getAllSettings() {

        String sql = "SELECT * FROM Settings";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                System.out.println(
                    result.getInt("setting_id") + " | " +
                    result.getString("setting_name") + " | " +
                    result.getString("setting_value")
                );
            }

            result.close();
            statement.close();
           DatabaseConnection.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   
        public void getSettingById(int settingId) {

    String sql = "SELECT * FROM Settings WHERE setting_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql);

        statement.setInt(1, settingId);

        ResultSet result = statement.executeQuery();

        if (result.next()) {

            System.out.println(
                result.getInt("setting_id") + " | " +
                result.getString("setting_name") + " | " +
                result.getString("setting_value")
            );

        } else {
            System.out.println("Setting not found.");
        }

        result.close();
        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}       
    public void addSetting(String settingName, String settingValue) {

    String sql = "INSERT INTO Settings (setting_name, setting_value) VALUES (?, ?)";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql);

        statement.setString(1, settingName);
        statement.setString(2, settingValue);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Setting added successfully!");
        }

        statement.close();
       DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}      public void updateSetting(int settingId, String settingName, String settingValue) {

    String sql = "UPDATE Settings " +
                 "SET setting_name = ?, setting_value = ? " +
                 "WHERE setting_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql);

        statement.setString(1, settingName);
        statement.setString(2, settingValue);
        statement.setInt(3, settingId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Setting updated successfully!");
        } else {
            System.out.println("Setting not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void deleteSetting(int settingId) {

    String sql = "DELETE FROM Settings WHERE setting_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql);

        statement.setInt(1, settingId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Setting deleted successfully!");
        } else {
            System.out.println("Setting not found.");
        }

        statement.close();
       DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}