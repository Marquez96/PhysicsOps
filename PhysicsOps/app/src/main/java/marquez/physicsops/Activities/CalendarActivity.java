package marquez.physicsops.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import marquez.physicsops.Classes.EventCalendar;
import marquez.physicsops.Classes.LayoutDay;
import marquez.physicsops.Classes.User;
import marquez.physicsops.R;

public class CalendarActivity extends AbstractActivity {

    int request_code = 1;
    private LayoutDay[] layouts;
    private String[] days;
    private String[] monthNames;
    private float firstTouchX;
    private Calendar sameDate;
    private TextView month;
    private int aux = 0;
    private LayoutDay daySelected;
    private View coordLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        setComponents(toolbar,drawer,true);

        //Load variables from XML and variables from system
        daySelected = null;
        days = getResources().getStringArray(R.array.arrayDays);
        monthNames = getResources().getStringArray(R.array.arrayMonths);
        LinearLayout layout = (LinearLayout) findViewById(R.id.verticalLayout);
        ScrollView scrollView = (ScrollView) findViewById(R.id.login_form);
        GridLayout myGridLayout = (GridLayout) findViewById(R.id.mygrid);
        month = (TextView) findViewById(R.id.textViewMonth);
        coordLayout = findViewById(R.id.coordLayout);

        //Parte del toolbar
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        final int width = size.x;
        sameDate = Calendar.getInstance();


        //Calendar
        month.setGravity(Gravity.BOTTOM | Gravity.CENTER);

        month.setText(monthNames[sameDate.get(Calendar.MONTH)] + "  " +Integer.toString(sameDate.get(Calendar.YEAR)) + "\n");

        sameDate.set(Calendar.DAY_OF_MONTH, 1);
        sameDate.add(Calendar.DAY_OF_MONTH, -1);

        while (sameDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            sameDate.add(Calendar.DAY_OF_MONTH, -1);
        }
        setOnTouch(scrollView, width, sameDate);

