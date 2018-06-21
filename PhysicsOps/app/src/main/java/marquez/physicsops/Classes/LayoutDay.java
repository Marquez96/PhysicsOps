package marquez.physicsops.Classes;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LayoutDay {
    private int day;
    private int month;
    private int year;
    private LinearLayout layout, layoutGray;
    private TextView dayTextView;
    private ArrayList<TextView> events;
    private ArrayList<LinearLayout> separators;
    private Activity activity;

    public LayoutDay(Calendar cal, Activity activity, int width){
        events = new ArrayList<>();
        separators = new ArrayList<>();
        this.activity = activity;
        this.day = cal.get(Calendar.DATE);
        this.month = cal.get(Calendar.MONTH);
        this.year = cal.get(Calendar.YEAR);
        layout = new LinearLayout(activity);
        layout.setLayoutParams(new ActionBar.LayoutParams(Math.round(3 * width / 24), Math.round(3 * width / 18)));
        layout.setId(this.hashCode());
        layout.setPadding(0, 0, 0, 0);
        layout.setOrientation(LinearLayout.VERTICAL);
        dayTextView = new TextView(activity);
        dayTextView.setText(Integer.toString(day));
        dayTextView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        layout.addView(dayTextView);
    }

    public int getIdLayout(){
        return layout.getId();
    }

    public void addEvent(TextView text){
        events.add(text);
        layout.addView(text);
        LinearLayout l = new LinearLayout(activity);
        separators.add(l);
        l.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
        layout.addView(l);
    }

    public void removeAllEvents(){
        layout.removeAllViews();
    }

    public TextView getEvent(int id){
        return events.get(id);
    }

    public LinearLayout getLayout(){
        return layout;
    }

    public void setBackgroundColor(int background, int border){
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(background);
        gd.setStroke(1, border);
        layout.setBackground(gd);
    }

    public void setNullBackground(){
        layout.setBackground(null);
    }

    public void startAnimation(Animation animation){
        layout.startAnimation(animation);
    }

    public void setDayView(String day){
        layout.removeAllViews();
        //dayTextView.setText(day);
    }
    public void setDayView(Calendar cal){
        this.clearText();
        dayTextView.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        this.setDay(cal.get(Calendar.DAY_OF_MONTH));
        this.setMonth(cal.get(Calendar.MONTH));
        this.setYear(cal.get(Calendar.YEAR));
    }

    public void clearText(){
        for (TextView v : events){
            layout.removeView(v);
        }
        for (LinearLayout sep : separators){
            layout.removeView(sep);
        }
    }

    public TextView getDayTextView() {
        return dayTextView;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Calendar getDate(){
        Calendar c = new GregorianCalendar();
        c.set(Calendar.DAY_OF_MONTH, this.day);
        c.set(Calendar.MONTH, this.month);
        c.set(Calendar.YEAR, this.year);
        return  c;
    }
}
