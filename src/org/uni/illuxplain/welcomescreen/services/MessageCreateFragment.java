package org.uni.illuxplain.welcomescreen.services;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.roster.Roster;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.xmpp.services.PrivateChat;
import org.uni.jain.illuxplain.R;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageCreateFragment extends Fragment{
	
	private Button sendMessage;
	private EditText to;
	private EditText message;

	private String toUser;
	private String content;
	
	private int seen;
	private PrivateChat chat;
	
	public static final String MESSAGE_SERVICE = "http://192.168.1.3:8082/IlluxplainWebServices/webservice/illuxplain/json/message";	
	
	private GlobalApplication application = GlobalApplication.getInstance();
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_message_create, container,false);
	
		sendMessage = (Button) rootView.findViewById(R.id.send_message);
		
		to = (EditText) rootView.findViewById(R.id.edit_enter_to);
		message = (EditText) rootView.findViewById(R.id.edit_enter_message);
		
		chat = application.getPrivateChat();
		
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();

		sendMessage.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				toUser = to.getText().toString();
				content = message.getText().toString();
				if(!toUser.equals("") && !content.equals("")){
					
					chat.sendMessage(toUser, content);
					
					storeMessage();
				}
				
				else{
					Toast.makeText(getActivity(), "Enter Required Fields", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
	private void storeMessage() {
		//Send data to database .. 
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("username", GlobalApplication.username);
		params.put("message_to",toUser);
		params.put("message", content);
		params.put("seen", ""+seen);
		
		MessageVolley message = new MessageVolley(Request.Method.POST, MESSAGE_SERVICE, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "Sending Message Failed", Toast.LENGTH_SHORT).show();
			}
		}, params);
		Volley.newRequestQueue(getActivity()).add(message);
	}
}
