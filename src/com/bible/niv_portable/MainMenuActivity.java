package com.bible.niv_portable;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

//import com.bible.niv_portable.R;

public class MainMenuActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		bdh = new BibleDatabaseHelper(this);
		bdh.initialiseDatabase();
		SOAPJournalDatabaseHelper sdh = new SOAPJournalDatabaseHelper(this);
		sdh.initialiseDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void loadCollectionMenu(View view) {

		if (bdh.openDataBase()) {

			// Collection ID - 1 is Old Testament, 2 is New Testament
			String collection = (String) view.getTag();

			ArrayList<String> bookIds = bdh.selectBookTitleIds(collection);
			ArrayList<String> bookTitles = bdh.selectBookTitles(collection);

			Intent next = new Intent(this, DisplayBibleCollectionActivity.class);

			next.putExtra("ACTIVITY_TITLE", ((Button) view).getText()
					.toString());
			next.putStringArrayListExtra("BOOK_IDS", bookIds);
			next.putStringArrayListExtra("BOOK_TITLES", bookTitles);

			bdh.close();

			startActivity(next);
		}
	}

	public void loadVerseSearch(View view) {
		Intent next = new Intent(this, SearchVerseActivity.class);
		startActivity(next);
	}

	public void loadSoapJournals(View view) {
		Intent next = new Intent(this, DisplaySOAPJournalListActivity.class);
		startActivity(next);
	}
}