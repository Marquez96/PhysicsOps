package marquez.physicsops.Classes;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

/**
 * Created by Carlos-Torre on 02/11/2017.
 */

public class User implements Serializable, IUser{
    private int idUser;
    private String userName;
    private String mail;
    private String password;
    private String name;
    private String surname;
    private ArrayList<EventCalendar> events;
    private ArrayList<Friend> friends;
    private UserSettings settings;
    private Calendar lastUpdate;

    /**
     *
     * @param idUser ID of the the user of the app
     * @param userName userName that the user will use to login in the system
     * @param name name of the username
     * @param surname surrname of the username
     * @param mail mail of the username
     * @param password password of the user that will use to login in the system
     * @param settings Settings of the user
     */
    public User(int idUser, String userName, String name, String surname, String mail, String password, UserSettings settings) {
        this.idUser = idUser;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.settings = settings;
        this.name = name;
        this.surname = surname;
        this.events = new ArrayList<>();
        this.friends = new ArrayList<>();
    }

    /**
     * Method that will use the database to create a User
     * @param query, row of the user that will be generated.
     */
    public User(Hashtable<String, String> query){
        this.idUser = Integer.parseInt(query.get("idUser"));
        this.userName = query.get("userName");
        this.mail = query.get("mail");
        this.password = query.get("password");
        this.password = query.get("password");
        this.name = query.get("name");
        this.surname = query.get("surname");
        String lastUpdateText = query.get("lastUpdate");
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            this.lastUpdate = Calendar.getInstance();
            this.lastUpdate.setTime(format1.parse(lastUpdateText));
        } catch (ParseException e) {
            Log.e("ErrorDate", e.toString());
            this.lastUpdate = Calendar.getInstance();
            this.lastUpdate.set(Calendar.YEAR, 1900);
        }
        this.events = new ArrayList<>();
        this.friends = new ArrayList<>();
    }

    /**
     * Empty contrusctor of the user
     */
    public User(){
        this.idUser = -1;
        this.userName = "null";
        this.mail = "null";
        this.password = "null";
        this.password = "null";
        this.name = "null";
        this.surname = "null";
        this.events = new ArrayList<>();
        this.friends = new ArrayList<>();
    }

    public String getFullName(){
        return this.name + " " + this.surname;
    }

    public int getIdUser() {
        return idUser;
    }

    public ArrayList<EventCalendar> getEvents (){
        return events;
    }

    public void addEvent(EventCalendar event){
        events.add(event);
        lastUpdate = Calendar.getInstance();
    }

    public void addInitialEvent(EventCalendar event){
        events.add(event);
    }

    public void addFriends(Friend request){
        friends.add(request);
        lastUpdate = Calendar.getInstance();
    }

    public void addInitialFriends(Friend f){
        friends.add(f);
    }

    @Override
    public String toString() {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return "User{\n" +
                "  idUser=" + idUser + "\n" +
                "  userName='" + userName + '\'' + "\n" +
                "  mail='" + mail + '\'' + "\n" +
                "  password='" + password + '\'' + "\n" +
                "  events="+ this.events.toString() + "\n" +
                "  friends="+ this.friends.toString() + "\n" +
                " lastUpdate="+format1.format(lastUpdate.getTime()) + "\n"+
                '}';
    }
    public ArrayList<EventCalendar> hasEvent(Calendar cal){
        ArrayList<EventCalendar> eventsDate = new ArrayList<>();
        for(EventCalendar e : this.events){
            if(e.getDay() == (int) cal.get(Calendar.DAY_OF_MONTH) && e.getMonth() == (int) cal.get(Calendar.MONTH)&& e.getYear() == (int) cal.get(Calendar.YEAR)){
                eventsDate.add(e);
            }
        }
        return eventsDate;
    }

    public void setEvents(ArrayList<EventCalendar> events) {
        this.events = events;
    }
    public String getMail() {
        return mail;
    }
    public String getUserName() {
        return userName;
    }

    /*
        FRIENDS
     */
    public ArrayList<Friend> getFriendsRequest() {
        ArrayList<Friend> reqUs = new ArrayList<>();
        for(int i=0; i < friends.size();i++){
            if(friends.get(i).getIdUserTo().equals(this.userName) && friends.get(i).getState().equals("request")){
                reqUs.add(friends.get(i));
            }
        }
        return reqUs;
    }
    public ArrayList<Friend> getFriends() {
        ArrayList<Friend> reqUs = new ArrayList<>();
        for(int i=0; i < friends.size();i++){
            if(friends.get(i).getState().equals("friend")){
                reqUs.add(friends.get(i));
            }
        }
        return reqUs;
    }

    public int checkCorrectUser(String friendUser){
        for(int i=0; i < friends.size();i++){
            if(friends.get(i).getIdUserFrom().equals(friendUser) || friends.get(i).getIdUserTo().equals(friendUser)){
                return friends.get(i).getState().equals("deleted") ? 0 : 1;
            }
        }
        return userName.equals(friendUser) ? 2: 0;
    }

    /*
        STATS
     */
    public ArrayList<Float> getDurationArrayPerMonth(int year){
        ArrayList<Float> durations = new ArrayList<>();
        for(int i = 0; i< 12;i++){
            float duration = 0 ;
            for(EventCalendar e : this.events){
                if(e.getMonth() == i && e.getYear() == year){
                    duration += e.getDuration();
                }
            }
            durations.add(duration);
        }
        return durations;
    }
    public ArrayList<Float> getImportanceArrayPerMonth(int year){
        ArrayList<Float> importances = new ArrayList<>();
        for(int i = 0; i< 12;i++){
            float importance = 0 ;
            for(EventCalendar e : this.events){
                if(e.getMonth() == i && e.getYear() == year){
                    importance += new Float(e.getImportance().floatValue());
                }
            }
            importances.add(importance);
        }
        return importances;
    }
    public ArrayList<Float> getNumArrayPerMonth(int year){
        ArrayList<Float> num = new ArrayList<>();
        for(int i = 0; i< 12;i++){
            float importance = 0 ;
            for(EventCalendar e : this.events){
                if(e.getMonth() == i && e.getYear() == year){
                    importance ++;
                }
            }
            num.add(importance);
        }
        return num;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate() {
        this.lastUpdate = Calendar.getInstance();
    }

    public static User getNewestUser(User userRest, User userBD) throws Exception{
        if (userRest.getLastUpdate().getTimeInMillis() > userBD.getLastUpdate().getTimeInMillis()){
            return userRest;
        } else if (userRest.getLastUpdate().getTimeInMillis() < userBD.getLastUpdate().getTimeInMillis()){
            FacadeRest f = new FacadeRest();
            f.updateUser(userBD);
            return userBD;
        }else {
            return userBD;
        }
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getStringDate(){
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format1.format(this.lastUpdate.getTime());
    }
}
