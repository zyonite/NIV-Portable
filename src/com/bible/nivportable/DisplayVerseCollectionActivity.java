package com.bible.nivportable;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DisplayVerseCollectionActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	private static final int MAX_VERSES_PER_PAGE = 3;

	private ArrayList<String[]> groupedVerseNumbers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verse_collection);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);
		bdh.initialiseDatabase();

		Intent currentIntent = getIntent();

		String title = currentIntent.getStringExtra("CHAPTER_NAME") + " "
				+ currentIntent.getStringExtra("CHAPTER_NUMBER");

		setTitle(title);

		final ArrayList<String> verseIds = currentIntent
				.getStringArrayListExtra("VERSE_IDS");
		final ArrayList<String> verseNumbers = currentIntent
				.getStringArrayListExtra("VERSE_NUMBERS");

		groupedVerseNumbers = splitArrayValues(verseNumbers
				.toArray(new String[verseNumbers.size()]));

		ArrayList<String> groupedVerseNumbersDisplay = new ArrayList<String>();

		String displayString = "";

		for (int i = 0; i < groupedVerseNumbers.size(); i++) {
			int verseCount = groupedVerseNumbers.get(i).length;

			// Use format of "x - y" as display string
			if (verseCount >= 3) {
				displayString = groupedVerseNumbers.get(i)[0] + " - "
						+ groupedVerseNumbers.get(i)[MAX_VERSES_PER_PAGE - 1];
			}
			// Use format of "x, y" as display string
			else if (verseCount > 1) {
				displayString = groupedVerseNumbers.get(i)[0] + ","
						+ groupedVerseNumbers.get(i)[1];
			}
			// Use verse number as display string
			else {
				displayString = groupedVerseNumbers.get(i)[0];
			}

			groupedVerseNumbersDisplay.add(displayString);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, groupedVerseNumbersDisplay);

		// final Intent newIntent = new Intent(this,
		// DisplayChapterCollectionActivity.class);

		// 0 1, 1 2, 2 3, etc
		GridView view = (GridView) findViewById(R.id.versecollectionlayout);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				/*
				 * if (bdh.openDataBase()) {
				 * 
				 * ArrayList<String> chapterIds = bdh
				 * .selectChapterIds(bookIds.get(position)); ArrayList<String>
				 * chapterNumbers = bdh
				 * .selectBookTitles(bookIds.get(position));
				 * 
				 * newIntent.putExtra("ACTIVITY_TITLE", ((TextView)
				 * v).getText());
				 * newIntent.putStringArrayListExtra("CHAPTER_IDS", chapterIds);
				 * newIntent.putStringArrayListExtra("CHAPTER_NUMBERS",
				 * chapterNumbers);
				 * 
				 * startActivity(newIntent); }
				 */
			}
		});

		view.refreshDrawableState();
	}

	// Split array elements into groups of amount given by MAX_VERSES_PER_PAGE
	private <T extends Object> ArrayList<T[]> splitArrayValues(T[] array) {
		ArrayList<T[]> groupedList = new ArrayList<T[]>();

		// Repeat grouping by this amount
		int numberOfTimes = array.length / MAX_VERSES_PER_PAGE;

		int lower = 0;
		int upper = 0;

		// Split into groups of default amount until there are less elements
		// than default amount
		for (int i = 0; i < numberOfTimes; i++) {
			upper += MAX_VERSES_PER_PAGE;
			groupedList.add(Arrays.copyOfRange(array, lower, upper));
			lower = upper;
		}

		if (upper < array.length - 1) {
			lower = upper;
			upper = array.length;
			groupedList.add(Arrays.copyOfRange(array, lower, upper));
		}

		return groupedList;
	}

	// Split array elements into groups of amount given by supplied value
	@SuppressWarnings("unused")
	private <T extends Object> ArrayList<T[]> splitArrayValues(T[] array,
			int groupAmount) {
		ArrayList<T[]> groupedList = new ArrayList<T[]>();

		// Repeat grouping by this amount
		int numberOfTimes = array.length / groupAmount;

		int lower = 0;
		int upper = 0;

		// Split into groups of default amount until there are less elements
		// than default amount
		for (int i = 0; i < numberOfTimes; i++) {
			upper += groupAmount;
			groupedList.add(Arrays.copyOfRange(array, lower, upper));
			lower = upper;
		}

		if (upper < array.length - 1) {
			lower = upper;
			upper = array.length;
			groupedList.add(Arrays.copyOfRange(array, lower, upper));
		}

		return groupedList;
	}
}