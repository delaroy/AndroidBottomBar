package com.delaroystudios.androidbottombar.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by delaroy on 9/3/17.
 */

public class FragranceProvider extends ContentProvider {

    public static final String LOG_TAG = FragranceProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the fragrance table */
    private static final int FRAGRANCES = 100;

    /** URI matcher code for the content URI for a single fragrance in the fragrance table */
    private static final int FRAGRANCE_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.fragrance/fragrance" will map to the
        // integer code {@link #fragrance}. This URI is used to provide access to MULTIPLE rows
        // of the fragrance table.
        sUriMatcher.addURI(FragranceContract.CONTENT_AUTHORITY, FragranceContract.PATH_FRAGRANCE, FRAGRANCES);

        // The content URI of the form "content://com.example.android.fragrance/fragrance/#" will map to the
        // integer code {@link #fragrance_ID}. This URI is used to provide access to ONE single row
        // of the fragrance table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.fragrance/fragrance/3" matches, but
        // "content://com.example.android.fragrance/fragrance" (without a number at the end) doesn't match.
        sUriMatcher.addURI(FragranceContract.CONTENT_AUTHORITY, FragranceContract.PATH_FRAGRANCE + "/#", FRAGRANCE_ID);
    }

    /** Database helper object */
    private FragranceDbHelper fragranceDbHelper;

    @Override
    public boolean onCreate() {
        fragranceDbHelper = new FragranceDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = fragranceDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FRAGRANCES:
                // For the fragrance code, query the fragrance table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the fragrance table.
                cursor = database.query(FragranceContract.FragranceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FRAGRANCE_ID:
                // For the fragrance_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.fragrance/fragrance/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = FragranceContract.FragranceEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the fragrance table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(FragranceContract.FragranceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FRAGRANCES:
                return insertFragrance(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertFragrance(Uri uri, ContentValues values) {

        // Get writeable database
        SQLiteDatabase database = fragranceDbHelper.getWritableDatabase();

        // Insert the new cart with the given values
        long id = database.insert(FragranceContract.FragranceEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the fragrance content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }




    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Get access to the database and write URI matching code to recognize a single ite

        // Return the number of tasks deleted
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
