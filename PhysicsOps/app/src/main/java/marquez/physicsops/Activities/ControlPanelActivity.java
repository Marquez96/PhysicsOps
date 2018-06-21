package marquez.physicsops.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.JsonReader;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import marquez.physicsops.BuildConfig;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import marquez.physicsops.Classes.DBSQLite;
import marquez.physicsops.Classes.EventCalendar;
import marquez.physicsops.Classes.FacadeSQLite;
import marquez.physicsops.Classes.PointsGPS;
import marquez.physicsops.Classes.User;
import marquez.physicsops.R;

import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.location.LocationListener;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;

import android.os.Looper;

import android.widget.Toast;


public class ControlPanelActivity extends AbstractActivity {

    FloatingActionButton fab;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ScheduledExecutorService scheduleTaskExecutor;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    Location mCurrentLocation;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private DBSQLite db;
    private FacadeSQLite sql;
    private Calendar actualDate;
    private Calendar previosDate;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    private static final String TAG = ControlPanelActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize the form
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Context context = this.getApplicationContext();
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setComponents(fab, toolbar, drawer, false);
        init();

        roundImage(ResourcesCompat.getDrawable(getResources(), R.drawable.icono3, null), (ImageView) findViewById(R.id.imageView3));
        ((TextView) findViewById(R.id.textViewName)).setText("Nombre: " + super.user.getFullName());
        Calendar cal = Calendar.getInstance();
        ArrayList<EventCalendar> events = user.hasEvent(cal);
        float exerciseToday = 0;
        for (int i = 0; i < events.size(); i++) {
            exerciseToday += events.get(i).getDuration();
        }
        ((TextView) findViewById(R.id.textViewExerciceToday)).setText("Ejercicio en el día de hoy: " + Float.toString(exerciseToday) + " h");
        float exerciseAll = 0;
        for (int i = 0; i < user.getEvents().size(); i++) {
            exerciseAll += user.getEvents().get(i).getDuration();
        }
        ((TextView) findViewById(R.id.textViewExerciceAll)).setText("Total de ejercicio realizado: " + String.format("%.1f", exerciseAll / 24) + " días");
        ((TextView) findViewById(R.id.textViewRequestControl)).setText("Solicitudes de amistad: " + Integer.toString(user.getFriendsRequest().size()));
    }

    @Override
    public void onBackPressed() {
        scheduleTaskExecutor.shutdown();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                showSnackbar(R.string.textwarn, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void init() {
        db = new DBSQLite(getApplicationContext(), "Administration", null, getResources().getInteger(R.integer.versionSQLite));
        sql = new FacadeSQLite(db);
        PointsGPS.processDataBase(sql, user);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        actualDate = Calendar.getInstance();
        previosDate = Calendar.getInstance();
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Log.d("GPS", "GPS");
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    getLastLocation();
                }
            }
        }, 0, 60, TimeUnit.SECONDS);
        createLocationRequest();
        createLocationCallback();
        startLocationUpdates();

    }

    void roundImage(Drawable originalDrawable, ImageView imageView) {
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        if (originalBitmap.getWidth() > originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getHeight(), originalBitmap.getHeight());
        } else if (originalBitmap.getWidth() < originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getWidth());
        }
        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getWidth());
        imageView.setImageDrawable(roundedDrawable);
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(ControlPanelActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "dont have permissions", Toast.LENGTH_SHORT).show();
            requestPermissions();
        } else {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    try {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            Log.d("GPS", Double.toString(mLastLocation.getLatitude()));
                            Log.d("GPS", Double.toString(mLastLocation.getAltitude()));
                            sql.insertPoint(mLastLocation, user);
                            if (previosDate.get(Calendar.DAY_OF_MONTH) != actualDate.get(Calendar.DAY_OF_MONTH)) {
                                PointsGPS points = sql.getPointsUser(user, true);
                                EventCalendar eventDoIt = points.getEventFromPoints();
                                sql.insertActivity(user, eventDoIt);
                                user.addEvent(eventDoIt);
                            }
                        } else {
                            Log.d("GPS", "dont return pos");
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };
    }

    private void startLocationUpdates() {
        final Activity activity = this;
            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            Log.i(TAG, "All location settings are satisfied.");


                            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());

                        }
                    });

    }

}
