package marquez.physicsops.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import marquez.physicsops.Classes.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Locale;
import android.widget.Toast;
import android.content.Intent;
import marquez.physicsops.Classes.*;


import java.util.Calendar;

import marquez.physicsops.R;

public class AddEventActivity extends AbstractActivity {

    User user = null;
    Calendar dayEvent = null;
    EventCalendar eventFrom = null;

    TextView dateTextView = null;
    TextView typeTextView = null;
    TextView durationTextView = null;
    TextView importanceTextView = null;
    TextView commentaryTextView = null;
    TextView tittleTextView = null;

    DatePickerDialog.OnDateSetListener date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_activity);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_settings);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        setComponents(toolbar, drawer, true);

        dateTextView = (TextView) findViewById(R.id.add_activity_date);
        typeTextView = (TextView) findViewById(R.id.add_type_activity);
        durationTextView = (TextView) findViewById(R.id.add_duration);
        importanceTextView = (TextView) findViewById(R.id.add_importance);
        //commentaryTextView = (TextView) findViewById(R.id.add_comemmtary);
        tittleTextView = (TextView) findViewById(R.id.add_tittle);

        user = (User) getIntent().getExtras().getSerializable("user");
        dayEvent = (Calendar) getIntent().getExtras().getSerializable("dayEvent");
        eventFrom = (EventCalendar) getIntent().getExtras().getSerializable("event");

        Date dateEvent = dayEvent.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");


        dateTextView.setText(format1.format(dateEvent));
        new SetDate(dateTextView, this, dayEvent);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dayEvent.set(Calendar.YEAR, year);
                dayEvent.set(Calendar.MONTH, monthOfYear);
                dayEvent.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateText();
            }

        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventFrom == null) {
                    insertActivity();
                }
                else {
                    updateActivity();
                }
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        FacadeRest facadeRest = new FacadeRest();
                        try {
                            facadeRest.updateUser(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        if(eventFrom != null){
            tittleTextView.setText(eventFrom.getTitle());
            typeTextView.setText(eventFrom.getType());
            importanceTextView.setText(Integer.toString(eventFrom.getImportance()));
            durationTextView.setText(Float.toString(eventFrom.getDuration()));
        }

    }

    private void updateDateText() {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        dateTextView.setText(format1.format(dayEvent.getTime()));
    }
    private void insertActivity(){
        String date = dateTextView.getText().toString();
        String tittle = tittleTextView.getText().toString();
        String type = typeTextView.getText().toString();
        String duration = durationTextView.getText().toString();
        String importance = importanceTextView.getText().toString();
        EventCalendar e = new EventCalendar(tittle, type, dayEvent, Float.parseFloat(duration), Integer.parseInt(importance));
        DBSQLite db = new DBSQLite(getApplicationContext(), getResources().getString(R.string.nameDB), null, getResources().getInteger(R.integer.versionSQLite));
        FacadeSQLite sql = new FacadeSQLite(db);
        sql.insertActivity(user,e);
        user.addEvent(e);
        Intent output = new Intent();
        output.putExtra("user", user);
        setResult(RESULT_OK, output);
        finish();
    }
    private void updateActivity(){
        String date = dateTextView.getText().toString();
        String tittle = tittleTextView.getText().toString();
        String type = typeTextView.getText().toString();
        String duration = durationTextView.getText().toString();
        String importance = importanceTextView.getText().toString();
        eventFrom.setTitle(tittle);
        eventFrom.setType(type);
        eventFrom.setCal(date);
        eventFrom.setDuration(Float.parseFloat(duration));
        eventFrom.setImportance(Integer.parseInt(importance));
        DBSQLite db = new DBSQLite(getApplicationContext(), getResources().getString(R.string.nameDB), null, getResources().getInteger(R.integer.versionSQLite));
        FacadeSQLite sql = new FacadeSQLite(db);
        sql.updateActivity(user,eventFrom);
        Intent output = new Intent();
        output.putExtra("user", user);
        for(int i=0;i<user.getEvents().size();i++){
            if(user.getEvents().get(i).getId() == eventFrom.getId()){
                user.getEvents().remove(i);
                user.addEvent(eventFrom);
            }
        }
        setResult(RESULT_OK, output);
        finish();
    }
}

