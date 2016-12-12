package org.uni.illuxplain.canvas;

import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;
import org.uni.illuxplain.xmpp.services.XmppManager;

import android.util.Log;

public class LinerMapping extends Thread {

	private String startX;
	private String startY;
	private String endX;
	private String endY;

	private JSONObject jsonObject;
	public String colorName;
	public String toolName;
	public String strokeWidth;
	private DrawingXmppServices drawing = CanvasActivity.drawingServices;

	public LinerMapping(String toolName, String colorName, String sX,String sY, String eX, String eY,String strokWidth) {
		jsonObject = new JSONObject();
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;

		this.toolName = toolName;
		this.colorName = colorName;
		this.strokeWidth = strokWidth;

	}

	@Override
	public void run() {
		userLine();
	}

	private void userLine() {
		try {
			jsonObject.put("ToolName", toolName);
			jsonObject.put("ColorName", colorName);
			jsonObject.put("StrokeWidth", strokeWidth);
			jsonObject.put("StartX", startX);
			jsonObject.put("StartY", startY);
			jsonObject.put("EndX", endX);
			jsonObject.put("EndY", endY);
			
			String drawingData = jsonObject.toString();
			drawing.sendDrawingData(drawingData);
			Log.v("Is it right? ", jsonObject.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
