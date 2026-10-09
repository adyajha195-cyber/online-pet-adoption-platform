package model;

/** Plain data object for one row of the Setting table. Immutable. */
public class Setting {

    private final int settingId;
    private final String name;
    private final String value;

    public Setting(int settingId, String name, String value) {
        this.settingId = settingId;
        this.name = name;
        this.value = value;
    }

    public int getSettingId() { return settingId; }
    public String getName() { return name; }
    public String getValue() { return value; }

    @Override
    public String toString() {
        return settingId + " | " + name + " | " + value;
    }
}
