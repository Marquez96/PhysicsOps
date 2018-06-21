package marquez.physicsops.Classes;

import android.util.Log;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by Carlos-Torre on 06/05/2018.
 */

public class Friend implements Serializable {
    int id;
    String idUserFrom;
    String idUserTo;
    String state;

    public Friend(String idUserFrom, String idUserTo) {
        this.idUserFrom = idUserFrom;
        this.idUserTo = idUserTo;
    }

    public Friend(String idUserFrom, String idUserTo, String state) {
        this.idUserFrom = idUserFrom;
        this.idUserTo = idUserTo;
        this.state = state;
        this.id = -1;
    }

    public Friend (Hashtable<String, String> query) {
        //
        try {
            this.id = Integer.parseInt(query.get("idFriends"));
            this.idUserFrom = query.get("idUser1");
            this.idUserTo = query.get("idUser2");
            this.state = query.get("state");
        }catch (Exception e){
            Log.e("ErrorFriend", e.toString());
        }
    }

    public String getIdUserFrom() {
        return idUserFrom;
    }

    public String getIdUserTo() {
        return idUserTo;
    }

    public String getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "idUserFrom='" + idUserFrom + '\'' +
                ", idUserTo='" + idUserTo + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

}
