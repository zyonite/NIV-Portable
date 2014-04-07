package com.bible.nivportable;

import java.util.ArrayList;

import com.bible.nivportable.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DisplaySOAPJournalListActivity extends Activity {

	private SOAPJournalDatabaseHelper sdh = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soap_journals);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		sdh = new SOAPJournalDatabaseHelper(this);

		if (sdh.openDataBase()) {
			final ArrayList<ArrayList<String>> savedRecords = sdh
					.SearchRecordEntries();
			sdh.close();

			ListView listView = (ListView) findViewById(R.id.soapjournalresults);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					new String[] { "No SOAP journals found" });

			// Any SOAP journals?
			if (savedRecords.size() > 0) {

				String[] journalResults = new String[savedRecords.size()];

				int counter = 0;

				for (ArrayList<String> resultSet : savedRecords) {
					String bookTitle = resultSet.get(0);
					String chapterNumber = resultSet.get(1);
					String verseNumber = resultSet.get(2);
					String date = resultSet.get(3);

					journalResults[counter] = bookTitle + " " + chapterNumber
							+ ":" + verseNumber + " (" + date + ")";

					counter++;
				}

				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, journalResults);
			}

			listView.setAdapter(adapter);
			listView.refreshDrawableState();

			final Intent next = new Intent(this,
					DisplaySOAPJournalActivity.class);// Change

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {

					ArrayList<String> selectedResultSet = savedRecords
							.get(position);

					next.putExtra("BOOK_TITLE", selectedResultSet.get(0));
					next.putExtra("CHAPTER_NUMBER", selectedResultSet.get(1));
					next.putExtra("VERSE_NUMBER", selectedResultSet.get(2));
					next.putExtra("DATE_CREATED", selectedResultSet.get(3));
					next.putExtra("OBSERVATION", selectedResultSet.get(4));
					next.putExtra("APPLICATION", selectedResultSet.get(5));
					next.putExtra("PRAYER", selectedResultSet.get(6));

					startActivity(next);
				}
			});
		}
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
