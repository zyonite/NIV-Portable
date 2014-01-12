package com.bible.nivportable;

import java.util.ArrayList;

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

public class DisplayChapterCollectionActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter_collection);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);
		bdh.initialiseDatabase();

		Intent currentIntent = getIntent();

		final String title = currentIntent.getStringExtra("ACTIVITY_TITLE");

		setTitle(title);

		final ArrayList<String> chapterIds = currentIntent
				.getStringArrayListExtra("CHAPTER_IDS");
		final ArrayList<String> chapterNumbers = currentIntent
				.getStringArrayListExtra("CHAPTER_NUMBERS");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, chapterNumbers);

		final Intent newIntent = new Intent(this,
				DisplayVerseCollectionActivity.class);

		GridView view = (GridView) findViewById(R.id.chaptercollectionlayout);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (bdh.openDataBase()) {

					ArrayList<String> verseIds = bdh.selectVerseIds(chapterIds
							.get(position));
					ArrayList<String> verseNumbers = bdh
							.selectVerseNumbers(chapterNumbers.get(position));

					newIntent.putExtra("CHAPTER_NAME", title);

					newIntent.putExtra("CHAPTER_NUMBER",
							((TextView) v).getText());

					newIntent.putStringArrayListExtra("VERSE_IDS", verseIds);
					newIntent.putStringArrayListExtra("VERSE_NUMBERS",
							verseNumbers);

					startActivity(newIntent);
				}

			}
		});

		view.refreshDrawableState();
	}
}