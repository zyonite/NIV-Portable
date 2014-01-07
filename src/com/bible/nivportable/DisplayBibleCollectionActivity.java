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
import android.widget.Button;
import android.widget.LinearLayout;

public class DisplayBibleCollectionActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible_collection);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		ArrayList<String> bookIds = intent.getStringArrayListExtra("BOOK_IDS");
		ArrayList<String> bookNames = intent
				.getStringArrayListExtra("BOOK_NAMES");

		LinearLayout layout = (LinearLayout) findViewById(R.id.biblecollectionlayout);

		for (int i = 0; i < bookIds.size(); i++) {

			Button button = new Button(this);
			button.setText(bookNames.get(i));
			button.setTag(bookIds.get(i));
			button.setWidth(100);
			button.setHeight(50);
			layout.addView(button);
		}

		layout.refreshDrawableState();
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
