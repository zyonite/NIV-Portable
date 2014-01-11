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

public class DisplayVerseCollectionActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

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

		String title = currentIntent.getStringExtra("CHAPTER_NAME") + " " + currentIntent.getStringExtra("CHAPTER_NUMBER");

		setTitle(title);

		final ArrayList<String> verseIds = currentIntent
				.getStringArrayListExtra("CHAPTER_IDS");
		final ArrayList<String> verseNumbers = currentIntent
				.getStringArrayListExtra("CHAPTER_NUMBERS");

		GridView view = (GridView) findViewById(R.id.chaptercollectionlayout);

		//Groups of 3 test
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, verseNumbers);

		// final Intent newIntent = new Intent(this,
		// DisplayChapterCollectionActivity.class);

		// 0 1, 1 2, 2 3, etc
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
}