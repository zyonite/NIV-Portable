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

		Intent current = getIntent();

		String title = current.getStringExtra("ACTIVITY_TITLE");

		setTitle(title);

		final ArrayList<String> bookIds = current
				.getStringArrayListExtra("BOOK_IDS");
		final ArrayList<String> bookTitles = current
				.getStringArrayListExtra("BOOK_TITLES");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.gridview_button_layout, bookTitles);

		final Intent next = new Intent(this,
				DisplayChapterCollectionActivity.class);

		GridView view = (GridView) findViewById(R.id.biblecollectionlayout);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (bdh.openDataBase()) {
					String bookId = bookIds.get(position);

					ArrayList<String> chapterIds = bdh.selectChapterIds(bookId);
					ArrayList<String> chapterNumbers = bdh
							.selectChapterNumbers(bookId);

					next.putExtra("ACTIVITY_TITLE", ((TextView) v).getText());
					next.putStringArrayListExtra("CHAPTER_IDS", chapterIds);
					next.putStringArrayListExtra("CHAPTER_NUMBERS",
							chapterNumbers);

					startActivity(next);

					bdh.close();
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
