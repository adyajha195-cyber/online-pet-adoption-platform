package backend;

import java.sql.SQLException;
import java.util.List;
import model.Setting;

public interface SettingsService {

    List<Setting> getAllSettings() throws SQLException;

    Setting getSettingById(int settingId) throws SQLException;

    String getSettingValue(String settingName) throws SQLException;

    int addSetting(String settingName, String settingValue)
            throws SQLException;

    boolean updateSetting(int settingId, String settingName,
                          String settingValue) throws SQLException;

    boolean deleteSetting(int settingId) throws SQLException;
}
