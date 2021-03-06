package com.sm.app.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sm.app.utils.Constant;

/**
 *  Class that provide SQLite helper.
 */
public class GeofenceSQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE               = " TEXT";
    private static final String INTEGER_DEFAULT_TYPE    = " INTEGER DEFAULT 0";
    private static final String COMMA_SEP               = ",";
    private static final String SQL_DELETE_ENTRIES      = "DROP TABLE IF EXISTS " + FenceEntrySQLiteDb.TABLE_NAME;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FenceEntrySQLiteDb.TABLE_NAME + " (" +
                    FenceEntrySQLiteDb._ID + " INTEGER PRIMARY KEY," +
                    FenceEntrySQLiteDb.COLUMN_FENCE_NAME + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_CITY + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_PROVINCE + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_LAT + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_LNG + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_RANGE + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_ACTIVE + INTEGER_DEFAULT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_MATCH + INTEGER_DEFAULT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_SMS_TEXT + TEXT_TYPE + COMMA_SEP +
                    FenceEntrySQLiteDb.COLUMN_FENCE_EVENT + INTEGER_DEFAULT_TYPE +  " );";

    /* Creation of DB, first time */
    public GeofenceSQLiteHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    /* Creation of table */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}