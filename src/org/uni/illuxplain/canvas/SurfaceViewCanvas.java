package org.uni.illuxplain.canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.xmpp.services.XmppManager;
import org.uni.jain.illuxplain.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.qoppa.android.pdf.PDFException;
import com.qoppa.android.pdfProcess.PDFPage;

public class SurfaceViewCanvas extends View implements ICanvasObjects,XmppManager.DrawingReceiver {

	public interface OnReceivedText{
		public void incomingTextMessage(String from,String message);
	}
	
	private static final String TOOL_NAME1 = "marker";
	private static final String TOOL_NAME2 = "eraser";
	private static final String TOOL_NAME3 = "liner";
	private static final String TOOL_GENERAL_OBJECT = "object";
	private static final String TOOL_NAME4 = "office_building";
	private static final String TOOL_NAME5 = "house_building";
	private static final String TOOL_NAME6 = "city_building";
	private static final String TOOL_NAME7 = "mobile_phone";
	private static final String TOOL_NAME8 = "human_men";
	private static final String TOOL_NAME9 = "human_women";
	private static final String TOOL_NAME11 = "deleter";
	private static final String TOOL_NAME12 = "road";
	private static final String TOOL_NAME13 = "car";
	private static final String TOOL_NAME14 = "bridge_building";
	
	private static final String TOOL_NAME_URL = "url";
	

	private static String objectName = "no object";
	
	private OnReceivedText text;
	
	public static int screenWidth;
	public static int screenHeight;
	public static List<String> incomingMessages = new ArrayList<String>();

	private Path drawPath;
	private Canvas canvasDraw;

	private Bitmap canvasBitmap;
	private Paint canvasPaint, drawPaint;

	private String userTrackerX = null;
	private String userTrackerY = null;
	private String toolName;
	private String colorName;
	private int strokeWidth;

	private MarkerAndEraserMapping jsonMapping;
	private JSONObject jsonObject;
	private boolean isEraser = false;
	private boolean isMarker = true;
	private boolean isLiner = false;

	private static float startX = 0;
	private static float startY = 0;
	private static int OBJECT_OFFSET_X = 70;
	private static int OBJECT_OFFSET_Y = 70;

	private static final float ROAD_WIDTH = 15;
	private boolean handled = false;
	private boolean isPre = false;
	private static int prex = 0;
	private static int prey = 0;

	private HashSet<ObjectArea> mObject = new HashSet<ObjectArea>();
	private SparseArray<ObjectArea> mObjectPointer = new SparseArray<ObjectArea>();
	

	public SurfaceViewCanvas(Context context) {
		super(context);
		init();
	}

	public SurfaceViewCanvas(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}

	public SurfaceViewCanvas(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init();
	}

	private void init() {
		drawPath = new Path();
		drawPaint = new Paint();
		strokeWidth = 5;
		colorName = "black";
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		setMarker();
	}
	
	public void setChangedWidthd(int wi){
		this.strokeWidth = wi;
		
		if(isLiner){
			setLiner();
		}
		else if(isMarker){
			setMarker();
		}
		
	}
	
	public void setMarker() {
		this.toolName = TOOL_NAME1;
		drawPaint.setColor(Color.parseColor(colorName));
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(strokeWidth);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		isLiner = false;
		isMarker = true;
		isEraser = false;
	}

	public void setLiner() {
		this.toolName = TOOL_NAME3;
		drawPaint.setColor(Color.parseColor(colorName));
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(strokeWidth);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		isLiner = true;
		isMarker = false;
		isEraser = false;
	}

	@Override
	public void setUserColor(String userColor) {
		this.colorName = userColor;
	}

	@Override
	public void setEraser(boolean eraser) {
		this.toolName = TOOL_NAME2;
		isEraser = eraser;
		if (isEraser){
//			setMarker();
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		}
		else{
//			setMarker();
			drawPaint.setXfermode(null);
		}
	}

	@Override
	public void startNew() {
		canvasDraw.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);

