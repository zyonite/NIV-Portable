package com.bible.nivportable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchVerseActivity extends Activity {

	private BibleDatabaseHelper bdh = null;
	private static final int MINIMAL_SEARCH_LENGTH = 4;
	private Context context;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_verses);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		bdh = new BibleDatabaseHelper(this);
		context = getApplicationContext();
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

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

		// Prevent search if search text is too short
		if (text.length() < MINIMAL_SEARCH_LENGTH) {
			Context context = getApplicationContext();
			CharSequence errorText = "You need to enter search text with four or more characters";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, errorText, duration);
			toast.show();
		} else {
			View layout = findViewById(R.id.searchversescontainer);
			ListView listView = (ListView) findViewById(R.id.versesearchresultlayout);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, new String[0]);
			listView.setAdapter(adapter);

			if (bdh.openDataBase()) {
				final ArrayList<ArrayList<String>> result = bdh
						.SearchVerses(text);
				bdh.close();

				// If there are results, then show them on ListView
				if (result.size() > 0) {
					String[] verseResults = new String[result.size()];
					int counter = 0;

					for (ArrayList<String> resultSet : result) {
						verseResults[counter] = resultSet.get(0) + " "
								+ resultSet.get(1) + ":" + resultSet.get(2)
								+ " - " + resultSet.get(3);
						counter++;
					}

					adapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_list_item_1, verseResults);
					listView.setAdapter(adapter);

					final Intent next = new Intent(this,
							CreateSOAPJournalActivity.class);// Change

					listView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							next.putExtra("BOOK_TITLE", result.get(position)
									.get(0));
							next.putExtra("CHAPTER_NUMBER", result
									.get(position).get(1));
							next.putExtra("VERSE_NUMBER", result.get(position)
									.get(2));
							startActivity(next);
						}
					});
				}
				// Otherwise, show message about no results found
				else {
					CharSequence errorText = "No verses found that contains '"
							+ text + "'";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, errorText, duration);
					toast.show();
				}

				listView.refreshDrawableState();
			}

			layout.refreshDrawableState();
		}
	}
}
