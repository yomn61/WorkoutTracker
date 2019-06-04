package com.learningmyway.me.workouttracker;

import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /* Inner class that defines the table contents */
    public static class WorkoutEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "WORKOUTS";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_WORKOUT = "Workout";
        public static final String COLUMN_NAME_WEIGHT = "Weight";
    }
}
