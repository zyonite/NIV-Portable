package com.bible.niv_portable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;

public class DisplayBibleVersesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

        Intent current = getIntent();

        String bookTitle = current.getStringExtra("BOOK_TITLE");
        String chapterNumber = current.getStringExtra("CHAPTER_NUMBER");
        ArrayList<String> verseNumbers = current.getStringArrayListExtra("VERSE_NUMBERS");
        ArrayList<String> verseTexts = current.getStringArrayListExtra("VERSE_TEXTS");

        String title = bookTitle + " " + chapterNumber;
        setTitle(title);

		//If option is list view then instantiate list view
		SharedPreferences sharedOptions = PreferenceManager.getDefaultSharedPreferences(this);
		String dispOptions = sharedOptions.getString("verses_display_style", "1");

		switch (dispOptions)
		{
			case "1":
                instantiateListView(bookTitle, chapterNumber, verseNumbers, verseTexts);
                break;
			case "2":
                instantiatePageView(bookTitle, chapterNumber, verseNumbers, verseTexts);
                break;
            default:
                instantiateListView(bookTitle, chapterNumber, verseNumbers, verseTexts);
                break;
		}
	}

	private void instantiateListView(final String bookTitle, final String chapterNumber,
                                     final ArrayList<String> verseNumbers,
                                     final ArrayList<String> verseTexts)
	{
        setContentView(R.layout.activity_bible_verses);

        ListView verseList = (ListView) findViewById(R.id.bibleverseslayout);

        String[] verseResults = new String[verseTexts.size()];

        for (int i = 0; i < verseTexts.size(); i++) {
            verseResults[i] = verseNumbers.get(i) + " - " + verseTexts.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, verseResults);
        verseList.setAdapter(adapter);

        final Intent next = new Intent(this, CreateSOAPJournalActivity.class);// Change

        verseList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                next.putExtra("BOOK_TITLE", bookTitle);
                next.putExtra("CHAPTER_NUMBER", chapterNumber);
                next.putExtra("VERSE_NUMBER", verseNumbers.get(position));
                next.putExtra("VERSE_TEXT", verseTexts.get(position));
                startActivity(next);
            }
        });
	}

	private void instantiatePageView(final String bookTitle, final String chapterNumber,
                                     final ArrayList<String> verseNumbers,
                                     final ArrayList<String> verseTexts)
	{
        setContentView(R.layout.viewpager_verse);
        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        PagerAdapter adapter = new VerseViewPagerAdapter(this, bookTitle, chapterNumber, verseNumbers, verseTexts);

        viewPager.setAdapter(adapter);
	}
}