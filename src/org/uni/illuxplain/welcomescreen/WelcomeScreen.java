package org.uni.illuxplain.welcomescreen;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.welcomescreen.services.MessageListFragment;
import org.uni.illuxplain.welcomescreen.services.ProfileUpdate;
import org.uni.illuxplain.xmpp.services.PrivateChat;
import org.uni.illuxplain.xmpp.services.XmppManager;
import org.uni.jain.illuxplain.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class WelcomeScreen extends AppCompatActivity {

	private ViewPager viewPager;
	private TabsPageAdapter mAdapter;
	private Menu menu;
	private Intent service;
	private GlobalApplication userSession = GlobalApplication.getInstance();
	private static int notificationNumbers = 1;
	private NotificationManager manager;
	public static boolean isBroadCast =false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);

		// Action Bar Styling -------------------------------
		final ActionBar actionBar = getSupportActionBar();
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPageAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		// ----------------------------------------------------------------------
		PrivateChat chat = new PrivateChat(this);
		userSession.setPrivateChat(chat);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.setting) {
			Intent intent = new Intent(this, ProfileUpdate.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.index_message) {
			if (manager != null) {
				manager.cancelAll();
				notificationNumbers = 1;
			}
			Intent intent = new Intent(WelcomeScreen.this, InboxActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.notifications) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}


	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle("Logout");
		build.setMessage("Do you want to logout?");

		build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				XMPPTCPConnection conn = XmppManager.conn;
				conn.disconnect();
				android.os.Process.killProcess(android.os.Process.myPid());
			}

		});

		build.setNegativeButton("NO!", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alert = build.create();
		alert.show();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("username", GlobalApplication.username);
		outState.putString("userurl", GlobalApplication.userURL);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String usr = savedInstanceState.getString("username");
		String url = savedInstanceState.getString("userurl");
		GlobalApplication.username = usr;
		GlobalApplication.userURL = url;
	}

}
