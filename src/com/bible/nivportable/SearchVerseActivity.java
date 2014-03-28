package com.bible.nivportable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class SearchVerseActivity extends Activity {

	private BibleDatabaseHelper bdh = null;
	private static final int MINIMAL_SEARCH_LENGTH = 4;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_verses);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);
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

	public void searchVerse(View view) {
		EditText searchText = (EditText) findViewById(R.id.searchtext);
		String text = searchText.getText().toString();

		// Prevent search if search text is too short
		if (text.length() < MINIMAL_SEARCH_LENGTH) {
			Context context = getApplicationContext();
			CharSequence errorText = "You need to enter search text with four or more characters";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, errorText, duration);
			toast.show();
		} else {
			if (bdh.openDataBase()) {
				ArrayList<ArrayList<String>> verseResult = bdh.SearchVerses(text);
				//String 
				// Intent next = new Intent(this,
				// DisplayVerseSearchResults.class);

				// startActivity(next);
				bdh.close();
				
				InputMethodManager imm = (InputMethodManager)getSystemService(
						Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
			}
		}
	}
}
