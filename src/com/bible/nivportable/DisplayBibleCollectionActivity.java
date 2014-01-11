package com.bible.nivportable;

import java.util.ArrayList;

import com.bible.nivportable.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class DisplayBibleCollectionActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible_collection);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);
		bdh.initialiseDatabase();

		Intent currentIntent = getIntent();

		String title = currentIntent.getStringExtra("ACTIVITY_TITLE");

		setTitle(title);

		final ArrayList<String> bookIds = currentIntent
				.getStringArrayListExtra("BOOK_IDS");
		final ArrayList<String> bookTitles = currentIntent
				.getStringArrayListExtra("BOOK_TITLES");

		GridView view = (GridView) findViewById(R.id.biblecollectionlayout);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, bookTitles);

		final Intent newIntent = new Intent(this,
				DisplayChapterCollectionActivity.class);

		// 0 1, 1 2, 2 3, etc
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (bdh.openDataBase()) {

					ArrayList<String> chapterIds = bdh
							.selectChapterIds(bookIds.get(position));
					ArrayList<String> chapterNumbers = bdh
							.selectChapterNumbers(bookIds.get(position));

					newIntent.putExtra("ACTIVITY_TITLE",
							((TextView) v).getText());
					newIntent.putStringArrayListExtra("CHAPTER_IDS",
							chapterIds);
					newIntent.putStringArrayListExtra("CHAPTER_NUMBERS",
							chapterNumbers);

					startActivity(newIntent);

					/*
					 * Toast.makeText( getApplicationContext(), ((TextView)
					 * v).getText() + " " + Integer.toString(position) + " " +
					 * bookIds.get(position), Toast.LENGTH_SHORT) .show();
					 */

				}
			}
		});

		view.refreshDrawableState();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main_activity_actions, menu);
		// return super.onCreateOptionsMenu(menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
