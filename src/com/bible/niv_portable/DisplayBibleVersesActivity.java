package com.bible.niv_portable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;

public class DisplayBibleVersesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_verse);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		Intent current = getIntent();

		final String bookTitle = current.getStringExtra("BOOK_TITLE");
		final String chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
		final ArrayList<String> verseNumbers = current.getStringArrayListExtra("VERSE_NUMBERS");
		final ArrayList<String> verseTexts = current.getStringArrayListExtra("VERSE_TEXTS");

		String title = bookTitle + " " + chapterNumber;
		setTitle(title);

		ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
		PagerAdapter adapter = new VerseViewPagerAdapter(this, bookTitle, chapterNumber, verseNumbers, verseTexts);

		viewPager.setAdapter(adapter);
	}
}