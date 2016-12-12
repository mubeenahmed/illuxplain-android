package org.uni.illuxplain.canvas;

import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;

public class ObjectDeleteMapper extends Thread{

	
	private int xAxis;
	private int yAxis;
	
	private String tool;
	private DrawingXmppServices drawing = CanvasActivity.drawingServices;
	
	public ObjectDeleteMapper(String toolName, int xAxis,int yAxis){
		tool = toolName;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}
	
	@Override
	public void run() {
		userObject();
	}
	
	synchronized private void userObject(){
		JSONObject user = new JSONObject();
		try {
			user.put("ToolName", tool);
			user.put("X-Axis", xAxis);
			user.put("Y-Axis", yAxis);
			
			String drawingData = user.toString();
			drawing.sendDrawingData(drawingData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
	}
}