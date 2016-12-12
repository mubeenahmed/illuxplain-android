package org.uni.illuxplain.group.chat;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.uni.jain.illuxplain.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ShowParticipantListAdpater extends BaseAdapter implements ListAdapter { 
	
	private List<String> list = new ArrayList<String>(); 
	private Context context; 



	public ShowParticipantListAdpater(List<String> list, Context context) { 
	    this.list = list; 
	    this.context = context; 
	} 

	@Override
	public int getCount() { 
	    return list.size(); 
	} 

	@Override
	public Object getItem(int pos) { 
	    return list.get(pos); 
	} 

	@Override
	public long getItemId(int pos) { 
//	    return list.get(pos).getId();
		return 0;
	    //just return 0 if your list items do not have an Id variable.
	} 

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    if (view == null) {
	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	        view = inflater.inflate(R.layout.custom_list_add_participants, null);
	    } 

	    //Handle TextView and display string from your list
	    TextView listItemText = (TextView)view.findViewById(R.id.list_item_string); 
	    listItemText.setText(list.get(position)); 

	    //Handle buttons and add onClickListeners
	    Button kick = (Button)view.findViewById(R.id.kick_button);

	    kick.setOnClickListener(new View.OnClickListener(){
	        @Override
	        public void onClick(View v) { 
	            //do something and kick the user from group
	        	try {
					GroupChat.multiChat.kickParticipant(list.get(position), "");
				} catch (XMPPErrorException e) {
					e.printStackTrace();
				} catch (NoResponseException e) {
					e.printStackTrace();
				} catch (NotConnectedException e) {
					e.printStackTrace();
				}
	        	
	            list.remove(position); //or some other task
	            notifyDataSetChanged();
	        }
	    });

	    return view; 
	} 
}
