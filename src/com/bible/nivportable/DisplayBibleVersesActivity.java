package com.bible.nivportable;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

//Add more functionalities to this in the future
public class DisplayBibleVersesActivity extends Activity {

	private BibleDatabaseHelper bdh = null;

	private final float verseTextSizeSp = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible_verses);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);

		Intent current = getIntent();

		String chapterName = current.getStringExtra("CHAPTER_NAME");
		String chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
		String[] verseNumbers = current.getStringArrayExtra("VERSE_NUMBERS");
		String title = chapterName + " " + chapterNumber + ":";

		int length = verseNumbers.length;

		if (length == 1) {
			title += verseNumbers[0];
		} else {
			title += verseNumbers[0] + " - " + verseNumbers[length - 1];
		}

		setTitle(title);

		ArrayList<String> verses = current
				.getStringArrayListExtra("BIBLE_VERSES");

		LinearLayout layout = (LinearLayout) findViewById(R.id.bibleverseslayout);

		for (int i = 0; i < verses.size(); i++) {
			TextView view = new TextView(this);
			float textSize = TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_SP, verseTextSizeSp, getResources()
							.getDisplayMetrics());
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			view.setPadding(10, 5, 10, 5);
			view.setText(verseNumbers[i] + " " + verses.get(i));
			view.setTextSize(textSize);
			layout.addView(view);
		}
		
		//TODO: Generate vertical navigation drag here 

		layout.refreshDrawableState();
	}
}