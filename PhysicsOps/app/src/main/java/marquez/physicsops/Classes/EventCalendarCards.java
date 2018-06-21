package marquez.physicsops.Classes;

import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.Toast;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Calendar;

import marquez.physicsops.R;

/**
 * Created by Carlos-Torre on 09/05/2018.
 */

public class EventCalendarCards {
    private ArrayList<EventCalendar> events;
    private ArrayList<Boolean> eventsSelected;
    private FloatingActionButton fab;
    private int cont;
    private boolean isSelectMode;
    private static final long DOUBLE_PRESS_INTERVAL = 500; // in millis
    private long lastPressTime;
    private boolean mHasDoubleClicked = false;

    public EventCalendarCards(ArrayList<EventCalendar> events, FloatingActionButton fab) {
        this.events = events;
        this.fab = fab;
        this.cont = 0;
        eventsSelected = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            eventsSelected.add(false);
        }
    }

    public void selectLongEvent(View v, int i, LinearLayout l) {
        this.isSelectMode = true;
    }

    public String selectShortEvent(View v, int i, View l) {
        boolean isSelected;
        if (isSelectMode) {
            this.isSelectMode = true;
            if (eventsSelected.get(i)) {
                eventsSelected.set(i, false);
                l.setBackgroundColor(v.getResources().getColor(R.color.lightGray));
                cont--;
                isSelected = true;
            } else {
                eventsSelected.set(i, true);
                l.setBackgroundColor(v.getResources().getColor(R.color.Gray));
                cont++;
                isSelected = false;
            }
            if (isSelected && cont == 0) {
                fab.hide();
                Handler handler = new Handler();
                final Drawable draw = ContextCompat.getDrawable(v.getContext(), R.drawable.plus);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.setImageDrawable(draw);
                        fab.show();
                    }
                }, 350);

            } else if (!isSelected && cont == 1){
                fab.hide();
                Handler handler = new Handler();
                final Drawable draw = ContextCompat.getDrawable(v.getContext(), R.drawable.delete);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.setImageDrawable(draw);
                        fab.show();
                    }
                }, 350);
            }
            if(cont == 0){
                this.isSelectMode = false;
            }
        } else {
            long pressTime = System.currentTimeMillis();
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
                mHasDoubleClicked = true;
                return "doubleClick";
            }
            lastPressTime = pressTime;
        }
        return "null";
    }
    public String getAction(){
        return cont == 0 ? "add":"remove";
    }

    public ArrayList<EventCalendar> removeEvents(User user, View v){
        //Change ArrayList
        ArrayList<EventCalendar> eventsDeleted = new ArrayList<>();
        Calendar cal = events.get(0).getCal();
        for(int i=0;i<eventsSelected.size();i++){
            if(eventsSelected.get(i)){
                user.getEvents().remove(events.get(i));
                user.setLastUpdate();
                eventsDeleted.add(events.get(i));
            }
        }
        events = user.hasEvent(cal);
        eventsSelected = new ArrayList<>();
        for(int i=0;i<events.size();i++){
            eventsSelected.add(false);
        }

        //Change graphical interface
        fab.hide();
        Handler handler = new Handler();
        final Drawable draw = ContextCompat.getDrawable(v.getContext(), R.drawable.plus);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.setImageDrawable(draw);
                fab.show();
            }
        }, 350);
        this.isSelectMode = false;
        this.cont = 0;
        return eventsDeleted;
    }

    public void setEvents(ArrayList<EventCalendar> events) {
        this.events = events;
        eventsSelected = new ArrayList<>();
        for(int i=0;i<events.size();i++){
            eventsSelected.add(false);
        }
    }
}
