package com.bible.nivportable;

import java.util.ArrayList;

import com.bible.nivportable.R;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplaySOAPJournalsActivity extends Activity {

	private SOAPJournalDatabaseHelper sdh = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soap_journals);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		sdh = new SOAPJournalDatabaseHelper(this);

		if (sdh.openDataBase()) {
			ArrayList<ArrayList<String>> savedSOAPJournals = sdh
					.SearchSOAPJournals();
			sdh.close();

			ListView listView = (ListView) findViewById(R.id.soapjournalresults);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					new String[] { "No SOAP journals found" });

			// Any SOAP journals?
			if (savedSOAPJournals.size() > 0) {

				String[] savedSOAPJournalResults = new String[savedSOAPJournals
						.size()];

				int counter = 0;

				for (ArrayList<String> resultSet : savedSOAPJournals) {
					savedSOAPJournalResults[counter] = resultSet.get(0) + " "
							+ resultSet.get(1) + ":" + resultSet.get(2) + " ("
							+ resultSet.get(3) + ")";
					counter++;
				}

				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,
						savedSOAPJournalResults);
			}

			listView.setAdapter(adapter);
			listView.refreshDrawableState();
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
