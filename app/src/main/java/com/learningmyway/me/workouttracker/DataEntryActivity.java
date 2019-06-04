package com.learningmyway.me.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

import static com.learningmyway.me.workouttracker.WorkoutsDbHelper.addWorkout;

public class DataEntryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Set the spinner to have the values from the workout list
        Spinner workout_spinner = (Spinner) findViewById(R.id.workout_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                                                             R.array.workouts,
                                                                             android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        workout_spinner.setAdapter(adapter);

        // Make the calendar specify what date it is on
        CalendarView calendarView = (CalendarView) findViewById(R.id.workout_date);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                view.setDate(c.getTime().getTime());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // If view_logs, then show the logs
            case R.id.view_logs:
                viewLogs();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Add workout to the table
    public void addCurrentWorkout(View view)
    {
        EditText editText = (EditText) findViewById(R.id.weight_no);

        // Do not add workout if no value is specified
        if (editText.getText().toString().equals(""))
            return;

        // Get the specified weight or reps
        int weight = Integer.parseInt(editText.getText().toString());

        // Get the specified workout
        Spinner workoutSpinner = (Spinner) findViewById(R.id.workout_spinner);
        String workout = workoutSpinner.getSelectedItem().toString();

        // Get the date specified
        CalendarView calendarView = (CalendarView) findViewById(R.id.workout_date);
        Date date = new Date(calendarView.getDate());

        // Add workout to the database
        addWorkout(getApplicationContext(), date, workout, weight);
    }

    // Change the screen to the logs
    private void viewLogs()
    {
        Intent intent = new Intent(this, DisplayWorkoutsActivity.class);
        startActivity(intent);
    }

    public void setToToday(View view)
    {
        CalendarView calendarView = (CalendarView) findViewById(R.id.workout_date);
        calendarView.setDate(Calendar.getInstance().getTime().getTime());
    }
}
