package com.learningmyway.me.workouttracker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_DATE;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_WEIGHT;
import static com.learningmyway.me.workouttracker.WorkoutsContract.strToDate;
import static com.learningmyway.me.workouttracker.WorkoutsDbHelper.getWorkoutOldFirst;

public class ProgressGraphActivity extends AppCompatActivity
{
    public static String workoutExtra = "WORKOUT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_progress_graph);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        populateGraph(getIntent().getStringExtra(workoutExtra));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void populateGraph(String workout)
    {
        Cursor cursor = getWorkoutOldFirst(getApplicationContext(), workout);

        ArrayList<DataPoint> points = new ArrayList<>();
        Date start, last;
        start = null;
        last = new Date();

        while (cursor.moveToNext())
        {
            Date date = last = strToDate(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));
            start = (start == null) ? last : start;
            int weight = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_WEIGHT)));

            points.add(new DataPoint(date, weight));
        }

        LineGraphSeries<DataPoint> data = new LineGraphSeries<>((DataPoint[]) points.toArray(new DataPoint[]{}));

        GraphView graph = (GraphView) findViewById(R.id.graph);

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.addSeries(data);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplication()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        if (start != null)
        {
            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(start.getTime());
            graph.getViewport().setMaxX(last.getTime());
            graph.getViewport().setXAxisBoundsManual(true);
        }
    }
}
