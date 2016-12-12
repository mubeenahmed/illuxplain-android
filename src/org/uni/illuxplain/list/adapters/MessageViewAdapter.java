package org.uni.illuxplain.list.adapters;

import java.util.List;

import org.uni.illuxplain.GlobalApplication;
import org.uni.jain.illuxplain.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageViewAdapter extends BaseAdapter{

	private Context context;
	private List<String> username;
	private List<String> userText;
	private List<String> createdDate;
	
	public MessageViewAdapter(Context context, List<String> username,List<String> userText,List<String> createdDate) {
		this.context = context;
		this.username = username;
		this.userText = userText;
		this.createdDate = createdDate;
	}
	
	@Override
	public int getCount() {
		return userText.size();
	}

	@Override
	public Object getItem(int p) {
		return p;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		if(v == null){
			ViewHolder viewHolder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(R.layout.view_friend_messages, parent, false);
			
			viewHolder.user = (TextView) v.findViewById(R.id.sender_name);
			viewHolder.text = (TextView) v.findViewById(R.id.sender_message);
			viewHolder.date = (TextView) v.findViewById(R.id.sender_date);
			
			v.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) v.getTag();
		
		if(GlobalApplication.username == username.get(position)){
			holder.user.setText(username.get(position));
			holder.user.setBackgroundColor(context.getResources().getColor(R.color.little_green));
			holder.text.setText(userText.get(position));
			holder.date.setText(createdDate.get(position));
		}
		else{
			holder.user.setText(username.get(position));
			holder.text.setText(userText.get(position));
			holder.date.setText(createdDate.get(position));
		}
		
		return v;
	}
	
	private static class ViewHolder{
		private TextView user;
		private TextView text;
		private TextView date;
		
	}
}
