package org.uni.illuxplain.welcomescreen;

import org.uni.jain.illuxplain.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsListAdapter extends BaseAdapter{

	private Context context;
	private String[] res;
	
	public FriendsListAdapter(Context context,String[] resource) {
		this.context = context;
		this.res = resource;
	}
	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.friend_list_view,parent,false);
			ViewHolder holder = new ViewHolder();
			holder.friendImage = (ImageView) v.findViewById(R.id.friend_image);
			holder.friendName = (TextView) v.findViewById(R.id.friend_name);
			
			v.setTag(holder);
		}
		v.getTag();
		
		return v;
	}
	
}
class ViewHolder{
	public static ImageView friendImage;
	public static TextView friendName;
}
