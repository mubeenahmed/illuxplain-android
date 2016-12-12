package org.uni.illuxplain.opentok;


import org.uni.jain.illuxplain.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MultipartyActivity extends Dialog {

    public MultipartyActivity(Context context) {
		super(context);
		this.context = context;
	}

	private static final String LOGTAG = "demo-subclassing";

    private MySession mSession;
    private EditText mMessageEditText;
    private boolean resumeHasRun = false;
    
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	
        super.onCreate(savedInstanceState);

        setContentView(R.layout.room);

        mSession = new MySession(context);
        mMessageEditText = (EditText) findViewById(R.id.message);
        ViewGroup preview = (ViewGroup) findViewById(R.id.preview);
        mSession.setPreviewView(preview);


        ViewPager playersView = (ViewPager) findViewById(R.id.pager);
        mSession.setPlayersViewContainer(playersView);
        mSession.setMessageView((TextView) findViewById(R.id.messageView), (ScrollView) findViewById(R.id.scroller));

        if(savedInstanceState == null){
        	init();
        }
      
    }
    
    private void init(){
    	mSession.connect();
    }
    
    public void disconnectSession(){
    	mSession.disconnect();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if (mSession != null) {
//            mSession.onPause();
//        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//        if (!resumeHasRun) {
//            resumeHasRun = true;
//            return;
//        } else {
//            if (mSession != null) {
//                mSession.onResume();
//            }
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        if (isFinishing()) {
//            if (mSession != null) {
//                mSession.disconnect();
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//
//        if (mSession != null) {
//            mSession.disconnect();
//        }
//
//        super.onDestroy();
//        finish();
//    }

//    @Override
//    public void onBackPressed() {
//        if (mSession != null) {
//            mSession.disconnect();
//        }
//
//        super.onBackPressed();
//    }

//    public void onClickSend(View v) {
//        if (mMessageEditText.getText().toString().compareTo("") == 0) {
//            Log.i(LOGTAG, "Cannot Send - Empty String Message");
//        } else {
//            Log.i(LOGTAG, "Sending a chat message");
//            mSession.sendChatMessage(mMessageEditText.getText().toString());
//            mMessageEditText.setText("");
//        }
//    }

}
