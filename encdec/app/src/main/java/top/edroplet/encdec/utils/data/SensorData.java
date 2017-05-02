package top.edroplet.encdec.utils.data;

/**
 * Created by xw on 2017/5/2.
 */

public class SensorData {
    private int type;
    private String name;
    private String version;
    private String vendor;

    public SensorData(int type, String name, String version, String vendor) {
        this.type = type;
        this.name = name;
        this.vendor = vendor;
        this.version = version;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getVersion() {
        return this.version;
    }
}
