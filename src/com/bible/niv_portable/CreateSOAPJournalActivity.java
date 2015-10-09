package com.bible.niv_portable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CreateSOAPJournalActivity extends Activity {

	private SOAPJournalDatabaseHelper sdh = null;
	private Context context;

	String bookTitle;
	String chapterNumber;
	String verseNumber;
	String verseText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_soap);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		sdh = new SOAPJournalDatabaseHelper(this);
		context = getApplicationContext();

		Intent current = getIntent();

		bookTitle = current.getStringExtra("BOOK_TITLE");
		chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
		verseNumber = current.getStringExtra("VERSE_NUMBER");
		verseText = current.getStringExtra("VERSE_TEXT");

		TextView view = (TextView) findViewById(R.id.scripture);
		String scripture = bookTitle + " " + chapterNumber + ":" + verseNumber + " - " + verseText;
		view.setText(scripture);
	}

	public void saveJournal(View view) {

		TextView observationView = (TextView) findViewById(R.id.observation);
		String observation = observationView.getText().toString();

		TextView applicationView = (TextView) findViewById(R.id.application);
		String application = applicationView.getText().toString();

		TextView prayerView = (TextView) findViewById(R.id.prayer);
		String prayer = prayerView.getText().toString();

		String message = "An error occured with the SOAP Journal database";

		if (sdh.openDataBase()) {
			message = sdh.SaveSOAPJournal(bookTitle, chapterNumber,
					verseNumber, observation, application, prayer);
			sdh.close();
		}

		if (message.equals("")) {
			message = "Successfully saved SOAP Journal";

			Intent intent = new Intent(this, MainMenuActivity.class);
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
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