		if (objectName != null) {
			if (objectName.equals(TOOL_NAME4)) {
				drawObject(canvas);
			}
			else if (objectName.equals(TOOL_NAME5)) {
				drawObject(canvas);
			} 
			else if (objectName.equals(TOOL_NAME6)) {
				drawObject(canvas);
			}
			else if (objectName.equals(TOOL_NAME7)) {
				drawObject(canvas);
			} 
			else if (objectName.equals(TOOL_NAME8)) {
				drawObject(canvas);
			}
			else if (objectName.equals(TOOL_NAME9)) {
				drawObject(canvas);
			}
			else if(objectName.equals(TOOL_NAME13)){
				drawObject(canvas);
			}
			else{
				drawObject(canvas);
			}
			
		}
	}

	private void deleteObject(int x,int y, boolean isOnUIThread) {
		for (ObjectArea area: mObject) {
			//if(area.x1 == x && area.y1 == y){
			if ((area.x1 - OBJECT_OFFSET_X <= x && area.y1 - OBJECT_OFFSET_Y <= y) && (area.x1 >= x && area.y1 >= y)){
				mObject.remove(area);
				//objectName = TOOL_NAME5;
				if(isOnUIThread){
					invalidate();
				}
				else{
					postInvalidate();
				}
				break;
			}
		}
		
	}

	private void drawObject(Canvas canvas) {

		for (ObjectArea object : mObject) {
			if (object.objectName.equals(objectName)) {
				int res = getResources().getIdentifier("ic_"+object.objectName, "drawable", getContext().getPackageName());
				Drawable d = ContextCompat.getDrawable(getContext(),res);
				
				d.setBounds(new Rect(object.x1 - OBJECT_OFFSET_X, object.y1 - OBJECT_OFFSET_Y, object.x1, object.y1));
				d.draw(canvas);
			} else {
				int res = getResources().getIdentifier("ic_"+object.objectName, "drawable", getContext().getPackageName());
				Drawable d = ContextCompat.getDrawable(getContext(),res);
				
				d.setBounds(new Rect(object.x1 - OBJECT_OFFSET_X, object.y1- OBJECT_OFFSET_Y, object.x1, object.y1));
				d.draw(canvas);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		screenWidth = w;
		screenHeight = h;
		
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvasDraw = new Canvas(canvasBitmap);
	}

	void setBitmap(Bitmap mImage) {
		canvasDraw.drawBitmap(mImage, 0, 0, null);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		int _x = (int) x;
		int _y = (int) y;

		int pointerId;
		ObjectArea area = null;
		int actionIndex = event.getActionIndex();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (toolName.equals(TOOL_NAME1) || toolName.equals(TOOL_NAME2)) {
				drawPath.moveTo(x, y);
				userTrackerX = "" + x;
				userTrackerY = "" + y;
				invalidate();
			} 
			
			
			else if (toolName.equals(TOOL_NAME3)) {
				startX = event.getX();
				startY = event.getY();
			}
			
			else if (objectName.equals(TOOL_NAME11)) {
				deleteObject(_x, _y, true);
				ObjectDeleteMapper mapper = new ObjectDeleteMapper(TOOL_NAME11, _x, _y);
				mapper.start();
			}
			
			else if(objectName.equals(TOOL_NAME12)){
				//draw road;
				startX = event.getX();
				startY = event.getY();
				
			}
			
			else if (toolName.equals(TOOL_GENERAL_OBJECT)) {
				clearCirclePointer();

				_x = (int) event.getX(0);
				_y = (int) event.getY(0);

				// check if we've touched inside some circle
				area = obtainTouchedCircle(_x, _y);
				area.x1 = _x;
				area.y1 = _y;

				mObjectPointer.put(event.getPointerId(0), area);

				invalidate();
				handled = true;
				break;
			}
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			// It secondary pointers, so obtain their ids and check objects
			pointerId = event.getPointerId(actionIndex);

			_x = (int) event.getX(actionIndex);
			_y = (int) event.getY(actionIndex);

			// check if we've touched inside some circle
			area = obtainTouchedCircle(_x, _y);

			mObjectPointer.put(pointerId, area);
			area.x1 = _x;
			area.y1 = _y;
			invalidate();
			handled = true;
			break;

		case MotionEvent.ACTION_MOVE:
			if (toolName.equals(TOOL_NAME1) || toolName.equals(TOOL_NAME2)) {
				drawPath.lineTo(x, y);
				userTrackerX += " " + x + " ";
				userTrackerY += " " + y + " ";
				invalidate();
			} 
			else if (toolName.equals(TOOL_GENERAL_OBJECT)) {
				final int pointerCount = event.getPointerCount();

				for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
					// Some pointer has moved, search it by pointer id
					pointerId = event.getPointerId(actionIndex);

					_x = (int) event.getX(actionIndex);
					_y = (int) event.getY(actionIndex);

					area = mObjectPointer.get(pointerId);

					if (null != area) {
						area.x1 = _x;
						area.y1 = _y;
					}
				}

				invalidate();
				handled = true;
				break;
			}

			break;

		case MotionEvent.ACTION_UP:
			if (toolName.equals(TOOL_NAME1) || toolName.equals(TOOL_NAME2)) {
				canvasDraw.drawPath(drawPath, drawPaint);
				invalidate();
				jsonMapping = new MarkerAndEraserMapping(toolName, colorName,""+strokeWidth,userTrackerX, userTrackerY);
				jsonMapping.start();
				userTrackerX = null;
				userTrackerX = null;
				// call the json mapping and network operation ...

			} else if (toolName.equals(TOOL_NAME3)) {
				float endX = event.getX();
				float endY = event.getY();
				LinerMapping liner = new LinerMapping(toolName, colorName, ""+ startX, "" + startY, "" + endX, "" + endY,""+strokeWidth);
				liner.start();
				canvasDraw.drawLine(startX, startY, endX, endY, drawPaint);
				invalidate();
			} 
			
			else if (objectName != null) {
				
				if(objectName.equals(TOOL_NAME12)){
					float endX = event.getX();
					float endY = event.getY();
					buildRoad(startX, startY, endX,endY, true);
					
				}
				
				else if (toolName.equals(TOOL_GENERAL_OBJECT)) {
					clearCirclePointer();
					invalidate();
					handled = true;

					ObjectMapping objectMapper = new ObjectMapping(objectName, _x,_y, prex, prey , isPre);
					objectMapper.start();
					prex = 0;
					prey = 0;
				}
			}
			drawPath.reset();
			break;

		case MotionEvent.ACTION_POINTER_UP:
			// not general pointer was up
			pointerId = event.getPointerId(actionIndex);
			mObjectPointer.remove(pointerId);
			invalidate();
			handled = true;
			break;

		case MotionEvent.ACTION_CANCEL:
			handled = true;
			break;
		}

		return true;
	}
	
	private void buildRoad(float startX, float startY, float endX, float endY,boolean isUIThread){
		//find x and y 
		float finalX = endX - startX;
		float finalY = endY - startY;
		
		float roadLength = finalX*finalX + finalY*finalY;
		roadLength = (float) Math.sqrt((double)roadLength);
		double roadAngle = Math.toDegrees(Math.atan2(finalY, finalX));
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inMutable = true;
		Matrix matrix = new Matrix();
		
		matrix.setTranslate(startX, startY);
		matrix.preRotate((float)roadAngle);
		
		Resources res = getContext().getResources();
		Bitmap roadBitmap = BitmapFactory.decodeResource(res, R.drawable.ic_road, options);
		
		int bitmapWeight = roadBitmap.getWidth() / 4;
		
		if (roadLength == 0) {
			roadLength = bitmapWeight;
		}
		
		roadBitmap = Bitmap.createScaledBitmap(roadBitmap, (int) roadLength,bitmapWeight, false);
//		BitmapDrawable road = new BitmapDrawable(getResources(), roadBitmap);
//		mObject.add(new ObjectArea((int)finalX, (int)finalY, objectName));
//		drawObject(canvasDraw);
		
		canvasDraw.drawBitmap(roadBitmap, matrix, null);
		if (isUIThread) {
			//On UI Thread True Mean that it is the sender 
			invalidate();
			RoadData data = new RoadData(TOOL_NAME12, startX, startY,endX, endY);
			data.start();
			
		} else {
			postInvalidate();
		}
	}
	
	@Override
	public void onReceiveDrawingMessage(String from, String drawing) {
		try {
			jsonObject = new JSONObject(drawing);
			this.toolName = jsonObject.getString("ToolName");
			
		} catch (JSONException e) {
			text = (OnReceivedText) getContext();
			text.incomingTextMessage(from, drawing);
			incomingMessages.add(from + ": " + drawing);
		}

		switch (toolName) {

		case TOOL_NAME1:
			drawMarker();
			break;

		case TOOL_NAME2:
			erasePixel();
			break;

		case TOOL_NAME3:
			liner();
			break;

		case TOOL_NAME4:
			drawObject(TOOL_NAME4);
			break;

		case TOOL_NAME5:
			drawObject(TOOL_NAME5);
			break;

		case TOOL_NAME6:
			drawObject(TOOL_NAME6);
			break;

		case TOOL_NAME7:
			drawObject(TOOL_NAME7);
			break;

		case TOOL_NAME8:
			drawObject(TOOL_NAME8);
			break;
			
		case TOOL_NAME9:
			drawObject(TOOL_NAME9);
			break;
			
		case TOOL_NAME11:
			deleteObject(TOOL_NAME11);
			break;
		
		case TOOL_NAME12:
			drawRoad(TOOL_NAME12);
			break;
		
		case TOOL_NAME13:
			drawObject(TOOL_NAME13);
			break;
			
		case TOOL_NAME_URL:
			getDownload();
			break;

		default:
			break;
		}
	}
	
	private void drawRoad(String toolName) {
		try {
			float startX = jsonObject.getInt("StartX");
			float startY = jsonObject.getInt("StartY");
			float endX  = jsonObject.getInt("EndX");
			float endY = jsonObject.getInt("EndY");
			
			buildRoad(startX, startY, endX, endY,false);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private void getDownload(){
		try {
			String url = jsonObject.getString("url");
			//Call Volley And Download the File/Image;
			ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>(){

				@Override
				public void onResponse(Bitmap bitmp) {
					bitmp = Bitmap.createScaledBitmap(bitmp,screenWidth,screenHeight, true);
					onImageReceive(bitmp);
				}
				
			}, 0, 0, null, Config.ARGB_8888, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError e) {
					Toast.makeText(getContext(), "Error Receiving File ... ", Toast.LENGTH_LONG).show();
				}
			});
			
			Volley.newRequestQueue(getContext()).add(ir);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteObject(String toolName){
		try {
			int x = jsonObject.getInt("X-Axis");
			int y = jsonObject.getInt("Y-Axis");
			deleteObject(x,y,false);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void drawObject(String objectName) {
		try {
			int x = jsonObject.getInt("X-Axis");
			int y = jsonObject.getInt("Y-Axis");
			int prex = jsonObject.getInt("Pre-X");
			int prey = jsonObject.getInt("Pre-Y");
			boolean isPre = jsonObject.getBoolean("Previous");
			
			if(isPre){
				deleteObject(prex,prey);
			}
			mObject.add(new ObjectArea(x,y,objectName));
			postInvalidate();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	private void deleteObject(int px, int py){
		for (ObjectArea area: mObject) {
			if(area.x1 == px && area.y1 == py){
				mObject.remove(area);
				objectName = TOOL_NAME5;
				postInvalidate();
				break;
			}
		}
	}

	private void drawMarker() {

		String x = null;
		String y = null;
		try {
			x = jsonObject.getString("X-Axis");
			y = jsonObject.getString("Y-Axis");
			this.colorName = jsonObject.getString("ColorName");
			this.strokeWidth = Integer.parseInt(jsonObject.getString("StrokeWidth"));
			
			setUserColor(this.colorName);
			
			setMarker();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String[] xAxis = x.split("\\s+");
		String[] yAxis = y.split("\\s+");

		for (int i = 0; i < yAxis.length && i < xAxis.length; i++) {
			float mX = Float.parseFloat(xAxis[i]);
			float mY = Float.parseFloat(yAxis[i]);

			if (i == 0) { // Action_down
				drawPath.moveTo(mX, mY);
				postInvalidate();

			} 
			else { // Action_Move
				drawPath.lineTo(mX, mY);
				postInvalidate();
			}
		}
		// Action_Up
		canvasDraw.drawPath(drawPath, drawPaint);
		drawPath.reset();
		postInvalidate();
	}

	private void erasePixel() {
		String x = null;
		String y = null;
		try {
			x = jsonObject.getString("X-Axis");
			y = jsonObject.getString("Y-Axis");
			
			this.strokeWidth = Integer.parseInt(jsonObject.getString("StrokeWidth"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String[] xAxis = x.split("\\s+");
		String[] yAxis = y.split("\\s+");
		
//		drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		setEraser(true);

		for (int i = 0; i < yAxis.length && i < xAxis.length; i++) {
			float mX = Float.parseFloat(xAxis[i]);
			float mY = Float.parseFloat(yAxis[i]);

			if (i == 0) {
				drawPath.moveTo(mX, mY);
				postInvalidate();
			} else {
				drawPath.lineTo(mX, mY);
				postInvalidate();
			}
		}
		canvasDraw.drawPath(drawPath, drawPaint);
		postInvalidate();
		drawPath.reset();
	}

	private void liner() {
		String stX = null;
		String stY = null;
		String edX = null;
		String edY = null;

		
		try {
			stX = jsonObject.getString("StartX");
			stY = jsonObject.getString("StartY");
			edX = jsonObject.getString("EndX");
			edY = jsonObject.getString("EndY");

			float sx = Float.parseFloat(stX);
			float sy = Float.parseFloat(stY);
			float ex = Float.parseFloat(edX);
			float ey = Float.parseFloat(edY);
			this.strokeWidth = Integer.parseInt(jsonObject.getString("StrokeWidth"));

			this.colorName = jsonObject.getString("ColorName");
			setUserColor(this.colorName);
			setLiner();

			canvasDraw.drawLine(sx, sy, ex, ey, drawPaint);
			postInvalidate();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onImageReceive(Bitmap bitmap) {
		canvasDraw.drawBitmap(bitmap, 0, 0, null);
		postInvalidate();
	}

	@Override
	public void onStartNewCanvas() {
		mObject.clear();
		this.startNew();

	}

	public void displayObject(String tool) {
		toolName = TOOL_GENERAL_OBJECT;
		objectName = tool;
	}

	@Override
	public void onFileReceived(String fileName) {
		String filePath = Environment.getExternalStorageDirectory().toString()+ "/Illuxplain/" + fileName;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
		this.setBitmap(bitmap);

	}
	
	public void displayPage(PDFPage page){
		
		 Bitmap bm = Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
		 try {
			bm = page.getBitmap();
			bm = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, false);
			this.setBitmap(bm);
		} catch (PDFException e1) {
			e1.printStackTrace();
		}
         
         // Create canvas to draw into the bitmap
        // Canvas c = new Canvas (bm);
         
         // Fill the bitmap with a white background
         //Paint whiteBgnd = new Paint();
         //whiteBgnd.setColor(Color.WHITE);
         //whiteBgnd.setStyle(Paint.Style.FILL);
         //c.drawRect(0, 0, screenWidth, screenHeight, whiteBgnd);
         
         // paint the page into the canvas
//         try {
//			page.paintPage(c);
			
//		} catch (PDFException e1) {
//			e1.printStackTrace();
//		}
         
//         createImage(filePath, width, height);
         
         // Save the bitmap
        File file = Environment.getExternalStorageDirectory();
        String root = file.getAbsolutePath();
         
		String filePath = root + "/illuxplain";
		OutputStream outStream;
		try {
			outStream = new FileOutputStream(filePath + "/output.jpg");
			bm.compress(CompressFormat.JPEG, 80, outStream);
			outStream.close();
			
			
			//bm = BitmapFactory.decodeFile(filePath+"/output.jpg");
			//this.setBitmap(bm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void clearCirclePointer() {
		mObjectPointer.clear();
	}

	private ObjectArea obtainTouchedCircle(int x, int y) {
		ObjectArea obj = getTouchedCircle(x, y);

		if (null == obj) {
			obj = new ObjectArea(x, y, objectName);
			mObject.add(obj);
		}

		return obj;
	}

	private ObjectArea getTouchedCircle(final int xTouch, final int yTouch) {
		ObjectArea touched = null;

		for (ObjectArea obj : mObject) {

			int x2 = obj.x1 - OBJECT_OFFSET_X;
			int y2 = obj.y1 - OBJECT_OFFSET_Y;
			isPre = false;

			if ((x2 <= xTouch && y2 <= yTouch) && (obj.x1 >= xTouch && obj.y1 >= yTouch)) {
				touched = obj;
				prex = obj.x1;
				prey = obj.y1;
				objectName = obj.objectName;
				isPre = true;
				break;
			}
		}
		return touched;
	}

}
