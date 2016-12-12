package org.uni.illuxplain.group.chat;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.canvas.CanvasActivity;
import org.uni.illuxplain.opentok.OpenTokConfig;
import org.uni.jain.illuxplain.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class GroupUsersDailog extends Dialog {

	public static List<String> users = new ArrayList<String>();
	private static ArrayAdapter<String> usersListAdapter;
	public static boolean isGrouped = false;
	private EditText addingUsers;
	private String sAddingUsers;
	private Button addButton;
	private Button okButton;
	private Button cancelButton;
	private ListView listView;
	
	public static String sessionId; 
	public static String tokenKey;
	
	private ProgressDialog dialog;
	
	public static final String SESSION_URL = "http://illuxplain-mubeen.rhcloud.com/webservice/illuxplain/opentok/generator";

	public GroupUsersDailog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.group_user_dialog);
		addingUsers = (EditText) findViewById(R.id.enter_users);
		addButton = (Button) findViewById(R.id.add);
		okButton = (Button) findViewById(R.id.ok_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);

		listView = (ListView) findViewById(R.id.users_added);

		usersListAdapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_list_item_1, users);
		listView.setAdapter(usersListAdapter);

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Search User is avaible
				sAddingUsers = addingUsers.getText().toString();
				users.add(sAddingUsers);
				usersListAdapter.notifyDataSetChanged();
			}
		});

		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isGrouped = true;

				// Create a service, get a session Id and token id .. Send the
				// session id to the invite users for there token id generation
				
				dialog = ProgressDialog.show(getContext(), "Please Wait", "Creating Session ID");
				dialog.setCancelable(true);
				
				getSessionID();
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				users.clear();
				dismiss();
			}
		});
	}

	private void getSessionID() {
//		JSONObject object = new JSONObject();
		final JsonObjectRequest req = new JsonObjectRequest(SESSION_URL, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						
						try {
							Log.v("Response Code ", response.toString());
							sessionId = response.getString("session_id");
							tokenKey = response.getString("token_key");
																				
							new OpenTokConfig(sessionId , tokenKey);
							
							dialog.dismiss();
							
							Intent i = new Intent(getContext(), CanvasActivity.class);
							getContext().startActivity(i);
							
							Toast.makeText(getContext(), "Media Stream Working", Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
				}, new Response.ErrorListener() {
					
					@Override
					public void onErrorResponse(VolleyError error) {
						dialog.dismiss();
						Toast.makeText(getContext(), "Error In Server", Toast.LENGTH_LONG).show();
						Intent i = new Intent(getContext(), CanvasActivity.class);
						getContext().startActivity(i);
					}
					
				});
		
		final RequestQueue queue = Volley.newRequestQueue(getContext());
		queue.add(req);
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface d) {
				queue.cancelAll(req);
			}
		});
	}

}
