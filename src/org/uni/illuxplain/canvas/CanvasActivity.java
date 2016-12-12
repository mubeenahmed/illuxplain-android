package org.uni.illuxplain.canvas;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.canvas.SurfaceViewCanvas.OnReceivedText;
import org.uni.illuxplain.canvas.dialog.MessageViewSessionDialog;
import org.uni.illuxplain.canvas.fragments.DrawingToolsFragment;
import org.uni.illuxplain.canvas.fragments.DrawingToolsFragment.OnDrawingToolSelected;
import org.uni.illuxplain.canvas.fragments.FeatureToolsFragment;
import org.uni.illuxplain.canvas.fragments.FeatureToolsFragment.OnFeatureClickListener;
import org.uni.illuxplain.canvas.fragments.IlluxplainToolsFragment;
import org.uni.illuxplain.canvas.fragments.IlluxplainToolsFragment.DrawingToolsClickListener;
import org.uni.illuxplain.canvas.fragments.ObjectToolsFragment;
import org.uni.illuxplain.canvas.fragments.ObjectToolsFragment.OnObjectToolsListener;
import org.uni.illuxplain.canvas.fragments.PDFOptionsFragment;
import org.uni.illuxplain.canvas.fragments.PDFOptionsFragment.PDFPageListener;
import org.uni.illuxplain.group.chat.GroupChat;
import org.uni.illuxplain.group.chat.GroupUsersDailog;
import org.uni.illuxplain.group.chat.ShowParticipants;
import org.uni.illuxplain.opentok.MultipartyActivity;
import org.uni.illuxplain.opentok.OpenTokConfig;
import org.uni.illuxplain.welcomescreen.HomeFragment;
import org.uni.illuxplain.xmpp.file.FileTransferOneToOne;
import org.uni.illuxplain.xmpp.file.FileTransferOneToOne.OnFileTransferOneToOne;
import org.uni.illuxplain.xmpp.file.FileTransferVolley;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices;
import org.uni.illuxplain.xmpp.services.DrawingXmppServices.OnDrawingRecieved;
import org.uni.jain.illuxplain.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.qoppa.android.pdfProcess.PDFPage;

