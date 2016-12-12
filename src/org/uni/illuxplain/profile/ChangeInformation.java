package org.uni.illuxplain.profile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.uni.illuxplain.welcomescreen.services.ProfileInfo;
import org.uni.illuxplain.welcomescreen.services.ProfileUpdate;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

public class ChangeInformation {

	private ProgressDialog dialog;
	private Context context;
	private static String res;

	public ChangeInformation(Context context) {
		this.context = context;
	}

	public void changeInformation(String... newData) {

		new AsyncTask<String, Void, String>() {

			protected void onPreExecute() {
				dialog = new ProgressDialog(context);
				dialog.setMessage("Saving.... ");
				dialog.setTitle("Please Wait");
				dialog.show();
			};

			@Override
			protected String doInBackground(String... params) {
				URL url;
				String result = null;
				
				try {
					url = new URL("http://illuxplain-mubeen.rhcloud.com/webservice/illuxplain/json/user/updater");
					Uri.Builder queryBuilder = new Uri.Builder();
					queryBuilder.appendQueryParameter("gender", params[0]);
					queryBuilder.appendQueryParameter("education", params[1]);
					queryBuilder.appendQueryParameter("work", params[2]);
					queryBuilder.appendQueryParameter("professional", params[3]);	
					queryBuilder.appendQueryParameter("username", params[4]);
					String myQuery = queryBuilder.build().getEncodedQuery();
					
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setReadTimeout(10000);
					connection.setRequestMethod("PUT");
					connection.setDoOutput(false);
				//	int response = connection.getResponseCode();
						
						OutputStream output = connection.getOutputStream();
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
						writer.write(myQuery);
						writer.flush();
						writer.close();
						output.close();
						
						// success
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String inputLine;
						StringBuffer s = new StringBuffer();

						while ((inputLine = in.readLine()) != null) {
							s.append(inputLine);
						}
						in.close();
						result = s.toString();
						return result;
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return result;
			}

			protected void onPostExecute(String result) {
				res = result;
				dialog.dismiss();
				if(res.equals("update")){
					ProfileInfo.gender = ProfileUpdate.sGender;
					ProfileInfo.education = ProfileUpdate.sEducation;
					ProfileInfo.work = ProfileUpdate.sWork;
					ProfileInfo.professional = ProfileUpdate.sProfessional;
					Toast.makeText(context, "Horray! Succesfully Updated", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(context, "Opps! Something Went Wrong", Toast.LENGTH_LONG).show();
				}
			};

		}.execute(newData);
	}
}
