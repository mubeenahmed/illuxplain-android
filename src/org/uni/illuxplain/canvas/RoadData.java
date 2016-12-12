package org.uni.illuxplain.canvas;

import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;

public class RoadData extends Thread {

	private String toolName;
	private float startx;
	private float starty;
	private float ex;
	private float ey;
	
	private DrawingXmppServices services = CanvasActivity.drawingServices;
	
	public RoadData(String toolName,float sx , float sy,float ex , float ey) {
		
		this.toolName = toolName;
		this.startx = sx;
		this.starty = sy;
		this.ex = ex;
		this.ey = ey;
	}
	
	@Override
	public void run() {
		userRoad();
	}
	
	private void userRoad(){
		JSONObject object = new JSONObject();
		try {
			object.put("ToolName", toolName);
			object.put("StartX", startx);
			object.put("StartY", starty);
			object.put("EndX", ex);
			object.put("EndY", ey);
			
			String data = object.toString();
			services.sendDrawingData(data);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
