package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fhiden on 2017-09-17.
 */

public class SQLHelper {

    public static class Entry {
        public static final String TABLE_NAME = "enteties";
        public static final String COLUMN_NAME_item = "item";
        public static final String COLUMN_NAME_score = "score";
        public static final String COLUMN_NAME_time_added = "time";
    }

    private  static SQLHelper sqlHelper;
    DatabaseHelper dbHelper;
    SQLiteDatabase database;

    private final String[] allColums = {
            Entry.COLUMN_NAME_time_added,
            Entry.COLUMN_NAME_score,
            Entry.COLUMN_NAME_item
    };

    private String primaryKeyLikeQuery = Entry.COLUMN_NAME_time_added + " LIKE ? AND " + Entry.COLUMN_NAME_item + " LIKE ? ";
    private String sortByDate = Entry.COLUMN_NAME_time_added + " ASC";


    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                    Entry.COLUMN_NAME_time_added + " INTEGER, " +
                    Entry.COLUMN_NAME_score + " INTEGER, " +
                    Entry.COLUMN_NAME_item + " TEXT," +
                    "PRIMARY KEY (" + Entry.COLUMN_NAME_time_added + ", " + Entry.COLUMN_NAME_item + "))";

    private final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

    public static SQLHelper getInstance(Context context) {
        return sqlHelper == null ? new SQLHelper(context) :sqlHelper;
    }

    private SQLHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();

        deleteItemsOlderthan30Days(context);
    }

    public void addItem(final Context context, final String item, final int score) {
        database = dbHelper.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(Entry.COLUMN_NAME_item, item);
        values.put(Entry.COLUMN_NAME_score, score);
        values.put(Entry.COLUMN_NAME_time_added, new Date().getTime());

        database.insert(Entry.TABLE_NAME, null, values);
    }

    public int getTotalScore(final Context context){
        String sql = "SELECT "+Entry.COLUMN_NAME_score+" FROM " + Entry.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql,null);
        int total = 0, i= 0;
        while (cursor.moveToNext()) {

            total +=  cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_NAME_score));
            i++;
        }
        Log.v("total "+total, ""+i);
        if (i>0)
            return total/i;
        else
            return 0;
    }

    public void deleteItemsOlderthan30Days(final Context context) {
        String sql = "DELETE FROM " + Entry.TABLE_NAME + " WHERE " + Entry.COLUMN_NAME_time_added + " <= " + (new Date().getTime() - 86400*30);
        database.execSQL(sql);
        Log.v("SQLite", "Removed items older than 24 h.");
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "food.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}
