package marquez.physicsops.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;
import marquez.physicsops.Classes.*;
import marquez.physicsops.R;
import java.text.SimpleDateFormat;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowEventActivity extends AppCompatActivity {

    Calendar dayEvent = null;
    User user = null;
    private RecyclerView rv;
    private EventCalendarCards cards;
    private TextView textViewDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewDay = (TextView) findViewById(R.id.textViewShowEvent);
        setSupportActionBar(toolbar);

        try {
            dayEvent = (Calendar) getIntent().getExtras().getSerializable("dayEvent");
            user = (User) getIntent().getExtras().getSerializable("user");
            Log.d("ShowEvent log", user.toString());
        } catch (Exception e) {
            user = null;
            Log.e("ShowEventError", e.toString());
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_show_event);
        cards = new EventCalendarCards(user.hasEvent(dayEvent), fab);
        rv = (RecyclerView) findViewById(R.id.rvEvents);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        RVAdapterEvents adapter = new RVAdapterEvents(user, user.hasEvent(dayEvent), cards);
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        textViewDay.setText(format1.format(dayEvent.getTime()));
        rv.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cards.getAction().equals("add")) {
                    Intent addEventIntent = new Intent(getApplicationContext(), AddEventActivity.class);
                    addEventIntent.putExtra("user", user);
                    addEventIntent.putExtra("dayEvent", dayEvent);
                    startActivityForResult(addEventIntent, 1);
                } else {
                    ArrayList<EventCalendar> eventsDeleted = cards.removeEvents(user, view);
                    RVAdapterEvents adapter = new RVAdapterEvents(user, user.hasEvent(dayEvent), cards);
                    rv.setAdapter(adapter);
                    DBSQLite db = new DBSQLite(getApplicationContext(), getResources().getString(R.string.nameDB), null, getResources().getInteger(R.integer.versionSQLite));
                    FacadeSQLite sql = new FacadeSQLite(db);
                    sql.deleteEvents(user, eventsDeleted);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            data.getDataString();
            user = (User) data.getExtras().getSerializable("user");
            RVAdapterEvents adapter = new RVAdapterEvents(user, user.hasEvent(dayEvent), cards);
            rv.setAdapter(adapter);
            cards.setEvents(user.hasEvent(dayEvent));
        }
    }

    @Override
    public void onBackPressed() {
        Intent output = new Intent();
        output.putExtra("user", user);
        setResult(RESULT_OK, output);
        finish();
        return;
    }
}
