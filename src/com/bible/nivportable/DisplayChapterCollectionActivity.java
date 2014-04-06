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

		Intent current = getIntent();

		final String title = current.getStringExtra("ACTIVITY_TITLE");

		setTitle(title);

		final ArrayList<String> chapterIds = current
				.getStringArrayListExtra("CHAPTER_IDS");
		final ArrayList<String> chapterNumbers = current
				.getStringArrayListExtra("CHAPTER_NUMBERS");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.gridview_button_layout, chapterNumbers);

		final Intent next = new Intent(this,
				DisplayVerseCollectionActivity.class);

		GridView view = (GridView) findViewById(R.id.chaptercollectionlayout);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (bdh.openDataBase()) {
					String chapterId = chapterIds.get(position);
					String chapterNumber = chapterNumbers.get(position);

					ArrayList<String> verseNumbers = bdh
							.selectVerseNumbers(chapterId);
					bdh.close();

					next.putExtra("CHAPTER_ID", chapterId);
					next.putExtra("CHAPTER_NAME", title);
					next.putExtra("CHAPTER_NUMBER", chapterNumber);
					next.putStringArrayListExtra("VERSE_NUMBERS", verseNumbers);

					startActivity(next);
				}
			}
		});

		view.refreshDrawableState();
	}
}