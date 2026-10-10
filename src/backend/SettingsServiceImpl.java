package backend;

import database.SettingsDAO;
import java.sql.SQLException;
import java.util.List;
import model.Settings;

public class SettingsServiceImpl implements SettingsService {

    private final SettingsDAO settingsDAO;

    public SettingsServiceImpl() {
        this.settingsDAO = new SettingsDAO();
    }

    @Override
    public List<Settings> getAllSettings() throws SQLException {
        return settingsDAO.getAllSettings();
    }

    @Override
    public Settings getSettingById(int settingId) throws SQLException {

        if (settingId <= 0) {
            return null;
        }

        return settingsDAO.getSettingById(settingId);
    }

    @Override
    public String getSettingValue(String settingName)
            throws SQLException {

        if (settingName == null || settingName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Setting name cannot be empty."
            );
        }

        return settingsDAO.getSettingValue(settingName.trim());
    }

    @Override
    public int addSetting(String settingName, String settingValue)
            throws SQLException {

        if (settingName == null || settingName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Setting name cannot be empty."
            );
        }

        if (settingValue == null) {
            throw new IllegalArgumentException(
                    "Setting value cannot be null."
            );
        }

        return settingsDAO.addSetting(
                settingName.trim(),
                settingValue
        );
    }

    @Override
    public boolean updateSetting(
            int settingId,
            String settingName,
            String settingValue
    ) throws SQLException {

        if (settingId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid setting ID."
            );
        }

        if (settingName == null || settingName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Setting name cannot be empty."
            );
        }

        if (settingValue == null) {
            throw new IllegalArgumentException(
                    "Setting value cannot be null."
            );
        }

        return settingsDAO.updateSetting(
                settingId,
                settingName.trim(),
                settingValue
        );
    }

    @Override
    public boolean deleteSetting(int settingId) throws SQLException {

        if (settingId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid setting ID."
            );
        }

        return settingsDAO.deleteSetting(settingId);
    }
}