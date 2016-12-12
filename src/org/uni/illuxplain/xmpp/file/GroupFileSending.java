package org.uni.illuxplain.xmpp.file;

import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.canvas.CanvasActivity;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;

public class GroupFileSending extends Thread{

	
	private String url;
	private String username;
	private DrawingXmppServices services = CanvasActivity.drawingServices;
	
	
	public GroupFileSending(String url,String username) {
		this.url = url;
		this.username = username;
	}
	
	
	@Override
	public void run() {
		userThread();
	}
	
	private void userThread(){
		
		JSONObject object = new JSONObject();
		try {
			object.put("ToolName", "url");
			object.put("url", url);
			object.put("username", username);
			
			String sending = object.toString();
			services.sendDrawingData(sending);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
