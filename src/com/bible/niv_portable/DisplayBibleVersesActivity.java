package com.bible.niv_portable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//Add more functionalities to this in the future
public class DisplayBibleVersesActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible_verses);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		Intent current = getIntent();

		final String bookTitle = current.getStringExtra("BOOK_TITLE");
		final String chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
		final String[] verseNumbers = current.getStringArrayExtra("VERSE_NUMBERS");
		String title = bookTitle + " " + chapterNumber + ":";

		int length = verseNumbers.length;

		if (length == 1) {
			title += verseNumbers[0];
		} else {
			title += verseNumbers[0] + " - " + verseNumbers[length - 1];
		}

		setTitle(title);

		ArrayList<String> verses = current
				.getStringArrayListExtra("BIBLE_VERSES");

		// Set up adapter for ListVie
		ListView listView = (ListView) findViewById(R.id.bibleverseslayout);

		String[] verseResults = new String[verses.size()];

		for (int i = 0; i < verses.size(); i++) {
			verseResults[i] = verseNumbers[i] + " - " + verses.get(i);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, verseResults);
		listView.setAdapter(adapter);

		final Intent next = new Intent(this, CreateSOAPJournalActivity.class);// Change

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				next.putExtra("BOOK_TITLE", bookTitle);
				next.putExtra("CHAPTER_NUMBER", chapterNumber);
				next.putExtra("VERSE_NUMBER", verseNumbers[position]);
				startActivity(next);
			}
		});

		// TODO: Generate vertical navigation drag here
		listView.refreshDrawableState();
	}
}