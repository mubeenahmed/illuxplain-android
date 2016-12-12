package org.uni.illuxplain.list.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.uni.illuxplain.welcomescreen.services.MessageListFragment;
import org.uni.illuxplain.welcomescreen.services.MessageListFragment.OnInboxFeatureTouch;
import org.uni.jain.illuxplain.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageListAdapter {
//extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

	private List<String> userName;
	private List<String> lastMessage;
	private List<String> lastDate;
	private Context context;

	public class ViewHolder {
//	extends RecyclerView.ViewHolder implements
//			View.OnClickListener {

//		public TextView userNameText;
//		public TextView userMessage;
//		public TextView createDate;
//
//		public ViewHolder(View itemView) {
//			super(itemView);
//
//			userNameText = (TextView) itemView.findViewById(R.id.friend_name);
//			userMessage = (TextView) itemView.findViewById(R.id.friend_message);
//			createDate = (TextView) itemView.findViewById(R.id.create_date);
//
//			userNameText.setOnClickListener(this);
//			userMessage.setOnClickListener(this);
//			createDate.setOnClickListener(this);
//		}
//
//		@Override
//		public void onClick(View v) {
//			if (v instanceof TextView) {
//				MessageListFragment.usernameListener = userNameText.getText()
//						.toString();
//				MessageListFragment.OnInboxFeatureTouch listListener = (OnInboxFeatureTouch) context;
//				listListener.onView();
//			}
//		}

	}

	public MessageListAdapter(Context context, List<String> userName,
			List<String> text, List<String> createdDate) {
		this.userName = userName;
		this.lastMessage = text;
		this.lastDate = createdDate;
		this.context = context;
	}

//	@Override
//	public int getItemCount() {
//		return userName.size();
//	}
//
//	@Override
//	public void onBindViewHolder(ViewHolder holder, int position) {
//		String name = userName.get(position);
//		String message = lastMessage.get(position);
//		String date = lastDate.get(position);
//
//		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//
//		long milliSeconds = Long.parseLong(date);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(milliSeconds);
//		String d = calendar.getTime().toString();
//
//		holder.userNameText.setText(name);
//		holder.userMessage.setText(message);
//		holder.createDate.setText(d);
//	}
//
//	@Override
//	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//		View v = LayoutInflater.from(parent.getContext()).inflate(
//				R.layout.message_list_customization, parent, false);
//		MessageListAdapter.ViewHolder vh = new ViewHolder(v);
//
//		return vh;
//	}

}
