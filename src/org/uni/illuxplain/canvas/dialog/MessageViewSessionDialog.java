package org.uni.illuxplain.canvas.dialog;

import java.util.ArrayList;
import java.util.List;

import org.uni.illuxplain.canvas.CanvasActivity;
import org.uni.illuxplain.canvas.SurfaceViewCanvas;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;
import org.uni.jain.illuxplain.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MessageViewSessionDialog extends Dialog{

	public MessageViewSessionDialog(Context context) {
		super(context);
		
		this.context = context;
		addText = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, addText);
	}
	

	private Button sendButton;
	private ListView listMessage;
	private EditText message;
	private static List<String> incomingMessage = SurfaceViewCanvas.incomingMessages;
	private ArrayList<String> addText;
	private ArrayAdapter<String> adapter;

	private DrawingXmppServices instantMesssage = CanvasActivity.drawingServices;
	private Context context;
	private ListView list;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.canvas_message_dialog);
		
		sendButton = (Button) findViewById(R.id.send_message);
		message = (EditText) findViewById(R.id.text_message);
		
		list = (ListView) findViewById(R.id.message_list);
		sendButton.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				String msg = message.getText().toString();
				if(!msg.equals("")){
					instantMesssage.sendDrawingData(msg);
					addText.add("YOU: "+msg);
				}
				adapter.notifyDataSetChanged();
				list.invalidate();
			}
			
		});
		list.setAdapter(adapter);
		
	}
	
	public void updateTextList(String from, String message){
		
		String incoming = from+ ": "+message;
		addText.add(incoming);
		adapter.notifyDataSetChanged();
		list.invalidate();
	}
	
	@Override
	public void onBackPressed() {
		incomingMessage.clear();
		CanvasActivity ac = (CanvasActivity) context;
		ac.onBackPressDialog();
		super.onBackPressed();
	}
}
