
package com.kobashin.contentprovidersample.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DeviceManagementDbProvider extends ContentProvider {

    private static final int INDEX_DEVICE_STATES_ALL = 1;
    private static final int INDEX_DEVICE_STATE = 2;

    // uri for accessing a table "device_states"
    public static final Uri DEVICE_STATE_CONTENT_URI = Uri
            .parse("content://com.kobashin.contentprovidersample/device_states");

    // You can access these columns
    public static final class Columns {
        public static final String _ID = "_id";
        public static final String DEVICE = "key";
        public static final String STATE = "state";
    }

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase mDb;

    static {
        sUriMatcher.addURI(DEVICE_STATE_CONTENT_URI.getAuthority(), "device_state",
                INDEX_DEVICE_STATES_ALL);
        sUriMatcher.addURI(DEVICE_STATE_CONTENT_URI.getAuthority(), "device_state/#",
                INDEX_DEVICE_STATE);
    }

    public DeviceManagementDbProvider() {
    }

    /**
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        DeviceManagementDbHelper helper = new DeviceManagementDbHelper(getContext());
        mDb = helper.getWritableDatabase();
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case INDEX_DEVICE_STATES_ALL:
                return mDb.query(DeviceManagementDbHelper.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);

            case INDEX_DEVICE_STATE:
                // _idにマッチングしたquery結果を返す
                return mDb.query(DeviceManagementDbHelper.TABLE_NAME, projection, "_ID = ?",
                        new String[] {
                            uri.getLastPathSegment()
                        }, null, null, sortOrder);

            default:
                break;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
