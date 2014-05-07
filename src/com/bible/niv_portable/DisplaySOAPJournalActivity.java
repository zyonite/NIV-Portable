package com.bible.niv_portable;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplaySOAPJournalActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_soap);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);

		Intent current = getIntent();

		String bookTitle = current.getStringExtra("BOOK_TITLE");
		String chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
		String verseNumber = current.getStringExtra("VERSE_NUMBER");
		String dateCreated = current.getStringExtra("DATE_CREATED");
		String scripture = "";
		String observation = current.getStringExtra("OBSERVATION");
		String application = current.getStringExtra("APPLICATION");
		String prayer = current.getStringExtra("PRAYER");

		String title = getTitle().toString();
		setTitle(title);// + " " + dateCreated);

		if (bdh.openDataBase()) {
			scripture = bdh.SearchVerse(bookTitle, chapterNumber, verseNumber);
			bdh.close();

			TextView scriptureView = (TextView) findViewById(R.id.scripture);
			scriptureView.setText(bookTitle + " " + chapterNumber + ":"
					+ verseNumber + " - " + scripture);

			TextView observationView = (TextView) findViewById(R.id.observation);
			observationView.setText(observation);

			TextView applicationView = (TextView) findViewById(R.id.application);
			applicationView.setText(application);

			TextView prayerView = (TextView) findViewById(R.id.prayer);
			prayerView.setText(prayer);
		}
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