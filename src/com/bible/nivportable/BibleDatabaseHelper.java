package com.bible.nivportable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

@SuppressLint("SdCardPath")
public class BibleDatabaseHelper extends SQLiteOpenHelper {
	// destination path (location) of our database on device
	private static String DB_PATH = "";
	private static String DB_NAME = "Bible.db";// Database name
	private SQLiteDatabase mDatabase;
	private final Context mContext;

	// 1 for NIV, different number for other Bible versions
	public BibleDatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);// 1? its Database Version

		if (android.os.Build.VERSION.SDK_INT >= 4.2) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}

		this.mContext = context;
	}

	public void initialiseDatabase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		if (!dbFile.exists()) {
			try {
				// Copy the database from assets
				SQLiteDatabase db = this.getWritableDatabase();
				db.close();
				copyDataBase();
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	// Copy the database from assets
	private void copyDataBase() throws IOException {
		InputStream mInput = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	// Open the database, so we can query it
	public boolean openDataBase() throws SQLException {
		String mPath = DB_PATH + DB_NAME;
		// Log.v("mPath", mPath);
		mDatabase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDatabase = SQLiteDatabase.openDatabase(mPath, null,
		// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return mDatabase != null;
	}

	public void SelectOldTestament() {
		if (mDatabase != null) {

		}
	}

	@Override
	public synchronized void close() {
		if (mDatabase != null)
			mDatabase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	// SQL Select Book Titles by Old Testament (1) or New Testament (2)
	// Extension will include Bible version
	public HashMap<String, String> selectBookTitles(int collection) {
		Cursor c = mDatabase.rawQuery(
				"SELECT _id, name FROM Book WHERE collection = ?",
				new String[] { Integer.toString(collection) });

		HashMap<String, String> bookIdTitles = new HashMap<String, String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			bookIdTitles.put(c.getString(0), c.getString(1));
			c.moveToNext();
		}
		c.close();

		return bookIdTitles;
	}
}