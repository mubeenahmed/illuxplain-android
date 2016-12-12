package org.uni.illuxplain.canvas;


import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;
import org.uni.illuxplain.xmpp.services.XmppManager;

import android.util.Log;

public class MarkerAndEraserMapping extends Thread {

	
	private JSONObject jsonObject;
	
	public String userTrackerX;
	public String userTrackerY;
	public String toolName;
	public String colorName;
	public String strokeWidth;
	private DrawingXmppServices drawing = CanvasActivity.drawingServices;
	
	
	public MarkerAndEraserMapping(String toolName,String colorName, String strokeWidth, String userTrackerX, String userTrackerY) {
		jsonObject = new JSONObject();
		this.toolName = toolName;
		this.colorName = colorName;
		this.userTrackerX = userTrackerX;
		this.userTrackerY = userTrackerY;
		this.strokeWidth = strokeWidth;
	}
	
	@Override
	public void run() {
		userTracker();
	}

	synchronized public void userTracker() {
		// jsonObject //ToolName,ColorName,X-Y-Axis, Widths 
		try {
			jsonObject.put("ToolName", toolName);
			jsonObject.put("ColorName", colorName);
			jsonObject.put("StrokeWidth", strokeWidth);
			jsonObject.put("X-Axis", userTrackerX);
			jsonObject.put("Y-Axis", userTrackerY);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String drawingData = jsonObject.toString();
		drawing.sendDrawingData(drawingData);
		Log.v("Is it right? ", drawingData);
		
	}
	
}
