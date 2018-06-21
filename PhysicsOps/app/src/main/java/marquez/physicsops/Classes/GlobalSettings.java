package marquez.physicsops.Classes;

/**
 * Created by Carlos-Torre on 30/05/2018.
 */

public class GlobalSettings {
    String ip;
    String port;
    boolean connectivity = false;

    public GlobalSettings(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean isConnectivity() {
        return connectivity;
    }

    public void setConnectivity(boolean connectivity) {
        this.connectivity = connectivity;
    }
}
