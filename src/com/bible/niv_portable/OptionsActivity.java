package com.bible.niv_portable;

import android.app.Activity;
import android.os.Bundle;

public class OptionsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new OptionsFragment())
                .commit();
    }
}