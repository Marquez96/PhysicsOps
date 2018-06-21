package marquez.physicsops.Classes;

import java.io.Serializable;

/**
 * Created by Carlos-Torre on 02/11/2017.
 */

public class UserSettings implements Serializable {
    private String changedPassword;
    private String languaje;
    private boolean notifications;

    public UserSettings(String changedPassword, String languaje, boolean notifications) {
        this.changedPassword = changedPassword;
        languaje = languaje;
        notifications = notifications;
    }
    public UserSettings() {
        this.changedPassword = "";
        languaje = "english";
        notifications = true;
    }
}
