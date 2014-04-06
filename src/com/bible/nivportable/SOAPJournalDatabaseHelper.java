package com.bible.nivportable;

import java.io.*;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.widget.Toast;

@SuppressLint("SdCardPath")
// Will include Bible versions such as KJV and others in the future
public class SOAPJournalDatabaseHelper extends SQLiteOpenHelper {
	// destination path (location) of our database on device
	private static String DB_PATH = "";
	private static String DB_NAME = "SOAP.db";// Database name
	private SQLiteDatabase mDatabase;
	private final Context mContext;

	private String sqlCommand;
	private String[] sqlParameters;

	// 1 for NIV, different number for other Bible versions
	public SOAPJournalDatabaseHelper(Context context) {
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

	// Search all existing SOAP journal entries
	ArrayList<ArrayList<String>> SearchSOAPJournals() {
		sqlCommand = "SELECT book_name, chapter_number, verse_number, date(date_created) FROM SOAP";

		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

		Cursor c = mDatabase.rawQuery(sqlCommand, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ArrayList<String> row = new ArrayList<String>();
			// Book name
			row.add(c.getString(0));
			// Chapter number
			row.add(c.getString(1));
			// Verse number
			row.add(c.getString(2));

			// Date string - Format is YYYY-MM-DD, change to DD/MM/YYYY
			String date = c.getString(3);
			String[] dateSplit = date.split("-");
			String modifiedDate = dateSplit[2] + "/" + dateSplit[1] + "/"
					+ dateSplit[0];
			row.add(modifiedDate);

			result.add(row);

			c.moveToNext();
		}
		c.close();
		return result;
	}

	String SaveSOAPJournal(String bookName, String chapterNumber,
			String verseNumber, String observation, String application,
			String prayer) {
		sqlCommand = "INSERT INTO SOAP (book_name, chapter_number, verse_number, observation, application, prayer) VALUES (?, ?, ?, ?, ?, ?, ?)";
		sqlParameters = new String[] { bookName, chapterNumber, verseNumber,
				observation, application, prayer };

		try
		{
			mDatabase.rawQuery(sqlCommand, sqlParameters);
			return "";
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
	}
}