        //GridLayout 7x7, one row for the name of the days
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width + 100);
        myGridLayout.setLayoutParams(params);
        myGridLayout.setColumnCount(7);
        myGridLayout.setRowCount(7);
        setOnTouch(layout, width, sameDate);

        int numOfCol = myGridLayout.getColumnCount();
        int numOfRow = myGridLayout.getRowCount();

        //Add to gridLayout names of days
        for (String day : days) {
            final LinearLayout t = new LinearLayout(this);
            t.setLayoutParams(new ActionBar.LayoutParams(3 * width / 23, LinearLayout.LayoutParams.WRAP_CONTENT));
            t.setGravity(Gravity.CENTER);
            TextView text = new TextView(this);
            text.setText(day);
            text.setTypeface(null, Typeface.BOLD);
            t.addView(text);
            myGridLayout.addView(t);
        }
        //load layouts and add it to gridLayout
        layouts = new LayoutDay[numOfCol * (numOfRow - 1)];
        for (int yPos = 0; yPos < numOfRow - 1; yPos++) {
            for (int xPos = 0; xPos < numOfCol; xPos++) {
                final LayoutDay tView = new LayoutDay(sameDate, this, width);
                setOnTouch(tView.getLayout(), width, sameDate);
                tView.setNullBackground();
                layouts[yPos * numOfCol + xPos] = tView;
                myGridLayout.addView(tView.getLayout());
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.transrighttoleft);
                tView.startAnimation(animation);
                for(EventCalendar e: user.hasEvent(sameDate)){
                    layouts[yPos * numOfCol + xPos].addEvent(e.getTextViewEvent(getApplicationContext()));
                }
                sameDate.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        sameDate.set(Calendar.DAY_OF_MONTH, 1);
        sameDate.add(Calendar.DAY_OF_MONTH, -1);
    }

    private void layoutAnimations(Animation animation) {
        for (LayoutDay layout : layouts)
            layout.startAnimation(animation);
    }

    private void setOnTouch(View view, final int width, final Calendar sameDate) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        firstTouchX = event.getX();
                        aux = 1;
                        for (LayoutDay layout : layouts)
                            if (v.getId() == layout.getIdLayout()) {
                                layout.setBackgroundColor(ContextCompat.getColor(CalendarActivity.this, R.color.lightGray), ContextCompat.getColor(CalendarActivity.this, R.color.water));
                                ArrayList<EventCalendar> eventsDate = user.hasEvent(layout.getDate());
                                if (layout.equals(daySelected)) {
                                    if(eventsDate.size() == 0) {
                                        Intent addEventIntent = new Intent(getApplicationContext(), AddEventActivity.class);
                                        addEventIntent.putExtra("user", user);
                                        Calendar dayEvent = Calendar.getInstance();
                                        dayEvent.set(Calendar.DAY_OF_MONTH, layout.getDay());
                                        dayEvent.set(Calendar.MONTH, layout.getMonth());
                                        dayEvent.set(Calendar.YEAR, layout.getYear());
                                        addEventIntent.putExtra("dayEvent", dayEvent);
                                        startActivityForResult(addEventIntent, 1);
                                    } else {
                                        Intent showEventIntent = new Intent(getApplicationContext(), ShowEventActivity.class);
                                        showEventIntent.putExtra("user", user);
                                        Calendar dayEvent = Calendar.getInstance();
                                        dayEvent.set(Calendar.DAY_OF_MONTH, layout.getDay());
                                        dayEvent.set(Calendar.MONTH, layout.getMonth());
                                        dayEvent.set(Calendar.YEAR, layout.getYear());
                                        showEventIntent.putExtra("dayEvent", dayEvent);
                                        showEventIntent.putExtra("events",eventsDate);
                                        startActivityForResult(showEventIntent, 1);
                                    }
                                } else {
                                    daySelected = layout;
                                }
                            } else {
                                layout.setNullBackground();
                            }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Animation animation;
                        if (Math.abs(firstTouchX - event.getX()) > 3 * width / 23 && aux == 1) {
                            if (firstTouchX > event.getX()) {
                                animation = AnimationUtils.loadAnimation(CalendarActivity.this, R.anim.transrighttoleft);
                                sameDate.add(Calendar.MONTH, 1);
                            } else {
                                animation = AnimationUtils.loadAnimation(CalendarActivity.this, R.anim.translefttoright);
                                sameDate.add(Calendar.MONTH, -1);
                            }
                            layoutAnimations(animation);
                            month.startAnimation(animation);

                            month.setText(monthNames[sameDate.get(Calendar.MONTH)]+ "  " + Integer.toString(sameDate.get(Calendar.YEAR)) + "\n");

                            sameDate.set(Calendar.DAY_OF_MONTH, 1);
                            sameDate.add(Calendar.DAY_OF_MONTH, -1);

                            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Log.d("wooola",format1.format(sameDate.getTime()));

                            while (sameDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                                sameDate.add(Calendar.DAY_OF_MONTH, -1);
                            }
                            for (LayoutDay layout : layouts) {
                                layout.setDayView(sameDate);
                                for(EventCalendar e: user.hasEvent(sameDate)){
                                    layout.addEvent(e.getTextViewEvent(getApplicationContext()));
                                }
                                sameDate.add(Calendar.DAY_OF_MONTH, 1);
                                layout.setNullBackground();
                            }
                            sameDate.set(Calendar.DAY_OF_MONTH, 1);
                            sameDate.add(Calendar.DAY_OF_MONTH, -1);
                            aux = 0;
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == request_code) && (resultCode == RESULT_OK)) {
            data.getDataString();
            user = (User) data.getExtras().getSerializable("user");
            for (LayoutDay layout : layouts) {
                Calendar sameDate = layout.getDate();
                layout.clearText();
                for(EventCalendar e: user.hasEvent(sameDate)){
                    layout.addEvent(e.getTextViewEvent(getApplicationContext()));
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent output = new Intent();
        output.putExtra("user", user);
        setResult(RESULT_OK, output);
        finish();
    }
}
