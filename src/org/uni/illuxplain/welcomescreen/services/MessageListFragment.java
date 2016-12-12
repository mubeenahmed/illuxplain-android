package org.uni.illuxplain.welcomescreen.services;

import java.util.ArrayList;
import java.util.List;

import org.uni.illuxplain.list.adapters.MessageListAdapter;
import org.uni.illuxplain.xmpp.services.Messenger;
import org.uni.jain.illuxplain.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageListFragment extends Fragment {

	public interface OnInboxFeatureTouch {
		public void onCreate();
		public void onView();
	}

	private OnInboxFeatureTouch inbox;
	private Button button;
//	private static RecyclerView list;
	private static MessageListAdapter adapter;
//	private RecyclerView.LayoutManager layoutManager;
	private TextView updatedText;
	
	private List<String> username;
	private List<String> userText;
	private List<String>  createdDate;
	
	public static String usernameListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_message_list,
				container, false);
		button = (Button) rootView.findViewById(R.id.create_message);
//		list = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

//		layoutManager = new LinearLayoutManager(getActivity());
//		list.setLayoutManager(layoutManager);
		
		username = new ArrayList<String>();
		userText = new ArrayList<String>();
		createdDate =new ArrayList<String>();
		
		updatedText = (TextView) rootView.findViewById(R.id.friend_message);
		inbox = (OnInboxFeatureTouch) getActivity();
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createMessageView();
			}
		});

		return rootView;
	}

	public void onStart() {
		super.onStart();
		loadMessages();
		
	}
	
	
	
	private void loadMessages() {
		
	}	

	public static void adapterHasChanged(){
//		adapter.notifyDataSetChanged();
//		list.invalidate();
	}

	private void createMessageView() {
		inbox.onCreate();

	}

	@Override
	public void onStop() {
		super.onStop();
		
		
	}

}
