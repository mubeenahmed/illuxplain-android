package org.uni.email.illuxplain;

import org.uni.jain.illuxplain.R;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EmailAndReportActivity extends ActionBarActivity {

	private TextView report;
	private TextView enterCode;
	private String username;
	private String email;
	private static final String FROM_USER = "mubeenahmedsiddiqui@gmail.com";
	private static final String FROM_PASS = "shireen.abc";
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_and_report);
		report = (TextView) findViewById(R.id.report);
		enterCode = (TextView) findViewById(R.id.enter_code_text);

		handler = new Handler();
		Bundle bundle = getIntent().getExtras();
		username = bundle.getString("username");
		email = bundle.getString("email");
		report.setText(username
				+ ", This step is for verification purpose. Please Check the email "
				+ email
				+ ". If you donot receive email, CLICK \"Resend Email\" button");
		enterCode.setText("Enter Your Code HERE");

		if (savedInstanceState == null) {
			// Send Email
			Thread thread  = new Thread(){
				@Override
				public void run(){
					sendEmail();
					handler.post(new Runnable(){

						@Override
						public void run() {
							Log.v(" ", "Sending EMAIL");
						}
					});
				}
			}; 
			thread.start();
			
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.email_and_report, menu);
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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("checkToTrue", true);
	}

	public void resendEmail(View v) {
		Thread thread  = new Thread(){
			@Override
			public void run(){
				sendEmail();
			}
		}; 
		thread.start();
	}

	private void sendEmail() {
//		EmailReport email = new EmailReport(FROM_USER, FROM_PASS);
//		email.setTo(this.email);
//		email.setSubject("A Email");
//		email.setFrom("mubeenahmedsiddiqui@gmail.com");
//		email.setBody("This is a email");
//		
//		
//		try {
//			//email.addAttachment("/sdcard/filelocaion");
//			if (email.send()) {
//				Toast.makeText(this, "Email was sent successfully.",
//						Toast.LENGTH_LONG).show();
//			} else {
//				Toast.makeText(this, "Email was not sent.",
//						Toast.LENGTH_LONG).show();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	}	
}
