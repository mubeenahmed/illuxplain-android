package org.uni.illuxplain.welcomescreen;

import org.uni.illuxplain.canvas.CanvasActivity;
import org.uni.illuxplain.group.chat.GroupChat;
import org.uni.illuxplain.group.chat.GroupUsersDailog;
import org.uni.jain.illuxplain.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

	private Button groupButton;
	private Handler handler;
	
	public static GroupChat groupChat;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		handler = new Handler();
		groupChat = new GroupChat(getActivity(),handler);
		
		View rootView = inflater.inflate(R.layout.home_fragment, container,
				false);
		groupButton = (Button) rootView.findViewById(R.id.group);
		groupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GroupUsersDailog dialog = new GroupUsersDailog(getActivity());
				dialog.setTitle("Create Group Chat: Enter User Name");
				dialog.show();
			}
		});
		return rootView;
	}
	
	
	public void createCanvas(){
		Intent i = new Intent(getActivity(), CanvasActivity.class);
		startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

}
