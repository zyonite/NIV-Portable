package com.bible.nivportable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CreateSOAPJournalActivity extends Activity {

	private BibleDatabaseHelper bdh = null;
	private SOAPJournalDatabaseHelper sdh = null;
	private Context context;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_soap);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);
		sdh = new SOAPJournalDatabaseHelper(this);
		context = getApplicationContext();

		Intent current = getIntent();
		
		String bookTitle = current.getStringExtra("BOOK_TITLE");
		String chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
		String verseNumber = current.getStringExtra("VERSE_NUMBER");
		//String "BOOK_TITLE" "CHAPTER_NUMBER" "VERSE_NUMBER"
		
		String scripture = "";
		
		if (bdh.openDataBase())
		{
			scripture = bdh.SearchVerse(bookTitle, chapterNumber, verseNumber);
			bdh.close();
			
			TextView view = (TextView) findViewById(R.id.scripture);
			view.setText(scripture);
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