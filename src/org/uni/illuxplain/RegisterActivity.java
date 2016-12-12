package org.uni.illuxplain;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.uni.email.illuxplain.EmailAndReportActivity;
import org.uni.illuxplain.xmpp.services.XmppManager;
import org.uni.jain.illuxplain.R;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements
		OnClickListener, DatePickerDialog.OnDateSetListener {

	private static final String USERNAME = "username";
	private static final String EMAIL = "email";

	private EditText username;
	private EditText email;
	private EditText birthDay;
	private EditText password;

	private int year = 0;
	private int month = 0;
	private int date = 0;

	private DatePickerDialog datePicker;
	private Calendar calendar;

	public static String user;
	public static String pass;
	public static String birth;
	public static String mail;
	private static XMPPTCPConnection connection;
	private XmppManager manager;
	private boolean isConnected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user_activity);
		if (savedInstanceState == null) {
			username = (EditText) findViewById(R.id.username_edit);
			email = (EditText) findViewById(R.id.email_edit);
			birthDay = (EditText) findViewById(R.id.birthdate_picker);
			password = (EditText) findViewById(R.id.password_edit);

			birthDay.setInputType(InputType.TYPE_NULL);

			calendar = Calendar.getInstance();
			birthDay.setOnClickListener(this);
			datePicker = new DatePickerDialog(this, this,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DATE));
			
			manager = new XmppManager(GlobalApplication.SERVER_IP, GlobalApplication.SERVER_NAME, GlobalApplication.PORT);
			Thread thread = new Thread(){
				public void run() {
					manager.init();
					connection = XmppManager.conn;
					if(!connection.isConnected()){
						Toast.makeText(getBaseContext(), "Not Connected TO Server", Toast.LENGTH_LONG).show();
						return;
					}
					else{
						isConnected = true;
					}
				}
			};
			thread.start();
		}

	}

	@Override
	public void onClick(View v) {
		datePicker.show();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar newDate = Calendar.getInstance();
		newDate.set(dayOfMonth, monthOfYear, year);
		birthDay.setText(dayOfMonth + "//" + monthOfYear + "//" + year);
		this.year = year;
		this.month = monthOfYear;
		this.date = dayOfMonth;
	}

	public void submit(View v) {
		// verifying user input
		user = this.username.getText().toString();
		mail = this.email.getText().toString();
		birth = this.birthDay.getText().toString();
		pass = this.password.getText().toString();

		if (email.equals("") && username.equals("") && birthDay.equals("")
				&& password.equals("")) {
			Toast.makeText(this, "Fill the required Fields", Toast.LENGTH_LONG)
					.show();
		} else if (username.equals("")) {
			this.username.setError("Fill Username Also");
		} else if (password.equals("")) {
			this.password.setError("Set Your Password");
		} else if (email.equals("")
				|| !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
			this.email.setError("Write Correct Email ID");
		} else if (birthDay.equals("11/12/1991")) {
			this.birthDay.setError("Select Your Birthday Please");
		} else {
			// checking username for uniquness.
			// sending to Other activity to take user input for getting password
			// Creating account in openfire server.
			
			AccountManager accountManager = AccountManager.getInstance(connection);
			AccountManager.sensitiveOperationOverInsecureConnectionDefault(true);
			Map<String, String> attr = new HashMap<String, String>();
			attr.put("birthday", birth);
			try {
				accountManager.createAccount(user, pass, attr);
				
			} catch (NoResponseException e) {
				e.printStackTrace();
				Toast.makeText(this, "No Response From Server", Toast.LENGTH_LONG).show();
			} catch (XMPPErrorException e) {
				e.printStackTrace();
				Toast.makeText(this, "User Already Exists", Toast.LENGTH_LONG).show();
			} catch (NotConnectedException e) {
				e.printStackTrace();
				Toast.makeText(this, "Server Not Connected", Toast.LENGTH_LONG).show();
			}

			// Intent intent = new Intent(this,EmailAndReportActivity.class);
			// intent.putExtra(USERNAME, user);
			// intent.putExtra(EMAIL, mail);
			// startActivity(intent);

		}
	}

	private void checkUserExist() {
		
		UserSearchManager search = new UserSearchManager(XmppManager.conn);
		Form searchForm = null;
		ReportedData data = null;
		try {
			searchForm = search
					.getSearchForm(XmppManager.conn.getServiceName());
		} catch (NoResponseException e1) {
			e1.printStackTrace();
		} catch (XMPPErrorException e1) {
			e1.printStackTrace();
		} catch (NotConnectedException e1) {
			e1.printStackTrace();
		}

		Form answerForm = searchForm.createAnswerForm();
		answerForm.setAnswer("Username", true);
		answerForm.setAnswer("search", user);

		try {
			data = search.getSearchResults(answerForm, "search."
					+ XmppManager.conn.getServiceName());
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}

		if (data.getRows() != null) {
			for (ReportedData.Row row : data.getRows()) {
				for (String value : row.getValues("jid")) {
					Log.i("Iteartor values......", " " + value);
				}
			}
			Toast.makeText(this, "Username Exists", Toast.LENGTH_SHORT).show();
		}
	}

	public void cancel(View v) {
		onBackPressed();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("username", this.username.getText().toString());
		outState.putString("email", this.email.getText().toString());
		outState.putString("birthday", this.birthDay.getText().toString());
	}
}
