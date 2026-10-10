package backend;

import java.sql.SQLException;
import java.util.List;
import model.Settings;

public interface SettingsService {

    List<Settings> getAllSettings() throws SQLException;

    Settings getSettingById(int settingId) throws SQLException;

    String getSettingValue(String settingName) throws SQLException;

    int addSetting(String settingName, String settingValue)
            throws SQLException;

    boolean updateSetting(int settingId, String settingName,
                          String settingValue) throws SQLException;

    boolean deleteSetting(int settingId) throws SQLException;
}
