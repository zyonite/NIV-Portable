package com.bible.nivportable;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//Add more functionalities to this in the future
public class DisplayBibleVersesActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	private final float verseTextSizeSp = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible_verses);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);

		Intent current = getIntent();

		String chapterName = current.getStringExtra("CHAPTER_NAME");
		String chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
		String[] verseNumbers = current.getStringArrayExtra("VERSE_NUMBERS");
		String title = chapterName + " " + chapterNumber + ":";

		int length = verseNumbers.length;

		if (length == 1) {
			title += verseNumbers[0];
		} else {
			title += verseNumbers[0] + " - " + verseNumbers[length - 1];
		}

		setTitle(title);

		ArrayList<String> verses = current
				.getStringArrayListExtra("BIBLE_VERSES");

		//Set up adapter for ListVie
		ListView listView = (ListView) findViewById(R.id.bibleverseslayout);
		
		String[] verseResults = new String[verses.size()];

		for (int i = 0; i < verses.size(); i++) {
			verseResults[i] = verseNumbers[i] + " - " + verses.get(i);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, verseResults);
		listView.setAdapter(adapter);

		//TODO: Generate vertical navigation drag here 
		listView.refreshDrawableState();
	}
}