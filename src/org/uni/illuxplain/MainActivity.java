package org.uni.illuxplain;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.uni.illuxplain.welcomescreen.WelcomeScreen;
import org.uni.illuxplain.xmpp.services.XmppManager;
import org.uni.jain.illuxplain.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

	private EditText username;
	private EditText password;
	public static String user;
	public static String pass;
	public static boolean status;
	private XMPPTCPConnection connection;
	public static XmppManager manager; 
	private GlobalApplication global;
	public boolean isConnected;
	private Handler handler;
	private ProgressDialog progressDialog;
	private ConnectivityManager connectivityManager;
	private boolean isInternetConnected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		username = (EditText) findViewById(R.id.username_edit);
		password = (EditText) findViewById(R.id.password_edit);
		connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		global = GlobalApplication.getInstance();
		
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		
		handler = new Handler();
		manager = new XmppManager(GlobalApplication.SERVER_IP,GlobalApplication.SERVER_NAME,GlobalApplication.PORT);
		
		progressDialog = ProgressDialog.show(this, "", "Connecting To Server");
		NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
		
		 if ( connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
				 connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
						 connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
								 connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
			 
			 isInternetConnected = true;
		 }
		final Thread connect = new Thread(){
			public void run() {
				manager.init();
				connection = XmppManager.conn;
				if(!connection.isConnected()){
					isConnected = false;
				}
				else {
					isConnected = true;
				}
				handler.post(new Runnable(){
					@Override
					public void run() {
						progressDialog.dismiss();
						
						if(!isConnected || !(isInternetConnected)){
							
							AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
							dialog.setTitle("Connect Failed");
							dialog.setMessage("Connect Your Internet Connection and Retry");
							dialog.setPositiveButton("OK", new OnClickListener(){
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									finish();
								}
							}).show();
							
							dialog.setCancelable(false);
						}
					}
				});
			}
		};
		connect.start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
	public void getLogin(View view){
		user = username.getText().toString();
		pass = password.getText().toString();
		
		if(user.equals("") || pass.equals("")){
			Toast.makeText(this, "Enter Username/Password", Toast.LENGTH_LONG).show();
			return;
		}
		if(getAuth(user, pass)){
			global.setUsername(user);
			Intent i = new Intent(this, WelcomeScreen.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
		}
		
	}
	public boolean getAuth(String user,String pass){
		
		if(isConnected){
			try {
				XmppManager.username = user;
				XmppManager.password = pass;
				
				manager.getLogin();
				status = true;
			} catch (XMPPException e) {
				e.printStackTrace();
				onException("Opps! Incorrect Username And Password..");
				
			} catch (SmackException e) {
				e.printStackTrace();
				status = false;
				onException("Opps! Server Exception.. ");
			} catch (IOException e) {
				status = false;
				e.printStackTrace();
				onException("Opps! Connect not connect to server. :( ");
			}
		}
		return status;
	}
	
	private void onException(String message){
		manager.destroy();
		status = false;
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	public void getRegister(View v){
		Intent intent = new Intent(this,RegisterActivity.class);
		startActivity(intent);
	}
}
