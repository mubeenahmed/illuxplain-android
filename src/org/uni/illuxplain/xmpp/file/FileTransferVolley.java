package org.uni.illuxplain.xmpp.file;

import java.io.File;
import java.util.HashMap;

import org.uni.illuxplain.GlobalApplication;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class FileTransferVolley {

	private Context context;
	private String mUri;
	private String webServiceUrl;
	
	public FileTransferVolley(Context context, String uri, String url){
		this.context = context;
		this.mUri = uri;
		this.webServiceUrl = url;
	}
	
	public void shareFile(){
		File file = new File(mUri);
		final String fileName = file.getName();
		HashMap<String,String> params = new HashMap<String, String>();
		
		FileTransferOneToMany oneToMany = new FileTransferOneToMany(webServiceUrl, new Response.Listener<String>() {

			@Override
			public void onResponse(String str) {
				Toast.makeText(context, "File Sending.. ", Toast.LENGTH_LONG).show();
				String sharingUrl = "http://illuxplain-mubeen.rhcloud.com/GroupUploadServlet/"+GlobalApplication.username+"_"+fileName;
				GroupFileSending fileSending = new GroupFileSending(sharingUrl, GlobalApplication.username);
				fileSending.start();
			}
			
		}, new Response.ErrorListener() {
			
			@Override
			public void onErrorResponse(VolleyError vError) {
				Toast.makeText(context, "Error Sending File .. "+ vError.getMessage(), Toast.LENGTH_LONG).show();
			}
			
		}, file , params);
		
		Volley.newRequestQueue(context).add(oneToMany);
	}
}
