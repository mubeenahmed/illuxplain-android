package org.uni.illuxplain.group.chat;



import java.util.List;

import org.uni.illuxplain.opentok.OpenTokConfig;
import org.uni.jain.illuxplain.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ShowParticipants extends Dialog implements android.view.View.OnClickListener{

	
	private ListView list;
	private ShowParticipantListAdpater adapter;
	private EditText addUser;
	private Button add;
	private Context context;
	private TextView noUsers;
	
	public ShowParticipants(Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.show_participants_dialog);
		
		list = (ListView) findViewById(R.id.participants);
		add = (Button) findViewById(R.id.add);
		addUser = (EditText) findViewById(R.id.add_participant);
		noUsers = (TextView) findViewById(R.id.no_users);
		
		add.setOnClickListener(this);
		
		List<String> users = GroupChat.getParticipantNick();//(ArrayList<String>) GroupUsersDailog.users;
		
	if(users != null){
		if(users.size() == 0 || users.size() < 0){
			noUsers.setText("No User On This Group");
		}
		else{
			noUsers.setText("");
			adapter = new ShowParticipantListAdpater(users, context);
			list.setAdapter(adapter);
		}
		
	}else{
		noUsers.setText("No User On This Group");
	}
		
	}

	@Override
	public void onClick(View v) {
		String mUserName = addUser.getText().toString();
		if(mUserName.equals("")){
			return;
		}
		GroupChat.setUsers(mUserName, OpenTokConfig.sessionId);
	}

}
