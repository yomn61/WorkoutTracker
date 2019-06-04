package com.learningmyway.me.workouttracker;

import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_DATE;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry
        .COLUMN_NAME_MUSCLE_GROUP;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_WEIGHT;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_WORKOUT;

public final class WorkoutsContract
{
    private WorkoutsContract() {}

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String dateToStr(Date date)
    {
        return dateFormat.format(date);
    }

    public static Date strToDate(String date)
    {
        try
        {
            return dateFormat.parse(date);
        }
        catch (ParseException e)
        {
            return new Date();
        }
    }

    public static String selectionConstructor(boolean date, boolean muscleGroup, boolean workout, boolean weight)
    {
        String selection = "";

        if (date)
            selection += COLUMN_NAME_DATE + " = ?";
        if (muscleGroup)
            selection += (selection.equals("") ? "" : " AND ") + COLUMN_NAME_MUSCLE_GROUP + " = ?";
        if (workout)
            selection += (selection.equals("") ? "" : " AND ") + COLUMN_NAME_WORKOUT + " = ?";
        if (weight)
            selection += (selection.equals("") ? "" : " AND ") + COLUMN_NAME_WEIGHT + " = ?";

        return selection;
    }

    /* Inner class that defines the table contents */
    public static class WorkoutEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "WORKOUTS";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_MUSCLE_GROUP = "Muscle_Group";
        public static final String COLUMN_NAME_WORKOUT = "Workout";
        public static final String COLUMN_NAME_WEIGHT = "Weight";
    }
}
