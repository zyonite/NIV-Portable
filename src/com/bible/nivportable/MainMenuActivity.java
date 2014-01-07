package com.bible.nivportable;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainMenuActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		bdh = new BibleDatabaseHelper(this);
		bdh.initialiseDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void loadCollectionMenu(View view) {

		if (bdh.openDataBase()) {

			String collection = (String) view.getTag();

			ArrayList<String> bookIds = bdh.selectBookTitleIds(collection);
			ArrayList<String> bookTitles = bdh.selectBookTitles(collection);

			Intent intent = new Intent(this,
					DisplayBibleCollectionActivity.class);

			intent.putStringArrayListExtra("BOOK_IDS", bookIds);
			intent.putStringArrayListExtra("BOOK_NAMES", bookTitles);

			startActivity(intent);

			bdh.close();
		}
	}
}