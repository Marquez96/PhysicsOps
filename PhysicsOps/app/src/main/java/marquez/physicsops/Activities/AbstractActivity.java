package marquez.physicsops.Activities;

import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.*;
import java.io.BufferedReader;

import java.util.Calendar;

import marquez.physicsops.Classes.*;
import marquez.physicsops.R;

/**
 * Created by Carlos-Torre on 29/11/2017.
 */

public class AbstractActivity extends AppCompatActivity {

    User user = null;
    int request_code = 1; //Request Code for user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setComponents(final FloatingActionButton fab, Toolbar toolbar, final DrawerLayout drawer, final boolean isFinish){
        setAuxScreen(toolbar,drawer,isFinish);
        setFab(fab, drawer);
        try {
            user = (User) getIntent().getExtras().getSerializable("user");
            Log.d("Abstract log",user.toString());
        } catch (Exception e){
            user = null;
            Log.e("Abstract log",e.toString());
        }
    }

    public void setComponents(Toolbar toolbar, final DrawerLayout drawer, final boolean isFinish){
        setAuxScreen(toolbar,drawer,isFinish);
        try {
            user = (User) getIntent().getExtras().getSerializable("user");
            Log.d("Abstract log",user.toString());
        }catch (Exception e){
            user = null;
            Log.d("Abstract log",e.toString());
        }
    }

    private void setFab(final FloatingActionButton fab, final DrawerLayout drawer) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.girar);
                anim.reset();
                fab.startAnimation(anim);
            }
        });
    }
    private void setAuxScreen(Toolbar toolbar, final DrawerLayout drawer, final boolean isFinish){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                ((TextView) findViewById(R.id.textViewNavName)).setText(user.getFullName());
                ((TextView) findViewById(R.id.textViewNavEmail)).setText("Email: " + user.getMail());
                LinearLayout profileActivity = (LinearLayout) findViewById(R.id.profile_linear);
                LinearLayout friendshipActivity = (LinearLayout) findViewById(R.id.amistades_linear);
                LinearLayout calendarActivity = (LinearLayout) findViewById(R.id.calendar_linear);
                LinearLayout statisticActivity = (LinearLayout) findViewById(R.id.statistic_linear);
                LinearLayout achievementsActivity = (LinearLayout) findViewById(R.id.achievements_linear);
                LinearLayout settingsActivity = (LinearLayout) findViewById(R.id.settings_linear);
                if(isFinish){
                    Intent output = new Intent();
                    output.putExtra("user", user);
                    setResult(RESULT_OK, output);
                }
                profileActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.closeDrawer(Gravity.START);
                        Intent profileActivity = new Intent(getApplicationContext(), ControlPanelActivity.class);
                        profileActivity.putExtra("user", user);
                        startActivity(profileActivity);
                        if(isFinish)
                            finish();
                    }
                });
                friendshipActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.closeDrawer(Gravity.START);
                        Intent amistadesIntent = new Intent(getApplicationContext(), FriendshipActivity.class);
                        amistadesIntent.putExtra("user", user);
                        startActivityForResult(amistadesIntent, request_code);
                        if(isFinish)
                            finish();
                    }
                });
                calendarActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.closeDrawer(Gravity.START);
                        Intent calendarIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                        calendarIntent.putExtra("user", user);
                        startActivityForResult(calendarIntent,request_code);
                        if(isFinish)
                            finish();
                    }
                });
                statisticActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.closeDrawer(Gravity.START);
                        Intent statisticIntent = new Intent(getApplicationContext(), StatisticsActivity.class);
                        statisticIntent.putExtra("user", user);
                        startActivity(statisticIntent);
                        if(isFinish)
                            finish();
                    }
                });
                achievementsActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.closeDrawer(Gravity.START);
                        Intent statisticIntent = new Intent(getApplicationContext(), AchievementsActivity.class);
                        statisticIntent.putExtra("user", user);
                        startActivity(statisticIntent);
                        if(isFinish)
                            finish();
                    }
                });
                settingsActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.closeDrawer(Gravity.START);
                        Intent statisticIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                        statisticIntent.putExtra("user", user);
                        startActivity(statisticIntent);
                        if(isFinish)
                            finish();
                    }
                });
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == request_code) {
            Log.d("RequestCodeAbstract",Integer.toString(requestCode));
            data.getDataString();
            user = (User) data.getExtras().getSerializable("user");
        }
    }
}
