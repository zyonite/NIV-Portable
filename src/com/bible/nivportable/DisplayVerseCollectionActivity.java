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

		Intent current = getIntent();

		final String chapterId = current.getStringExtra("CHAPTER_ID");
		final String chapterName = current.getStringExtra("CHAPTER_NAME");
		final String chapterNumber = current
				.getStringExtra("CHAPTER_NUMBER");
		String title = chapterName + " " + chapterNumber;

		setTitle(title);

		final ArrayList<String> verseNumbers = current
				.getStringArrayListExtra("VERSE_NUMBERS");
		groupedVerseNumbers = splitArrayValues(verseNumbers
				.toArray(new String[verseNumbers.size()]));

		String displayString = "";
		final ArrayList<String> groupedVerseNumbersDisplay = new ArrayList<String>();
		for (int i = 0; i < groupedVerseNumbers.size(); i++) {
			String[] verseNumbersArray = groupedVerseNumbers.get(i);
			int verseCount = verseNumbersArray.length;

			// Use format of "x - y" as display string
			/*if (verseCount >= 3) {
				displayString = verseNumbersArray[0] + " - "
						+ verseNumbersArray[verseNumbersArray.length - 1];
			}
			// Use format of "x, y" as display string
			else if (verseCount > 1) {
				displayString = verseNumbersArray[0] + ", "
						+ verseNumbersArray[1];
			}
			// Use verse number as display string
			else {
				displayString = verseNumbersArray[0];
			}*/
			
			displayString = verseNumbersArray[0];
			for (int j = 1; j < verseCount; j++)
			{
				displayString += ", " + verseNumbersArray[j];
			}

			groupedVerseNumbersDisplay.add(displayString);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.gridview_button_layout, groupedVerseNumbersDisplay);

		final Intent next = new Intent(this, DisplayBibleVersesActivity.class);

		// 0 1, 1 2, 2 3, etc
		GridView view = (GridView) findViewById(R.id.versecollectionlayout);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (bdh.openDataBase()) {
					String[] versesArray = groupedVerseNumbers.get(position);
					//String verseNumbersDisplay = groupedVerseNumbersDisplay
					//		.get(position);
					ArrayList<String> verses = bdh
							.selectVerses(chapterId, versesArray[0],
									versesArray[versesArray.length - 1]);

					next.putExtra("CHAPTER_NAME", chapterName);
					next.putExtra("CHAPTER_NUMBER", chapterNumber);
					next.putExtra("VERSE_NUMBERS", versesArray);
					next.putExtra("BIBLE_VERSES", verses);

					startActivity(next);
				}
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