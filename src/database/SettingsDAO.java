package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Setting;

/** Database operations for system settings (key/value pairs). */
public class SettingsDAO {

    private static Setting map(ResultSet r) throws SQLException {
        return new Setting(r.getInt("setting_id"), r.getString("setting_name"), r.getString("setting_value"));
    }

    public List<Setting> getAllSettings() throws SQLException {
        List<Setting> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM Settings ORDER BY setting_id");
             ResultSet r = ps.executeQuery()) {
            while (r.next()) {
                list.add(map(r));
            }
        }
        return list;
    }

    /** Returns the setting, or null if no setting has this id. */
    public Setting getSettingById(int settingId) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM Settings WHERE setting_id = ?")) {
            ps.setInt(1, settingId);
            try (ResultSet r = ps.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    /** Convenience: the value of a named setting (e.g. "MaintenanceMode"), or null if it doesn't exist. */
    public String getSettingValue(String settingName) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT setting_value FROM Settings WHERE setting_name = ?")) {
            ps.setString(1, settingName);
            try (ResultSet r = ps.executeQuery()) {
                return r.next() ? r.getString(1) : null;
            }
        }
    }

    /**
     * Adds a setting and returns its new id.
     * @throws SQLException (duplicate key) if a setting with this name already exists
     */
    public int addSetting(String settingName, String settingValue) throws SQLException {
        String sql = "INSERT INTO Settings (setting_name, setting_value) VALUES (?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, settingName);
            ps.setString(2, settingValue);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    /** Returns true if updated, false if no such setting. */
    public boolean updateSetting(int settingId, String settingName, String settingValue) throws SQLException {
        String sql = "UPDATE Settings SET setting_name = ?, setting_value = ? WHERE setting_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, settingName);
            ps.setString(2, settingValue);
            ps.setInt(3, settingId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteSetting(int settingId) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Settings WHERE setting_id = ?")) {
            ps.setInt(1, settingId);
            return ps.executeUpdate() > 0;
        }
    }
}
