package marquez.physicsops.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import marquez.physicsops.Classes.DBSQLite;
import marquez.physicsops.Classes.EventCalendar;
import marquez.physicsops.Classes.FacadeSQLite;
import marquez.physicsops.Classes.PointsGPS;
import marquez.physicsops.R;
import android.widget.Button;

public class AchievementsActivity extends AbstractActivity {

    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private DBSQLite db;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout5);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        setComponents(fab,toolbar,drawer,true);

        db = new DBSQLite(getApplicationContext(), "Administration", null, getResources().getInteger(R.integer.versionSQLite));
        final FacadeSQLite sql = new FacadeSQLite(db);
        PointsGPS points = sql.getPointsUser(user, false);
        TextView t = (TextView) findViewById(R.id.valor2);
        DBSQLite db = new DBSQLite(getApplicationContext(), getResources().getString(R.string.nameDB), null, getResources().getInteger(R.integer.versionSQLite));
        t.setText(sql.obtenerTodoUsuario());

        final Button button = (Button) findViewById(R.id.button_id_calculo);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PointsGPS points = sql.getPointsUser(user, false);
                EventCalendar eventDoIt = points.getEventFromPoints();
                sql.insertActivity(user,eventDoIt);
                user.addEvent(eventDoIt);
                Log.d("Event",user.toString());
            }
        });
        Log.d("Points",points.toString());
        points.getEventFromPoints();
    }
}
