package org.uni.illuxplain.welcomescreen.services;

import org.uni.illuxplain.list.adapters.MessageViewAdapter;
import org.uni.jain.illuxplain.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MessageViewFragment extends Fragment {


	private ListView list;
	private MessageViewAdapter adapter;
	private Button sendButton;
	private EditText text;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_message_view,
				container, false);
		list = (ListView) rootView.findViewById(R.id.view_message);
		//sendButton = (Button) rootView.findViewById(R.id.your_message_button);
		//text = (EditText) rootView.findViewById(R.id.your_message);
		
		return rootView;

	}

	@Override
	public void onStart() {
		super.onStart();
		
	}

	@Override
	public void onStop() {
		super.onStop();
		
	}

	

}
