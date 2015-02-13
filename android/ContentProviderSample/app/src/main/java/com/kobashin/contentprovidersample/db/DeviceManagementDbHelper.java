
package com.kobashin.contentprovidersample.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by skobayashi on 6/13/14.
 */
public class DeviceManagementDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "devicemanagement.db";
    public static final String TABLE_NAME = "device_states";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEY_DEVICE_WHAT = "key";
    public static final String COLUMN_KEY_DEVICE_STATE = "state";

    private static final String SQL_CREATE = "create table %s (%s integer primary key, %s text not null, %s integer)";

    private Context mContext;

    public DeviceManagementDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        mContext = context;
    }

    private void initDb(SQLiteDatabase db, int userId) {
        ContentValues values = new ContentValues();
        String tablename = String.format(TABLE_NAME, Integer.toString(userId));
        values.put(COLUMN_KEY_DEVICE_WHAT, "wwan");
        values.put(COLUMN_KEY_DEVICE_STATE, 0);
        db.insert(tablename, null, values);

        values.clear();
        values.put(COLUMN_KEY_DEVICE_WHAT, "gps");
        values.put(COLUMN_KEY_DEVICE_STATE, 0);
        db.insert(tablename, null, values);

        values.clear();
        values.put(COLUMN_KEY_DEVICE_WHAT, "camera");
        values.put(COLUMN_KEY_DEVICE_STATE, 1);
        db.insert(tablename, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDbTable(db, 0);
    }

    public void createDbTable(SQLiteDatabase db, int userId) {
        db.beginTransaction();
        try {
            String tablename = String.format(TABLE_NAME, Integer.toString(userId));
            String createCmd = String.format(SQL_CREATE, tablename, COLUMN_ID,
                    COLUMN_KEY_DEVICE_WHAT, COLUMN_KEY_DEVICE_STATE);
            db.execSQL(createCmd);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        initDb(db, userId);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public synchronized void upateDbValue(String what, int state) {
        Log.i("koba", "what=" + what + " state=" + state);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY_DEVICE_WHAT, what);
        values.put(COLUMN_KEY_DEVICE_STATE, state);

        Log.i("koba", "content value : " + values.toString());
        String tablename = String.format(TABLE_NAME);
        db.update(tablename, values, COLUMN_KEY_DEVICE_WHAT + "=?", new String[] {
            what
        });
        db.close();
    }
}
