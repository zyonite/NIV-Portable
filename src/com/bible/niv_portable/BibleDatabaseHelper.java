package com.bible.niv_portable;

import java.io.*;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

@SuppressLint("SdCardPath")
// Will include Bible versions such as KJV and others in the future
public class BibleDatabaseHelper extends SQLiteOpenHelper {
	// destination path (location) of our database on device
	private static String DB_PATH = "";
	private static String DB_NAME = "Bible.db";// Database name
	private SQLiteDatabase mDatabase;
	private final Context mContext;

	private String sqlCommand;
	private String[] sqlParameters;

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

	// Selects book IDs of either Old Testament or New Testament
	ArrayList<String> selectBookTitleIds(String collection) {
		Cursor c = mDatabase.rawQuery(
				"SELECT _id FROM Book WHERE collection = ?",
				new String[] { collection });

		ArrayList<String> bookIdTitles = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			bookIdTitles.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return bookIdTitles;
	}

	// Selects book titles of either Old Testament or New Testament
	ArrayList<String> selectBookTitles(String collection) {
		Cursor c = mDatabase.rawQuery(
				"SELECT name FROM Book WHERE collection = ?",
				new String[] { collection });

		ArrayList<String> bookIdTitles = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			bookIdTitles.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return bookIdTitles;
	}

	// Selects chapter IDs of a book
	ArrayList<String> selectChapterIds(String bookId) {
		Cursor c = mDatabase.rawQuery(
				"SELECT _id FROM Chapter WHERE book_id = ?",
				new String[] { bookId });

		ArrayList<String> chapterIds = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			chapterIds.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return chapterIds;
	}

	// Selects chapter numbers of a book
	ArrayList<String> selectChapterNumbers(String bookId) {
		Cursor c = mDatabase.rawQuery(
				"SELECT chapter_number FROM Chapter WHERE book_id = ?",
				new String[] { bookId });

		ArrayList<String> chapterNumbers = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			chapterNumbers.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return chapterNumbers;
	}

	// Selects chapter names of a book
	ArrayList<String> selectChapterNames(String bookId) {
		Cursor c = mDatabase.rawQuery(
				"SELECT chapter_name FROM Chapter WHERE book_id = ?",
				new String[] { bookId });

		ArrayList<String> chapterNames = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			chapterNames.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return chapterNames;
	}

	// Selects verse IDs of a chapter
	ArrayList<String> selectVerseIds(String chapter_id) {
		Cursor c = mDatabase.rawQuery(
				"SELECT _id FROM Verse WHERE chapter_id = ?",
				new String[] { chapter_id });

		ArrayList<String> verseIds = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			verseIds.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return verseIds;
	}

	// Selects verse numbers of a chapter
	ArrayList<String> selectVerseNumbers(String chapter_id) {
		Cursor c = mDatabase.rawQuery(
				"SELECT verse_number FROM Verse WHERE chapter_id = ?",
				new String[] { chapter_id });

		ArrayList<String> verseNumbers = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			verseNumbers.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return verseNumbers;
	}

	// Selects a range of verses of a chapter
	ArrayList<String> selectVerses(String chapter_id, String startVerseNumber,
			String endVerseNumber) {
		if (startVerseNumber != endVerseNumber) {
			sqlCommand = "SELECT verse_text FROM Verse WHERE chapter_id = ? AND verse_number BETWEEN ? AND ?";
			sqlParameters = new String[] { chapter_id, startVerseNumber,
					endVerseNumber };
		} else {
			sqlCommand = "SELECT verse_text FROM Verse WHERE chapter_id = ? AND verse_number = ?";
			sqlParameters = new String[] { chapter_id, startVerseNumber };
		}

		Cursor c = mDatabase.rawQuery(sqlCommand, sqlParameters);

		ArrayList<String> verses = new ArrayList<String>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			verses.add(c.getString(0));
			c.moveToNext();
		}
		c.close();

		return verses;
	}

	// Search verse by book title, chapter number, and verse number
	String SearchVerse(String bookTitle, String chapterNumber,
			String verseNumber) {
		sqlCommand = "SELECT v.verse_text FROM Book b, Chapter c, Verse v WHERE b.name = ? AND c.chapter_number = ? AND v.verse_number = ? AND b._id = c.book_id AND c._id = v.chapter_id";
		sqlParameters = new String[] { bookTitle, chapterNumber, verseNumber };

		String result = "";

		Cursor c = mDatabase.rawQuery(sqlCommand, sqlParameters);
		c.moveToFirst();
		result = c.getString(0);
		c.close();

		return result;
	}

	// Search book name, chapter number, verse number and verse text based on
	// word/s used
	ArrayList<ArrayList<String>> SearchVerses(String text) {
		sqlCommand = "SELECT b.name, c.chapter_number, v.verse_number, v.verse_text FROM Book b, Chapter c, Verse v WHERE LOWER(v.verse_text) LIKE LOWER(?) AND b._id = c.book_id AND c._id = v.chapter_id";
		String searchTerm = "%" + text + "%";
		sqlParameters = new String[] { searchTerm };

		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

		Cursor c = mDatabase.rawQuery(sqlCommand, sqlParameters);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			ArrayList<String> row = new ArrayList<String>();
			row.add(c.getString(0));
			row.add(c.getString(1));
			row.add(c.getString(2));
			row.add(c.getString(3));
			result.add(row);

			c.moveToNext();
		}
		c.close();

		return result;
	}
}