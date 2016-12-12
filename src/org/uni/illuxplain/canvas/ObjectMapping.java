package org.uni.illuxplain.canvas;

import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;

public class ObjectMapping extends Thread{

	
	private int xAxis;
	private int yAxis;
	private int prex;
	private int prey;
	
	private boolean isPrevious;
	
	
	private String tool;
	private DrawingXmppServices drawing = CanvasActivity.drawingServices;
	
	public ObjectMapping(String toolName, int xAxis,int yAxis,int prex,int prey, boolean isPre){
		tool = toolName;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.prex = prex;
		this.prey = prey;
		this.isPrevious = isPre;
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
			user.put("Pre-X", prex);
			user.put("Pre-Y", prey);
			user.put("Previous", isPrevious);
			
			String drawingData = user.toString();
			drawing.sendDrawingData(drawingData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
