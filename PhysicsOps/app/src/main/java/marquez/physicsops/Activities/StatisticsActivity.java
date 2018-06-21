package marquez.physicsops.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Calendar;

import android.widget.LinearLayout;
import android.widget.PopupMenu;

import marquez.physicsops.Classes.AxisValuesFormatter;
import marquez.physicsops.Classes.DayAxisValueFormatter;
import marquez.physicsops.Classes.EventCalendar;
import marquez.physicsops.Classes.LayoutDay;
import marquez.physicsops.Classes.XYMarkerView;
import marquez.physicsops.R;

import android.widget.TextView;
import android.widget.Toast;
import android.view.View.*;

public class StatisticsActivity extends AbstractActivity {

    ActionBarDrawerToggle toggle;
    BarChart barChart;
    Button optionsButton;
    FloatingActionButton fab;
    String optionMenuSelected = "Duracion";
    LinearLayout layoutStats, layoutText;
    int year;
    float firstTouchX;
    int aux = 0;
    TextView textYearStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        year = Calendar.getInstance().get(Calendar.YEAR);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        layoutStats = (LinearLayout) findViewById(R.id.linearStats);
        layoutText = (LinearLayout) findViewById(R.id.linearTextStats);
        optionsButton = (Button) findViewById(R.id.buttonOptionsStats);
        textYearStats = (TextView) findViewById(R.id.textYearStats);
        setComponents(fab, toolbar,drawer,true);

        textYearStats.setText(Integer.toString(year) + " - " + optionMenuSelected);
        barChart = (BarChart) findViewById(R.id.bargraph);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(12); //Can be 7
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new AxisValuesFormatter("H");


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv);
        setData(12, optionMenuSelected, year);

        optionsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(StatisticsActivity.this, optionsButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_stats, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(final MenuItem item) {
                        if(!item.getTitle().equals(optionMenuSelected)){
                            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.scale_down);
                            layoutStats.startAnimation(animation);
                            textYearStats.startAnimation(animation);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setData(12, item.getTitle().toString(), year);
                                    optionMenuSelected = item.getTitle().toString();
                                    barChart.invalidate();
                                    textYearStats.setText(Integer.toString(year) + " - " + optionMenuSelected);
                                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                                    layoutStats.startAnimation(animation);
                                    textYearStats.startAnimation(animation);
                                }
                            }, 290);

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        layoutText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        firstTouchX = event.getX();
                        aux = 1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Toast.makeText(getApplicationContext(), Float.toString(Math.abs(firstTouchX - event.getX())), Toast.LENGTH_SHORT).show();
                        if (Math.abs(firstTouchX - event.getX()) > 5 && aux == 1) {
                            final Animation animation1 = AnimationUtils.loadAnimation(v.getContext(), R.anim.scale_down);
                            final Animation animation2 = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                            if (firstTouchX > event.getX()) {
                                year++;
                            } else {
                                year--;
                            }
                            textYearStats.startAnimation(animation1);
                            layoutStats.startAnimation(animation1);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setData(12, optionMenuSelected, year);
                                    barChart.invalidate();
                                    layoutStats.startAnimation(animation2);
                                    textYearStats.setText(Integer.toString(year) + " - " + optionMenuSelected);
                                    textYearStats.startAnimation(animation2);
                                }
                            }, 290);

                            aux=0;
                        }
                        break;
                }
                return true;
            }
        });

    }

    private void setData(int count, String type, int year) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        ArrayList<Float> values;
        IAxisValueFormatter axisValue;
        if(type.equals("Duracion")) {
            values = user.getDurationArrayPerMonth(year);
            axisValue = new AxisValuesFormatter("H");
        } else if (type.equals("Importancia")){
            values = user.getImportanceArrayPerMonth(year);
            axisValue = new AxisValuesFormatter("I");
        } else {
            values = user.getNumArrayPerMonth(year);
            axisValue = new AxisValuesFormatter("N");
        }
        barChart.getAxisLeft().setValueFormatter(axisValue);
        barChart.getAxisRight().setValueFormatter(axisValue);

        for (int i = (int) start; i < start + count; i++) {
            yVals1.add(new BarEntry(i, values.get(i-1)));
        }

        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Year "+ Integer.toString(year));
            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            barChart.setData(data);
        }
    }

}
