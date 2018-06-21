package marquez.physicsops.Classes;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

import marquez.physicsops.R;

/**
 * Created by Carlos on 17/05/2017.
 */

public class EventCalendar implements Serializable, IEvent {
    private String title, type;
    private int id;
    private Integer day, month, year, importance;
    private Calendar cal;
    private float duration;

    public EventCalendar(String title, String type, Calendar cal) {
        this.title = title;
        this.type = type;
        this.cal = cal;
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.month = cal.get(Calendar.MONTH);
        this.year = cal.get(Calendar.YEAR);
        this.id = -1;
    }

    public EventCalendar(String title, String type, Calendar cal, float duration, int importance) {
        this.title = title;
        this.type = type;
        this.cal = cal;
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.month = cal.get(Calendar.MONTH);
        this.year = cal.get(Calendar.YEAR);
        this.id = -1;
        this.duration = duration;
        this.importance = importance;
    }

    public EventCalendar (Hashtable<String, String> query) {
        try {
            this.id = Integer.parseInt(query.get("idEvents"));
            this.title = query.get("title");
            this.type = query.get("type");
            this.duration = Float.parseFloat(query.get("duration"));
            this.importance = Integer.parseInt(query.get("importance"));
            String dateTxt = query.get("date");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                this.cal = Calendar.getInstance();
                this.cal.setTime(sdf.parse(dateTxt));
            } catch (ParseException e) {
                Log.e("ErrorDate", e.toString());
                this.cal = Calendar.getInstance();
                this.cal.set(Calendar.YEAR, 1900);
            }
            Log.d("Date2", this.cal.toString());
            this.day = this.cal.get(Calendar.DAY_OF_MONTH);
            this.month = this.cal.get(Calendar.MONTH);
            this.year = this.cal.get(Calendar.YEAR);
        }catch (Exception e){
            Log.e("ErrorDate", e.toString());
        }
    }

    public Calendar getCalendar() {
        return cal;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getMonth() {
        return month;
    }

    public TextView getTextViewEvent(Context context){
        TextView event = new TextView(context);
        event.setText(this.getShortTitle());
        event.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        event.setBackgroundColor(ContextCompat.getColor(context, R.color.skyBlue));
        event.setTextColor(ContextCompat.getColor(context, R.color.white));
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCalendar)) return false;

        EventCalendar that = (EventCalendar) o;

        if(day == null ||month == null || year == null)
            return false;

        return day.equals(that.getDay()) && month.equals(that.getMonth()) && year.equals(that.getYear());
        /*if (day != null ? !day.equals(that.day) : that.day != null) return false;
        if (month != null ? !month.equals(that.month) : that.month != null) return false;
        return year != null ? year.equals(that.year) : that.year == null;*/

    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Integer getYear() {
        return year;
    }

    public Calendar getCal() {
        return cal;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCal(String dateTxt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.cal = Calendar.getInstance();
            this.cal.setTime(sdf.parse(dateTxt));
        } catch (ParseException e) {
            Log.e("ErrorDate", e.toString());
            this.cal = Calendar.getInstance();
            this.cal.set(Calendar.YEAR, 1900);
        }
        Log.d("Date3", this.cal.toString());
        this.day = this.cal.get(Calendar.DAY_OF_MONTH);
        this.month = this.cal.get(Calendar.MONTH);
        this.year = this.cal.get(Calendar.YEAR);
    }

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "EventCalendar{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", importance=" + importance +
                ", duration=" + duration +
                '}';
    }

    public String getShortTitle(){
        return this.title.length() > 5 ? this.title.substring(0,5) : this.title;
    }
}
