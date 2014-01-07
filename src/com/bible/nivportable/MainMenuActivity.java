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

	public void loadOldTestament(View view) {

		if (bdh.openDataBase()) {
			ArrayList<String> bookIds = bdh.selectBookTitleIds(1);
			ArrayList<String> bookTitles = bdh.selectBookTitles(1);

			Intent intent = new Intent(this, DisplayOldTestamentActivity.class);

			intent.putStringArrayListExtra("BOOK_IDS", bookIds);
			intent.putStringArrayListExtra("BOOK_NAMES", bookTitles);

			startActivity(intent);

			bdh.close();
		}
	}

	public void loadNewTestament(View view) {

		if (bdh.openDataBase()) {
			ArrayList<String> bookTitles = bdh.selectBookTitles(2);

			/*
			 * Intent intent = new Intent(this,
			 * DisplayNewTestamentActivity.class);
			 * 
			 * Bundle extras = new Bundle(); extras.putSerializable("BOOK_MENU",
			 * bookTitles); intent.putExtras(extras); startActivity(intent);
			 * 
			 * bdh.close();
			 */
		}
	}
}
