package com.sd1340.android.webview;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;
import android.app.ActionBar;

import com.sd1340.android.common.activities.SampleActivityBase;
import com.sd1340.android.common.logger.Log;
import com.sd1340.android.common.logger.LogFragment;
import com.sd1340.android.common.logger.LogWrapper;
import com.sd1340.android.common.logger.MessageOnlyLogFilter;

/**
 * A simple launcher activity containing a summary sample description, sample log
 *
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends SampleActivityBase {

    public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            PermissionRequestFragment fragment = new PermissionRequestFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

	// Hide actionBar
	ActionBar actionBar = getActionBar();
	actionBar.hide();  // Hide all menu items and app title that are in the top action bar

	// Hide log / sample text output
	ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
	output.setVisibility(View.GONE); // to show: View.VISIBLE
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.menu_toggle_log:
            mLogShown = !mLogShown;
            ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
            if (mLogShown) {
                output.setDisplayedChild(1);
            } else {
                output.setDisplayedChild(0);
            }
            supportInvalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
        public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}
