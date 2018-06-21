package marquez.physicsops.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.text.InputType;
import android.widget.Toast;

import marquez.physicsops.Classes.DBSQLite;
import marquez.physicsops.Classes.FacadeRest;
import marquez.physicsops.Classes.FacadeSQLite;
import marquez.physicsops.Classes.PasswordHash;
import marquez.physicsops.Classes.User;
import marquez.physicsops.R;

public class RegisterActivity extends AppCompatActivity {

    TextView textName;
    TextView textSurname;
    TextView textMail;
    TextView textUser;
    TextView textPassword;
    TextView textRePassword;
    Button buttonAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textName = (TextView) findViewById(R.id.name_register);
        textSurname = (TextView) findViewById(R.id.surname_register);
        textMail = (TextView) findViewById(R.id.mail_register);
        textUser = (TextView) findViewById(R.id.user_register);
        textPassword = (TextView) findViewById(R.id.register_password);
        textRePassword = (TextView) findViewById(R.id.register_repassword);
        buttonAddUser = (Button) findViewById(R.id.button_register_user);
        textMail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DBSQLite db = new DBSQLite(getApplicationContext(), getResources().getString(R.string.nameDB), null, getResources().getInteger(R.integer.versionSQLite));
                    FacadeSQLite sql = new FacadeSQLite(db);
                    sql.addNewUser(textUser.getText().toString(), textMail.getText().toString(), PasswordHash.hash(textPassword.getText().toString()), textName.getText().toString(), textSurname.getText().toString());
                    final User user = sql.getUserBD(textUser.getText().toString());
                    Intent initActivity = new Intent(RegisterActivity.this, ControlPanelActivity.class);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            FacadeRest facadeRest = new FacadeRest();
                            try {
                                facadeRest.insertUser(user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    initActivity.putExtra("user", user);
                    startActivity(initActivity);
                    finish();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
