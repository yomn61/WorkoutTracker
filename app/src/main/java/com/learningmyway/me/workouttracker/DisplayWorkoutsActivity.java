package com.learningmyway.me.workouttracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.learningmyway.me.workouttracker.ViewConstructionHelper.makeTextView;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_DATE;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_WEIGHT;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_WORKOUT;
import static com.learningmyway.me.workouttracker.WorkoutsContract.strToDate;
import static com.learningmyway.me.workouttracker.WorkoutsDbHelper.getAll;
import static com.learningmyway.me.workouttracker.WorkoutsDbHelper.removeWorkout;

public class DisplayWorkoutsActivity extends AppCompatActivity
{
    private final static String TAG = DisplayWorkoutsActivity.class.toString();

    private void addCheckBoxesButton()
    {
        String buttonText = "Add CheckBoxes";
        Button b = (Button) findViewById(R.id.addRemoveCheckButton);
        b.setText(buttonText);
    }

    // Helper function to help generate individual rows for the table
    private TableRow makeTableRow(List<String> params, boolean bold)
    {
        TableRow tr = new TableRow(getApplicationContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < params.size(); i++)
        {
            tr.addView(makeTextView(getApplicationContext(), params.get(i), bold));
        }

        return tr;
    }

    // Helper function to generate the Table view
    private void makeTable()
    {
        Cursor cursor = getAll(getApplicationContext());

        // Building the Table
        TableLayout tl = (TableLayout) findViewById(R.id.tl);
        Log.d(TAG, Integer.toString(tl.getChildCount()));
        // Column headers
        tl.addView(makeTableRow(new ArrayList<>(Arrays.asList("Date", "Workout", "Weight")), true));



        int numRows = 0;
        // Adding all of the rows from the database
        while (cursor.moveToNext())
        {
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));
            String workout = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_WORKOUT));
            String weight = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_WEIGHT));

            tl.addView(makeTableRow(new ArrayList<>(Arrays.asList(date, workout, weight)), false));
            numRows++;
        }

        Log.d(TAG, "Number of rows put: " + Integer.toString(tl.getChildCount()));

        cursor.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workouts);

        // Set the top button
        addCheckBoxesButton();

        makeTable();
    }

    // Adds checkboxes to each of the rows
    // Toggles the button onClickListener from addCheckBoxes() to removeCheckBoxes()
    public void addCheckBoxes(View view)
    {
        TableLayout tl = (TableLayout) findViewById(R.id.tl);

        String buttonDescripts = "Check to Remove";
        TextView textView = new TextView(getApplicationContext());
        textView.setText(buttonDescripts);
        textView.setTypeface(null, Typeface.BOLD);
        ((TableRow) tl.getChildAt(0)).addView(textView, 0);

        Log.d(TAG, Integer.toString(tl.getChildCount()));

        for (int i = 1, rowCount = tl.getChildCount(); i < rowCount; i++)
        {
            View view1 = tl.getChildAt(i);

            Log.d(TAG, String.valueOf(((TableRow) view1).getChildCount()));

            if (!(view1 instanceof TableRow)) continue;

            CheckBox cb = new CheckBox(this);
            ((TableRow) view1).addView(cb, 0);
        }

        // Changing the Button View and OnClick
        String buttonStr = "Remove Workouts";
        Button button = findViewById(R.id.addRemoveCheckButton);
        button.setText(buttonStr);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                removeCheckBoxes(v);
            }
        });
    }

    // Adds checkboxes to each of the rows
    // Toggles the button onClickListener from removeCheckBoxes() to addCheckBoxes()
    public void removeCheckBoxes(View view)
    {
        TableLayout tl = (TableLayout) findViewById(R.id.tl);

        for (int i = tl.getChildCount() - 1; i >= 1; i--)
        {
            View view1 = tl.getChildAt(i);

            // Needs to be a TableRow
            if (!(view1 instanceof TableRow)) continue;

            CheckBox cb = (CheckBox) ((TableRow) view1).getChildAt(0);
            if (cb.isChecked())
            {
                Date date = strToDate(((TextView) ((TableRow) view1).getChildAt(1)).getText().toString());
                String workout = ((TextView) ((TableRow) view1).getChildAt(2)).getText().toString();
                int weight = Integer.parseInt(((TextView) ((TableRow) view1).getChildAt(3)).getText().toString());

                removeWorkout(getApplicationContext(), date, workout, weight);
                tl.removeViewAt(i);
            }
            else
            {
                ((TableRow) view1).removeViewAt(0);
            }
        }

        ((TableRow) tl.getChildAt(0)).removeViewAt(0);

        // Changing the Button View and OnClick
        String buttonStr = "Add CheckBoxes";
        Button button = findViewById(R.id.addRemoveCheckButton);
        button.setText(buttonStr);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addCheckBoxes(v);
            }
        });
    }
}
