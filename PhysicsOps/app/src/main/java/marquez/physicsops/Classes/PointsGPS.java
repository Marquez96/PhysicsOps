package marquez.physicsops.Classes;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

import android.location.Location;

/**
 * Created by Carlos-Torre on 27/05/2018.
 */

public class PointsGPS {
    private ArrayList<PointEvent> points = new ArrayList<>();

    public PointsGPS(ArrayList<Hashtable<String, String>> pointsQuery){
        for(Hashtable<String, String> queryPoint : pointsQuery) {
            PointEvent p = new PointEvent(queryPoint);
            points.add(p);
        }
    }

    public PointsGPS() {
    }

    @Override
    public String toString() {
        return "PointsGPS{" +
                "points=" + points +
                '}';
    }

    public EventCalendar getEventFromPoints(){
        float totalDistance = 0;
        try {
            for(int i = 0; i < points.size()-1;i++){
                float distance = points.get(i).getLocation().
                        distanceTo(points.get(i+1).getLocation());

                float miliseconds = points.get(i+1).getDate().getTimeInMillis() -
                        points.get(i).getDate().getTimeInMillis();
                float speed = distance *1000 / miliseconds;
                if(speed < (40*1000/3600) && miliseconds < (5*60*1000)){
                    Log.d("Points","Speed " + Float.toString(speed));
                    totalDistance += distance;
                } else {
                    Log.d("Point exceeded", Float.toString(speed));
                }
            }
            Log.d("Points",Float.toString(totalDistance));
        } catch (Exception e) {
            Log.e("PointError",e.toString());
        }
        //String title, String type, Calendar cal, float duration, int importance
        if(points.size() > 0) {
            float duration =(points.get(points.size()-1).getDate().getTimeInMillis() - points.get(0).getDate().getTimeInMillis())/(1000*60*60);
            EventCalendar e = new EventCalendar(String.format("%.1f", totalDistance/1000) + "km", "Recorrido", points.get(0).getDate(), duration,1);
            return e;
        } else {
            return null;
        }
    }

    protected ArrayList<PointEvent> getPoints() {
        return points;
    }

    public static void processDataBase(FacadeSQLite facade, User user){
        PointsGPS pointsGPS = facade.getPointsUser(user, false);
        if(pointsGPS.getPoints().size() > 0 ) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) !=
                    pointsGPS.getPoints().get(0).getDate().get(Calendar.DAY_OF_MONTH)) {

                EventCalendar e = pointsGPS.getEventFromPoints();
                facade.insertActivity(user,e);
                user.addEvent(e);
                facade.deletePointsUser(user);
            }
        }
    }
}
