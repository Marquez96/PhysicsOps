package marquez.physicsops.Classes;

import android.database.Cursor;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import 	java.io.OutputStream;
import 	java.io.OutputStreamWriter;

/**
 * Created by Carlos-Torre on 15/05/2018.
 */

public class FacadeRest {

    public User login(String username, String password) throws Exception {
        HttpURLConnection myConnection = null;
        User user = null;
        URL endPoint = new URL("http://192.168.1.113:3000/users/" + username + "/"+password);
        myConnection = (HttpURLConnection) endPoint.openConnection();
        if (myConnection.getResponseCode() == 200) {
            InputStream responseBody = myConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(responseBody));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            Gson gson = new Gson();
            user = gson.fromJson(sb.toString(), User.class);
            return user;
        } else if(myConnection.getResponseCode() == 401){
            return null;
        }
        if (myConnection != null) {
            return null;
        }
        return user;
    }

    public boolean insertUser(User user) throws Exception {
        HttpURLConnection myConnection = null;
        URL endPoint = new URL("http://192.168.1.113:3000/users");
        myConnection = (HttpURLConnection) endPoint.openConnection();
        myConnection.setRequestMethod("POST");
        myConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        OutputStream os = myConnection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        Gson gson = new Gson();
        osw.write(gson.toJson(user));
        osw.close();
        os.close();
        if (myConnection.getResponseCode() == 200) {
            return true;
        } else if(myConnection.getResponseCode() == 401){
            return false;
        }
        if (myConnection != null) {
            return false;
        }
        return true;
    }

    public boolean updateUser(User user) throws Exception {
        HttpURLConnection myConnection = null;
        URL endPoint = new URL("http://192.168.1.113:3000/users");
        myConnection = (HttpURLConnection) endPoint.openConnection();
        myConnection.setRequestMethod("PUT");
        myConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        OutputStream os = myConnection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        Gson gson = new Gson();
        osw.write(gson.toJson(user));
        osw.close();
        os.close();
        if (myConnection.getResponseCode() == 200) {
            return true;
        } else if(myConnection.getResponseCode() != 200){
            return false;
        }
        if (myConnection != null) {
            return false;
        }
        return true;
    }

    public boolean checkUser(String username) throws Exception {
        HttpURLConnection myConnection = null;
        URL endPoint = new URL("http://192.168.1.113:3000/checkUser/" + username );
        myConnection = (HttpURLConnection) endPoint.openConnection();
        if (myConnection.getResponseCode() == 200) {
            InputStream responseBody = myConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(responseBody));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            return sb.toString().contains("true");
        } else if(myConnection.getResponseCode() == 401){
            return false;
        }
        if (myConnection != null) {
            return false;
        }
        return false;
    }

    public boolean checkConnectivity() throws Exception {
        HttpURLConnection myConnection = null;
        URL endPoint = new URL("http://192.168.1.113:3000/api");
        myConnection = (HttpURLConnection) endPoint.openConnection();
        myConnection.setConnectTimeout(2000);
        if (myConnection.getResponseCode() == 200) {
            return true;
        } else if(myConnection.getResponseCode() == 401){
            return false;
        } else {
            return false;
        }
    }

    public boolean operationFriend(Friend friend, String method) throws Exception {
        HttpURLConnection myConnection = null;
        URL endPoint = new URL("http://192.168.1.113:3000/friend");
        myConnection = (HttpURLConnection) endPoint.openConnection();
        myConnection.setRequestMethod(method);
        myConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        OutputStream os = myConnection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        Gson gson = new Gson();
        osw.write(gson.toJson(friend));
        osw.close();
        os.close();
        if (myConnection.getResponseCode() == 200) {
            return true;
        } else if(myConnection.getResponseCode() == 401){
            return false;
        }
        if (myConnection != null) {
            return false;
        }
        return false;
    }



}
