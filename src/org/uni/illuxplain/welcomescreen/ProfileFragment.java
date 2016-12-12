package org.uni.illuxplain.welcomescreen;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;
import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.canvas.SurfaceViewCanvas;
import org.uni.illuxplain.welcomescreen.services.FullScreenDisplayActivity;
import org.uni.illuxplain.welcomescreen.services.ProfileInfo;
import org.uni.jain.illuxplain.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

	public ImageView pic;
	public TextView username;
	public TextView age;
	public TextView gender;
	public TextView education;
	public TextView work;
	public TextView professional;
	
	private Bitmap bitmap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_fragment, container,
				false);

		pic = (ImageView) rootView.findViewById(R.id.profile_picture);
		username = (TextView) rootView.findViewById(R.id.edit_username);
		age = (TextView) rootView.findViewById(R.id.edit_age);
		gender = (TextView) rootView.findViewById(R.id.edit_gender);
		education = (TextView) rootView.findViewById(R.id.edit_education);
		work = (TextView) rootView.findViewById(R.id.edit_work);
		professional = (TextView) rootView.findViewById(R.id.edit_professional);

		new GetUserInfo().execute(GlobalApplication.username);

		pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),FullScreenDisplayActivity.class);
				startActivity(intent);
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(GlobalApplication.bitmap != null){
			pic.setImageBitmap(GlobalApplication.bitmap);
		}
	}

	private class GetUserInfo extends AsyncTask<String, Void, String> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Waiting.. Loading Some Data");
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			URL url;
			String result = null;
			try {
				url = new URL(
						"http://illuxplain-mubeen.rhcloud.com/webservice/illuxplain/profile/info/"
								+ params[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setReadTimeout(10000);
				connection.setRequestMethod("GET");
				int response = connection.getResponseCode();

				if (response == HttpURLConnection.HTTP_OK) { // success
					BufferedReader in = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String inputLine;
					StringBuffer s = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						s.append(inputLine);
					}
					in.close();
					result = s.toString();
					JSONObject o;
					try {
						o = new JSONObject(result);
						String picurl = ProfileInfo.pic = o.getString("pic");
						ProfileInfo.username = GlobalApplication.username;
						ProfileInfo.age = o.getString("age");
						ProfileInfo.gender = o.getString("gender");
						ProfileInfo.education = o.getString("education");
						ProfileInfo.work = o.getString("work");
						ProfileInfo.professional = o.getString("profess");

						getImageFromServer(picurl);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		private void getImageFromServer(String picurl) {
			try {
				URL url = new URL("http://192.168.1.3:8082/IlluxplainWebServices/uploads/"+picurl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(30000);
	            conn.setReadTimeout(30000);
	            conn.setDoInput(true);
	            conn.setInstanceFollowRedirects(true);
	            InputStream is=conn.getInputStream();
				
	           BitmapFactory.Options options = new BitmapFactory.Options();
	           options.inSampleSize =8;
	           options.inJustDecodeBounds = true;

	           BufferedInputStream bis = new BufferedInputStream(is);

	           ByteArrayBuffer baf = new ByteArrayBuffer(50);
	           int current = 0;
	           while ((current = bis.read()) != -1) {
	               baf.append((byte)current);
	           }
	           byte[] imageData = baf.toByteArray();
	           
	           bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
	           conn.disconnect();
	             
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			username.setText(ProfileInfo.username);
			age.setText(ProfileInfo.age);
			gender.setText(ProfileInfo.gender);
			education.setText(ProfileInfo.education);
			work.setText(ProfileInfo.work);
			professional.setText(ProfileInfo.professional);
			
			if(bitmap !=null){
				pic.setImageBitmap(bitmap);
				GlobalApplication.bitmap = bitmap;
			}
			else{
				pic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_user_default));
			}
			progressDialog.dismiss();

		}
	}

}