public class CanvasActivity extends AppCompatActivity implements
		DrawingToolsClickListener, OnDrawingToolSelected,
		OnFeatureClickListener,OnDrawingRecieved, OnObjectToolsListener,OnFileTransferOneToOne,OnReceivedText,PDFPageListener {

	private ImageButton optionsButton;
	private SurfaceViewCanvas canvas;
	private Toolbar toolbar;
	private Fragment fragment;
	
	private static final int REQUEST_CODE = 200;
	public static final String WEB_SERVICE_URL= "http://192.168.1.3:8082/IlluxplainWebServices/GroupUploadServlet";

	private boolean isMarker = true;
	private boolean isLiner = false;
	private boolean isEraser = false;
	private boolean isToogleButton = false;
	
	private ImageButton incomingText;
	
	private MessageViewSessionDialog mDialog;
	
	public static DrawingXmppServices drawingServices;
	private GroupChat chat;
	
	private FileTransferOneToOne oneToOne;
	
	private MultipartyActivity dialogVoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canvas);

		optionsButton = (ImageButton) findViewById(R.id.show_tools);
		canvas = (SurfaceViewCanvas) findViewById(R.id.surface_view_canvas);
		toolbar = (Toolbar) findViewById(R.id.tools);
		incomingText = (ImageButton) findViewById(R.id.incoming_message);
		
		
		setSupportActionBar(toolbar);
		
		if(savedInstanceState == null){
			init();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return menuItem(item);
			}
		});
	}
	
	private void init(){
		
		dialogVoice = new MultipartyActivity(this);
		oneToOne = new FileTransferOneToOne(this);
		
		if(GroupUsersDailog.users.size() > GroupChat.LENGTH_OF_ROOM){
				Toast.makeText(this, "less than 10 users are allowed", Toast.LENGTH_LONG).show();
				finish();
		}
			else {
				if(!GroupChat.isAccept){
					chat = HomeFragment.groupChat;
					chat.getGroupChatDomain();
					
					chat.setUsers(GroupUsersDailog.users, OpenTokConfig.sessionId);
				}
				drawingServices = new DrawingXmppServices(this, canvas);
		}
		dialogVoice.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.canvas, menu);
		return true;
	}

	public void onOptionToolsClicked(View v) {
		if (!isToogleButton) {
			openOptionButton();
		}
		else{
			closeOptionButton();
		}
	}
	
	private void openOptionButton(){
		optionsButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_option_arrow_closer));
		fragment = new IlluxplainToolsFragment();
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		trans.add(R.id.tools, fragment);
		trans.commit();
		isToogleButton = true;
	}
	
	private void closeOptionButton(){
		optionsButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_option_arrow));
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		trans.remove(fragment);
		trans.commit();
		isToogleButton = false;
	}


	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE) {
				Uri uri = data.getData();
				if (uri == null) {
					return;
				}
				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
					bitmap = Bitmap.createScaledBitmap(bitmap,canvas.getWidth(),canvas.getHeight(), true);
					String mUri = getPath(uri);
					canvas.setBitmap(bitmap);

					List<String> participantID = GroupChat.getParticipantJID();

					if (participantID != null) {
						if (participantID.size() == 1) {
							// Create One To One File Transfer
//							FileTransferOneToOne oneToOne = new FileTransferOneToOne(this);
							oneToOne.sendImage(participantID.get(0), mUri);
						} else if (participantID.size() == 0) {
							Toast.makeText(this,"Loading Bitmap. But There is no user connected",Toast.LENGTH_LONG).show();
						}
						else if(participantID.size() > 1) {
							FileTransferVolley volley = new FileTransferVolley(this, mUri, WEB_SERVICE_URL);
							volley.shareFile();
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mDialog = new MessageViewSessionDialog(this);
		mDialog.show();
		mDialog.dismiss();
		
	}
	
	private boolean menuItem(MenuItem item){
		int id = item.getItemId();

	
		if (id == R.id.show_participants) {
			ShowParticipants participants = new ShowParticipants(this);
			participants.setTitle("Users In Group");
			participants.show();
			
			return true;
		}
		if (id == R.id.file_transfer) {
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(i, "Choose Image/PDF"),REQUEST_CODE);
			return true;
		}

		if (id == R.id.action_settings) {
			return true;
		}
		return true;
	
	}
	
	private String getPath(Uri uri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Leaving Session");
		builder.setMessage("Are You Leaving Session?");
		builder.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				try {
					dialogVoice.disconnectSession();
					DrawingXmppServices.muc.leave();
					try {
						DrawingXmppServices.muc.destroy("End", "");
					} catch (NoResponseException | XMPPErrorException e) {
						e.printStackTrace();
					}
					GroupUsersDailog.isGrouped = false;
					finish();
					dialog.dismiss();
				} catch (NotConnectedException e) {
					e.printStackTrace();
				}
			}
		});

		builder.setNegativeButton("No", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onDrawingOptionsDemand(ImageButton v) {
		if (v.getId() == R.id.drawing_tools_options) {
			// Drawing Tools Fragment add
			fragment = new DrawingToolsFragment();
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.tools, fragment);
			transaction.commit();
		} else if (v.getId() == R.id.objects_tools_options) {
			// Objects Tools Fragment add
			fragment = new ObjectToolsFragment();
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.tools, fragment);
			transaction.commit();

		} 
		else if(v.getId() == R.id.pdf_tools_options){
			
			fragment = new PDFOptionsFragment();
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.tools, fragment);
			transaction.commit();
		}
		else {
			// Features Tools Fragment add
			fragment = new FeatureToolsFragment();
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.tools, fragment);
			transaction.commit();

		}
	}

	@Override
	public void onMarkerSelection() {
		isMarker = true;
		isEraser = false;
		isLiner = false;

		canvas.setEraser(isEraser);
		canvas.setMarker();
	}

	@Override
	public void onLinerSelection() {
		isEraser = false;
		isLiner = false;
		isMarker = true;

		canvas.setEraser(isEraser);
		canvas.setLiner();
	}

	@Override
	public void onEraserSelection() {
		isEraser = true;
		isLiner = false;
		isMarker = false;
		
		canvas.setEraser(isEraser);
	}
	
	public void onVideoAudioDialog(View v){
		dialogVoice.show();
	}

	@Override
	public void onColorChanges(String colorName) {
		if (isLiner) {
			canvas.setUserColor(colorName);
			canvas.setLiner();
		}

		else if (isMarker) {
			canvas.setUserColor(colorName);
			canvas.setMarker();
		} else {

		}
	}

	@Override
	public void onCameraSelection() {
		// Start Camera..
	}

	@Override
	public void onStartNewSelection() {
		canvas.startNew();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if(fragment != null){
			closeOptionButton();
		}

	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onSaveSelection() {
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
		saveDialog.setTitle("Save drawing");
		saveDialog.setMessage("Save drawing to device Gallery?");
		saveDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String imgSaved = MediaStore.Images.Media.insertImage(
								getContentResolver(), canvas.getDrawingCache(),
								UUID.randomUUID().toString() + ".png",
								"drawing");
					}
				});
		saveDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		saveDialog.show();

	}

	@Override
	public void onRecevieDrawing(String from, String drawing) {
		canvas.onReceiveDrawingMessage(from, drawing);
	}

	@Override
	public void displayOjbect(String tool) {
		canvas.displayObject(tool);
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("username", GlobalApplication.username);
		outState.putString("userurl", GlobalApplication.userURL);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		String usr = savedInstanceState.getString("username");
		String url = savedInstanceState.getString("userurl");
		
		
		GlobalApplication.username = usr;
		GlobalApplication.userURL = url;
		
	}

	@Override
	public void incomingOneToOneFile(String fileName) {
		canvas.onFileReceived(fileName);
	}

	public void onIncomingMessage(View v) {
		// Button Touch therefore create fragment..
		mDialog.show();
		mDialog.setCanceledOnTouchOutside(false);
	}
	
	public void onBackPressDialog(){
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				incomingText.setImageDrawable(ContextCompat.getDrawable(CanvasActivity.this, R.drawable.ic_conversation));
				incomingText.invalidate();
			}
			
		});
	}
	
	@Override
	public void incomingTextMessage(final String from, final String message) {
		// Incoming Text Message
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				mDialog.updateTextList(from,message);
				incomingText.setImageDrawable(ContextCompat.getDrawable(CanvasActivity.this, R.drawable.ic_conversation_coming));
				incomingText.invalidate();
			}
			
		});
		
	}


	@Override
	public void getPageBitmap(PDFPage page) {
		 // Create a bitmap and canvas to draw the page into
//        int width = (int)Math.ceil (page.getDisplayWidth());
//        int height = (int)Math.ceil(page.getDisplayHeight());
        
        canvas.displayPage(page);
        
	}

	@Override
	public void onWidthChanges(int width) {
		canvas.setChangedWidthd(width);
	}
}
