package marquez.physicsops.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import marquez.physicsops.Classes.*;

import marquez.physicsops.R;

public class FriendshipActivity extends AbstractActivity {

    ActionBarDrawerToggle toggle;
    private float firstTouchX;
    private int aux;
    private TextView textSolicitud;
    private TextView textActualFriends;
    private LinearLayout linear, linearSearch, linearCards, linearActual;
    private RecyclerView rvRequest;
    private RecyclerView rvActual;
    private DBSQLite db;
    private FacadeSQLite sql;
    private TextView userFriendTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        db = new DBSQLite(getApplicationContext(), "Administration", null, getResources().getInteger(R.integer.versionSQLite));

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        setComponents(toolbar, drawer, true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_friend);
        textSolicitud = (TextView) findViewById(R.id.textSolicitud);
        textActualFriends = (TextView) findViewById(R.id.textActualFriends);
        textActualFriends.setText(Html.fromHtml(getResources().getString(R.string.bold_text_actual_friends), Html.FROM_HTML_MODE_LEGACY));
        linear = (LinearLayout) findViewById(R.id.layout_friendship);
        linearSearch = (LinearLayout) findViewById(R.id.linearAddFriend);
        userFriendTextView = (TextView) findViewById(R.id.textViewSearchFriend);
        linearActual = (LinearLayout) findViewById(R.id.linearCardsActualFriends);
        setOnTouch(linear);
        setOnTouch(linearSearch);

        rvRequest = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvRequest.setHasFixedSize(true);
        rvRequest.setLayoutManager(llm);
        RVAdapterFriendsRequest adapter = new RVAdapterFriendsRequest(user, user.getFriendsRequest(), this);
        rvRequest.setAdapter(adapter);

        rvActual = (RecyclerView) findViewById(R.id.rvActual);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        rvActual.setHasFixedSize(true);
        rvActual.setLayoutManager(llm2);
        RVAdapterFriends adapter2 = new RVAdapterFriends(user, user.getFriends(), this);
        rvActual.setAdapter(adapter2);

        db = new DBSQLite(getApplicationContext(), "Administration", null, getResources().getInteger(R.integer.versionSQLite));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserRequest();
            }
        });
        userFriendTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    checkUserRequest();
                    return true;
                }
                return false;
            }
        });
    }

    private void setOnTouch(View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        firstTouchX = event.getX();
                        aux = 1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(firstTouchX - event.getX()) > 5 && aux == 1) {
                            if (firstTouchX > event.getX()) {
                                showActualFriends();
                            } else {
                                showRequestFriends();
                            }
                            aux = 0;
                        }
                        break;
                }
                return true;
            }
        });

    }

    public void showActualFriends() {
        textActualFriends.setText(Html.fromHtml(getResources().getString(R.string.bold_text_actual_friends), Html.FROM_HTML_MODE_LEGACY));
        textSolicitud.setText(getResources().getString(R.string.text_friendship));
        linearSearch.setVisibility(LinearLayout.GONE);
        linearActual.setVisibility(LinearLayout.VISIBLE);
    }

    public void showRequestFriends() {
        textSolicitud.setText(Html.fromHtml(getResources().getString(R.string.bold_text_friendship), Html.FROM_HTML_MODE_LEGACY));
        textActualFriends.setText(getResources().getString(R.string.text_actual_friends));
        linearSearch.setVisibility(LinearLayout.VISIBLE);
        linearActual.setVisibility(LinearLayout.GONE);
    }

    public void checkUserRequest() {
        sql = new FacadeSQLite(db);
        final FacadeRest facadeRest = new FacadeRest();
        final String userFriend = userFriendTextView.getText().toString();
        final int result;
        final FriendshipActivity activity = this;

        if (userFriend.equals("")) {
            showRequestFriends();
            Toast.makeText(getApplicationContext(), "Introduce un usuario", Toast.LENGTH_SHORT).show();
            result = 1;
        } else {
            result = user.checkCorrectUser(userFriend);
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int resultRest = 1;
                try {
                    resultRest = facadeRest.checkUser(userFriend) ? 0 : 1;
                } catch (Exception e) {
                    Log.e("Connectivity", e.toString());
                }
                if (result == 0 && resultRest == 0) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Peticion enviada", Toast.LENGTH_SHORT).show();
                        }
                    });
                    sql = new FacadeSQLite(db);
                    sql.insertFriend(user, userFriend);
                    try {
                        facadeRest.operationFriend(new Friend(user.getUserName(), userFriend, "request"), "POST");

                    } catch (Exception e) {
                        Log.e("connectivityError", e.toString());
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ya tienes a este usuario como amigo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void resetRecyclerView() {
        RVAdapterFriendsRequest adapter = new RVAdapterFriendsRequest(user, user.getFriendsRequest(), this);
        rvRequest.setAdapter(adapter);
        RVAdapterFriends adapter2 = new RVAdapterFriends(user, user.getFriends(), this);
        rvActual.setAdapter(adapter2);
    }

    @Override
    public void onBackPressed() {
        Intent output = new Intent();
        output.putExtra("user", user);
        setResult(RESULT_OK, output);
        finish();
    }


}
