package marquez.physicsops.Classes;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Carlos-Torre on 03/06/2018.
 */

public interface IUser {
    public String getFullName();
    public ArrayList<EventCalendar> getEvents ();
    public ArrayList<EventCalendar> hasEvent(Calendar cal);
    public ArrayList<Friend> getFriendsRequest();
    public ArrayList<Friend> getFriends();
    public int checkCorrectUser(String friendUser);
    public ArrayList<Float> getDurationArrayPerMonth(int year);
    public ArrayList<Float> getImportanceArrayPerMonth(int year);
    public ArrayList<Float> getNumArrayPerMonth(int year);
    public Calendar getLastUpdate();

}
