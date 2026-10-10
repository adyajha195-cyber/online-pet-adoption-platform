package backend;

import database.SettingsDAO;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import model.Setting;

public class SettingsServiceImpl implements SettingsService {

    private final SettingsDAO settingsDAO;

    public SettingsServiceImpl() {
        this.settingsDAO = new SettingsDAO();
    }

    @Override
    public List<Setting> getAllSettings() throws SQLException {
        return settingsDAO.getAllSettings();
    }

    @Override
    public Setting getSettingById(int settingId) throws SQLException {

        if (settingId <= 0) {
            return null;
        }

        return settingsDAO.getSettingById(settingId);
    }

    @Override
    public String getSettingValue(String settingName)
            throws SQLException {

        requireName(settingName);

        return settingsDAO.getSettingValue(settingName.trim());
    }

    @Override
    public int addSetting(String settingName, String settingValue)
            throws SQLException {

        requireName(settingName);
        requireValue(settingValue);

        try {
            return settingsDAO.addSetting(settingName.trim(), settingValue);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException(
                    "A setting named '" + settingName.trim()
                            + "' already exists.", e
            );
        }
    }

    @Override
    public boolean updateSetting(
            int settingId,
            String settingName,
            String settingValue
    ) throws SQLException {

        if (settingId <= 0) {
            throw new IllegalArgumentException("Invalid setting ID.");
        }

        requireName(settingName);
        requireValue(settingValue);

        try {
            return settingsDAO.updateSetting(
                    settingId,
                    settingName.trim(),
                    settingValue
            );
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException(
                    "A setting named '" + settingName.trim()
                            + "' already exists.", e
            );
        }
    }

    @Override
    public boolean deleteSetting(int settingId) throws SQLException {

        if (settingId <= 0) {
            throw new IllegalArgumentException("Invalid setting ID.");
        }

        return settingsDAO.deleteSetting(settingId);
    }

    private static void requireName(String settingName) {
        if (settingName == null || settingName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Setting name cannot be empty."
            );
        }
    }

    private static void requireValue(String settingValue) {
        if (settingValue == null) {
            throw new IllegalArgumentException(
                    "Setting value cannot be null."
            );
        }
    }
}
