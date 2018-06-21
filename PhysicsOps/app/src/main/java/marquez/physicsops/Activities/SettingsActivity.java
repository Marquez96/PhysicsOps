package marquez.physicsops.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import marquez.physicsops.Classes.DBSQLite;
import marquez.physicsops.Classes.FacadeSQLite;
import marquez.physicsops.Classes.User;
import marquez.physicsops.R;

public class SettingsActivity extends  AbstractActivity {

    ActionBarDrawerToggle toggle;
    Button buttonUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_settings);
        setComponents(toolbar, drawer, true);
        buttonUpdate = (Button) findViewById(R.id.button_update_settings);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DBSQLite db = new DBSQLite(getApplicationContext(), getResources().getString(R.string.nameDB), null, getResources().getInteger(R.integer.versionSQLite));
                    FacadeSQLite sql = new FacadeSQLite(db);
                    try{

                    }catch (Exception e){}
                    Toast.makeText(getApplicationContext(), "El servidor no contesta", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
