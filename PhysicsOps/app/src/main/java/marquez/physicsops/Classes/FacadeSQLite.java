package marquez.physicsops.Classes;

import android.location.Location;
import android.util.Log;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import marquez.physicsops.R;

/**
 * Created by Carlos-Torre on 21/11/2017.
 */

public class FacadeSQLite {

    DBSQLite sqlLite;

    public FacadeSQLite(DBSQLite sqlLite) {
        this.sqlLite = sqlLite;
    }

    public String obtenerTodoUsuario() {
        String q = "select * from User";
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        Cursor cursor = bd.rawQuery(q, null);

        return getResultQuery(getQueryResult(cursor));
    }

    public String obtenerTodoSettings() {
        String q = "select * from Settings";
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        Cursor cursor = bd.rawQuery(q, null);
        return getResultQuery(getQueryResult(cursor));
    }

    private ArrayList<Hashtable<String, String>> getQueryResult(Cursor cursor) {
        ArrayList<Hashtable<String, String>> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Hashtable<String, String> row = new Hashtable<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    try {
                        row.put(cursor.getColumnName(i), cursor.getString(i));
                    } catch (Exception e) {
                        Log.e("Error query", "Param -> " + cursor.getColumnName(i));
                    }
                }
                result.add(row);
            } while (cursor.moveToNext());
        }
        Log.d("FacadeSQLite", result.toString());
        return result;
    }

    public String getResultQuery(ArrayList<Hashtable<String, String>> queryResult) {
        String txt = "";
        if (!queryResult.isEmpty()) {
            Hashtable<String, String> row = queryResult.get(0);
            for (Map.Entry<String, String> entry : row.entrySet()) {
                txt += entry.getKey() + "   ";
            }
            txt += "\n\n";
            Iterator<Hashtable<String, String>> it = queryResult.iterator();
            while (it.hasNext()) {
                for (Map.Entry<String, String> entry : it.next().entrySet()) {
                    txt += entry.getValue() + "   ";
                }
                txt += "\n";
            }
        }
        return txt;
    }

    //**********************
    //*   USERS
    //**********************
    public boolean addNewUser(String userName, String mail, String password, String name, String surname) {
        boolean userInDB = false;
        String checkUser = "select userName from User where userName = '" + userName + "'";
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        Cursor cursor = bd.rawQuery(checkUser, null);
        if (!cursor.moveToFirst()) {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String queryUser = "insert into User(userName,mail,name,surname,lastUpdate, password) values(" +
                    "\"" + userName + "\"," +
                    "\"" + mail + "\"," +
                    "\"" + name + "\"," +
                    "\"" + surname + "\"," +
                    "\"" + format1.format(Calendar.getInstance().getTime()) + "\"," +
                    "\"" + password + "\")";
            bd.execSQL(queryUser);
            bd.close();
            User user = getUserBD(userName);
            bd = sqlLite.getWritableDatabase();
            String querySettings = "insert into Settings(online,color,idUser) values(\"" + "0" + "\",\"" + "green" + "\"," + user.getIdUser() + ")";
            bd.execSQL(querySettings);
            bd.close();
            userInDB = true;
            Log.d("Register login:", Integer.toString(user.getIdUser()));
        }
        return userInDB;
    }

    public boolean addNewUser(User user) {
        boolean userInDB = false;
        String checkUser = "select userName from User where userName = '" + user.getUserName() + "'";
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        Cursor cursor = bd.rawQuery(checkUser, null);
        if (!cursor.moveToFirst()) {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String queryUser = "insert into User(userName,mail,name,surname,lastUpdate, password) values(" +
                    "\"" + user.getUserName() + "\"," +
                    "\"" + user.getMail() + "\"," +
                    "\"" + user.getName() + "\"," +
                    "\"" + user.getSurname() + "\"," +
                    "\"" + format1.format(user.getLastUpdate().getTime()) + "\"," +
                    "\"" + user.getPassword() + "\")";
            bd.execSQL(queryUser);
            bd.close();
            userInDB = true;
            Log.d("Register login:", Integer.toString(user.getIdUser()));
        }
        return userInDB;
    }

    public boolean updateUser(User user, boolean delete) {
        if(delete) {
            deleteSettings(user);
            deleteAllFriends(user);
            deleteEvents(user);
            deleteUser(user);
            Log.d("DeletedAll", user.getUserName());
        }
        addNewUser(user);
        user.setIdUser(getUserBD(user.getUserName()).getIdUser());
        Log.d("ey", Integer.toString(user.getEvents().size()));
        for (int i = 0; i< user.getEvents().size(); i++){
            insertActivity(user,user.getEvents().get(i));
        }
        for (int i = 0; i< user.getFriends().size(); i++){
            insertFriend(user, user.getFriends().get(i));
        }
        insertSettings(user);
        return false;
    }

    public boolean checkUserCredentials(String userName, String password) {
        String checkUser = "select userName from User where userName = '" + userName + "' AND password = '" + password + "'";
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        Cursor cursor = bd.rawQuery(checkUser, null);
        return cursor.moveToFirst();
    }

    public User getUserBD(String userName) {
        User user = null;
        try {
            String checkUser = "select * from User where userName = '" + userName + "'";
            SQLiteDatabase bd = sqlLite.getReadableDatabase();
            Cursor cursor = bd.rawQuery(checkUser, null);
            ArrayList<Hashtable<String, String>> rs = getQueryResult(cursor);
            cursor.close();
            user = new User(rs.get(0));
            if (user.getFullName() != null) {
                /*
                 *  Events
                 */
                String getActivities = "select * from Events where idUser = '" + user.getIdUser() + "'";
                bd = sqlLite.getReadableDatabase();
                cursor = bd.rawQuery(getActivities, null);
                ArrayList<Hashtable<String, String>> rsEvent = getQueryResult(cursor);
                Log.d("LoginEvent:", rsEvent.toString());
                for (int i = 0; i < rsEvent.size(); i++) {
                    EventCalendar e = new EventCalendar(rsEvent.get(i));
                    Log.d("Here:", e.toString());
                    user.addInitialEvent(e);
                }
                /*
                *   Friends
                */
                String getFriends = "select * from Friends where idUser1 = '" + user.getUserName() + "' or idUser2 = '" + user.getUserName() + "'";
                bd = sqlLite.getReadableDatabase();
                cursor = bd.rawQuery(getFriends, null);
                ArrayList<Hashtable<String, String>> rsEventFriends = getQueryResult(cursor);
                Log.d("LoginEvent:", rsEventFriends.toString());
                for (int i = 0; i < rsEventFriends.size(); i++) {
                    Friend e = new Friend(rsEventFriends.get(i));
                    user.addInitialFriends(e);
                }
            }
            bd.close();
        } catch (Exception e) {
            Log.e("Login error:", e.toString());
        }
        return user;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String queryActivity = "delete from User where idUser = " + Integer.toString(user.getIdUser());
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("DeleteUser:", user.toString());
        return true;
    }

    public boolean updateLastUpdateUser(User user) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String queryUpdate = "update User SET " +
                "lastUpdate = '" + format1.format(Calendar.getInstance().getTime()) + "' "+
                " WHERE userName = '" + user.getUserName() + "'";
        bd.execSQL(queryUpdate);
        bd.close();
        Log.d("AddActivity add:", Integer.toString(user.getIdUser()));

        return true;
    }

    //**********************
    //*   Activities
    //**********************
    public boolean insertActivity(User user, EventCalendar e) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String queryActivity = "insert into Events(title,date,idUser,type,duration,importance) values(" +
                "\"" + e.getTitle() + "\"," +
                "\"" + format1.format(e.getCalendar().getTime()) + "\","
                + user.getIdUser() + "," +
                "\"" + e.getType() + "\"," +
                "\"" + Float.toString(e.getDuration()) + "\"," +
                "\"" + Integer.toString(e.getImportance()) + "\")";
        Log.d("Event", queryActivity);
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("AddActivity add:", Integer.toString(user.getIdUser()));
        updateLastUpdateUser(user);
        return true;
    }

    public void deleteEvents(User user, ArrayList<EventCalendar> eventsDeleted) {
        for (int i = 0; i < eventsDeleted.size(); i++) {
            SQLiteDatabase bd = sqlLite.getWritableDatabase();
            String queryActivity = "delete from Events where idEvents=" + Integer.toString(eventsDeleted.get(i).getId());
            bd.execSQL(queryActivity);
            bd.close();
            Log.d("FriendRequestDeleted:", Integer.toString(eventsDeleted.get(i).getId()));
        }
        updateLastUpdateUser(user);
    }

    public boolean updateActivity(User user, EventCalendar e) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String queryUpdate = "update Events SET " +
                "title = '" + e.getTitle() + "', " +
                "type = '" + e.getType() + "', " +
                "duration = '" + Float.toString(e.getDuration()) + "', " +
                "importance = '" + Integer.toString(e.getImportance()) + "', " +
                "date = '" + format1.format(e.getCalendar().getTime()) +
                "' WHERE idEvents = '" + e.getId() + "'";
        bd.execSQL(queryUpdate);
        bd.close();
        Log.d("AddActivity add:", Integer.toString(user.getIdUser()));
        updateLastUpdateUser(user);
        return true;
    }

    public boolean deleteEvents(User user) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String queryActivity = "delete from Events where idUser = " + Integer.toString(user.getIdUser());
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("DeleteEvents:", user.toString());
        return true;
    }

    //**********************
    //*   Friends
    //**********************
    public boolean insertFriend(User user, String userRequest) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String queryActivity = "insert into Friends(idUser1,idUser2,state) values(" +
                "\"" + user.getUserName() + "\"," +
                "\"" + userRequest + "\"," +
                "'" + "request" + "')";
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("FriendRequestAdd:", Integer.toString(user.getIdUser()));
        updateLastUpdateUser(user);
        return true;
    }

    public boolean insertFriend(User user, Friend f) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String queryActivity = "insert into Friends(idUser1,idUser2,state) values(" +
                "\"" + f.getIdUserFrom() + "\"," +
                "\"" + f.getIdUserTo() + "\"," +
                "'" + f.getState() + "')";
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("FriendRequestAdd:", f.getIdUserFrom());
        updateLastUpdateUser(user);
        return true;
    }

    public boolean updateFriend(User user, Friend f) {
        try {
            SQLiteDatabase bd = sqlLite.getWritableDatabase();
            String queryUpdate = "update Friends SET " +
                    "state = '" + "friend" + "'" +
                    " WHERE idFriends = " + f.getId() + "";
            bd.execSQL(queryUpdate);
            bd.close();
            Log.d("AddActivity add:", Integer.toString(user.getIdUser()));
        } catch (Exception e) {
            Log.e("ErrorUpdateFriend", e.toString());
        }
        updateLastUpdateUser(user);
        return true;
    }

    public boolean deleteFriend(User user, Friend f) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String queryActivity = "delete from Friends where idFriends = " + Integer.toString(f.getId());
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("FriendDeleted:", f.toString());
        updateLastUpdateUser(user);
        return true;
    }

    public boolean deleteAllFriends(User user) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String queryActivity = "delete from Friends where idUser1 = \"" + user.getUserName()+ "\" or idUser2 = \"" + user.getUserName() + "\"";
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("DeleteAllFriends:", user.toString());
        return true;
    }

    //****************
    //   Settings
    //****************
    public boolean deleteSettings(User user) {
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String queryActivity = "delete from Settings where idUser = " + Integer.toString(user.getIdUser());
        bd.execSQL(queryActivity);
        bd.close();
        Log.d("Settings:", user.toString());
        return true;
    }

    public boolean insertSettings(User user){
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        String querySettings = "insert into Settings(online,color,idUser) values(\"" + "0" + "\",\"" + "green" + "\"," + user.getIdUser() + ")";
        bd.execSQL(querySettings);
        bd.close();
        return true;
    }

    //*************
    //  POINT
    //*************
    public boolean insertPoint(Location point, User user){
        SQLiteDatabase bd = sqlLite.getWritableDatabase();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String queryPoint = "insert into Point(latitude,longitude,date, userName) values("
                + point.getLatitude()+ ","
                + point.getLongitude() + ","
                + "\"" + format1.format(Calendar.getInstance().getTime()) + "\","
                + "\"" + user.getUserName()+"\""
                + ")";
        bd.execSQL(queryPoint);
        bd.close();
        return true;
    }

    public PointsGPS getPointsUser(User user, boolean deleted){
        try {
            String checkUser = "select * from Point where userName = '" + user.getUserName() + "'";
            SQLiteDatabase bd = sqlLite.getReadableDatabase();
            Cursor cursor = bd.rawQuery(checkUser, null);
            ArrayList<Hashtable<String, String>> rs = getQueryResult(cursor);
            cursor.close();
            bd.close();
            PointsGPS points = new PointsGPS(rs);
            if (deleted) deletePointsUser(user);

            return points;
        }catch (Exception e){
            return new PointsGPS();
        }
    }

    public void deletePointsUser(User user){
        SQLiteDatabase bd = sqlLite.getReadableDatabase();
        String querySettings = "delete from Point where userName = \"" + user.getUserName() + "\"";
        bd.execSQL(querySettings);
        bd.close();
    }
}