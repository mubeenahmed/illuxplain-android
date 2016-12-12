package org.uni.illuxplain.welcomescreen.services;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class MessageVolley extends Request<JSONObject>{

	private Map<String,String> params;
	private ErrorListener errorListener;
	private Listener<String> onResponse;
	
	public MessageVolley(int method, String url, Listener<String> listener2, ErrorListener listener, HashMap<String,String> params) {
		super(method, url, listener);
		
		errorListener = listener;
		this.params = params;
	}
	
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}


	@Override
	protected void deliverResponse(JSONObject json) {
		
	}


	@Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
           
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(),HttpHeaderParser.parseCacheHeaders(response));

		} catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } 
    }    

}
