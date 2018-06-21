package marquez.physicsops.Classes;

import android.location.Location;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

/**
 * Created by Carlos-Torre on 27/05/2018.
 */

public class PointEvent {
    private double latitude;
    private double longitude;
    private Calendar date;

    public PointEvent(Hashtable<String, String> query) {
        this.latitude = Double.parseDouble(query.get("latitude"));
        this.longitude = Double.parseDouble(query.get("longitude"));
        String dateText = query.get("date");
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            this.date = Calendar.getInstance();
            this.date.setTime(format1.parse(dateText));
        } catch (ParseException e) {
            Log.e("ErrorDate", e.toString());
            this.date = null;
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Calendar getDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return "PointEvent{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", date=" + format1.format(this.date.getTime()) +
                '}';
    }

    public Location getLocation(){
        Location location = new Location("");
        location.setLatitude(this.getLatitude());
        location.setLongitude(this.getLongitude());
        return location;
    }

}
