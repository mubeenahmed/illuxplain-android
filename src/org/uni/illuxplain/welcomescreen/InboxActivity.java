package org.uni.illuxplain.welcomescreen;

import org.uni.illuxplain.welcomescreen.services.MessageCreateFragment;
import org.uni.illuxplain.welcomescreen.services.MessageListFragment;
import org.uni.illuxplain.welcomescreen.services.MessageViewFragment;
import org.uni.jain.illuxplain.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class InboxActivity extends AppCompatActivity implements MessageListFragment.OnInboxFeatureTouch{

	
	public static boolean isInbox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		
		Fragment fragment = new MessageListFragment();
		trans.add(R.id.fragment, fragment);
		trans.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		Fragment fragment = new MessageCreateFragment();
		trans.replace(R.id.fragment, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	@Override
	public void onView() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		Fragment fragment = new MessageViewFragment();
		trans.replace(R.id.fragment, fragment);
		trans.addToBackStack(null);
		trans.commit();
	}
}
