package com.nookdevs.twook.utilities;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DBhelper {
    /*LOG COMMENT  private static final String TAG = DBhelper.class.getName();  LOG COMMENT*/
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "username";
    public static final String KEY_IMG = "icon";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "twookdb";
    private static final int DATABASE_VERSION = 1;

    private static final String ICONS_TABLE = "icons";

    private static final String CREATE_ICONS_TABLE = "create table if not exists "
	    + ICONS_TABLE
	    + " ("
	    + KEY_ID
	    + " integer primary key autoincrement, "
	    + KEY_IMG
	    + " blob not null, " + KEY_NAME + " text not null unique );";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
	DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
	    try {
		db.execSQL(CREATE_ICONS_TABLE);
		 /*LOG COMMENT  Log.d(TAG, "Database created");  LOG COMMENT*/
	    } catch (SQLException excep) {
		/*LOG COMMENT  Log.e(TAG, excep.getMessage());  LOG COMMENT*/
	    }
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    try {
		db.execSQL("DROP TABLE IF EXISTS " + ICONS_TABLE);
	    } catch (SQLException excep) {
		/*LOG COMMENT  Log.e(TAG, excep.getMessage());  LOG COMMENT*/
	    }
	    onCreate(db);
	}
	
    }

    public void Reset() {
	mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DBhelper(Context ctx) {
	mCtx = ctx;
	mDbHelper = new DatabaseHelper(mCtx);
    }

    public DBhelper open() throws SQLException {
	mDb = mDbHelper.getWritableDatabase();
	return this;
    }

    public void close() {
	mDbHelper.close();
    }

    public void createRowEntry(Row row) {
	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    row.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
	    ContentValues cv = new ContentValues();
	    cv.put(KEY_IMG, out.toByteArray());
	    cv.put(KEY_NAME, row.getName());
	    mDb.insert(ICONS_TABLE, null, cv);
	} catch (SQLException excep) {
	    /*LOG COMMENT  Log.e(TAG, excep.getMessage());  LOG COMMENT*/
	}
    }

    public Row getFirstRowFromDB(String username) {
	Cursor cur = null;
	try {
		/*LOG COMMENT  Log.d(TAG, "Opened database");  LOG COMMENT*/
//
//	    cur = mDb.query(true, ICONS_TABLE,
//		    new String[] {KEY_IMG}, "username="
//			    + username , null, null, null, null, "1");

		/*LOG COMMENT  Log.d(TAG, "Got cursor");  LOG COMMENT*/

	    if (cur.moveToFirst()) {
		/*LOG COMMENT  Log.d(TAG, "Move to first entry");  LOG COMMENT*/

		byte[] blob = cur.getBlob(cur.getColumnIndex(KEY_IMG));
		Bitmap bmp = BitmapFactory
			.decodeByteArray(blob, 0, blob.length);
		String name = cur.getString(cur.getColumnIndex(KEY_NAME));
		cur.close();
		/*LOG COMMENT  Log.d(TAG, "Closed cursor");  LOG COMMENT*/

		return new Row(bmp, name);
	    }
	} catch (Exception excep) {
	    /*LOG COMMENT  Log.e(TAG, excep.getMessage());  LOG COMMENT*/
	} 
	finally {
	    cur.close();
	}
	return null;

    }

    public Bitmap getImage(String username) {
	/*LOG COMMENT  Log.d(TAG, "Opened database");  LOG COMMENT*/
	Row row = getFirstRowFromDB(username);
	/*LOG COMMENT  Log.d(TAG, "Closed database");  LOG COMMENT*/
	return row.getBitmap();
    }

    public void insertImage(String username, Bitmap icon) {

	// FIXME check if the image has changed
	Row row = new Row(icon, username);
	if (getFirstRowFromDB(username) == null) {
	    createRowEntry(row);
	}

    }
}