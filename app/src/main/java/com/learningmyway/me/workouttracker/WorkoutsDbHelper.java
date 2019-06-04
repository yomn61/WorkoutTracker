package com.learningmyway.me.workouttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

import static android.provider.BaseColumns._ID;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_DATE;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry
        .COLUMN_NAME_MUSCLE_GROUP;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_WEIGHT;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.COLUMN_NAME_WORKOUT;
import static com.learningmyway.me.workouttracker.WorkoutsContract.WorkoutEntry.TABLE_NAME;
import static com.learningmyway.me.workouttracker.WorkoutsContract.dateToStr;

public class WorkoutsDbHelper extends SQLiteOpenHelper
{
    private final static String TAG = WorkoutsDbHelper.class.toString();

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = TABLE_NAME + ".db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID +
            " INTEGER PRIMARY KEY," + COLUMN_NAME_DATE + " TEXT," + COLUMN_NAME_MUSCLE_GROUP + " TEXT,"
            + COLUMN_NAME_WORKOUT + " TEXT," + COLUMN_NAME_WEIGHT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static WorkoutsDbHelper ourInstance = null;

    public static WorkoutsDbHelper getInstance(Context context)
    {
        if (ourInstance == null)
            ourInstance = new WorkoutsDbHelper(context);

        return ourInstance;
    }

    private WorkoutsDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void addWorkout(Context context, Date date, String muscle_group, String workout, int weight)
    {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put(COLUMN_NAME_DATE, dateToStr(date));
        vals.put(COLUMN_NAME_MUSCLE_GROUP, muscle_group);
        vals.put(COLUMN_NAME_WORKOUT, workout);
        vals.put(COLUMN_NAME_WEIGHT, weight);

        // Check to see if this entry exists, if does, do not add
        String selection = COLUMN_NAME_DATE + " = ? AND " + COLUMN_NAME_WORKOUT + " = ? AND " +
                COLUMN_NAME_WEIGHT + " = ?";
        String sortOrder = COLUMN_NAME_DATE + " DESC";
        String[] selectionArgs = {dateToStr(date), workout, Integer.toString(weight)};
        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        // If our table has the value, do not add
        if (cursor.getCount() > 0)
            return;

        long newRowId = db.insert(TABLE_NAME, null, vals);

        if (newRowId == -1)
        {
            /// TODO: Add stuff
        }
    }

    public static void removeWorkout(Context context, Date date, String workout, int weight)
    {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();

        // Define 'where' part of query.
        String selection = COLUMN_NAME_DATE + " = ? AND " + COLUMN_NAME_WORKOUT + " = ? AND " +
                COLUMN_NAME_WEIGHT + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {dateToStr(date), workout, Integer.toString(weight)};
        Log.d(TAG, "removeWorkout: " + selection);
        // Issue SQL statement.
        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public static Cursor getAll(Context context)
    {
        String sortOrder = COLUMN_NAME_DATE + " DESC";
        return getAll(context, sortOrder);
    }

    public static Cursor getAll(Context context, String sortOrder)
    {
        SQLiteDatabase db = getInstance(context).getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,             // The table to query
                                 null,           // The array of columns to return (pass null to
                                 // get all)
                                 null,              // The columns for the WHERE clause
                                 null,          // The values for the WHERE clause
                                 null,                  // don't group the rows
                                 null,                   // don't filter by row groups
                                 sortOrder               // The sort order
        );

        return cursor;
    }
}
