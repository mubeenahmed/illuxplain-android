package org.uni.illuxplain.welcomescreen.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.welcomescreen.ProfileFragment;
import org.uni.jain.illuxplain.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class FullScreenDisplayActivity extends AppCompatActivity {

	private ImageView fullImage;
	private static final int REQUEST_CODE = 1;

	private Uri uri;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_full_screen);

		fullImage = (ImageView) findViewById(R.id.full);
		if(GlobalApplication.bitmap !=null){
			fullImage.setImageBitmap(GlobalApplication.bitmap);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		
	}

	public void selectImage(View v) {
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Choose Image/PDF"),
				REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE) {

				uri = data.getData();
				if (uri == null) {
					return;
				}
				String mUri = getPath(uri);
				uploadImage(mUri);
			}
		}
	}

	private void uploadImage(String mUri) {

		new AsyncTask<String, Integer, Bitmap>() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = new ProgressDialog(FullScreenDisplayActivity.this);
				dialog.setMessage("Uploading Image");
				dialog.show();
				dialog.setCancelable(false);
				dialog.setProgress(0);
			}

			@Override
			protected Bitmap doInBackground(String... params) {

				HttpURLConnection connection = null;
				DataOutputStream outputStream = null;
				DataInputStream inputStream = null;
				String pathToOurFile = params[0];
				String urlServer = "http://illuxplain-mubeen.rhcloud.com/UploadServlet";
				String lineEnd = "\r\n";
				String twoHyphens = "--";
				String boundary = "*****";

				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 1 * 1024 * 1024;

				try {
					FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));

					URL url = new URL(urlServer);
					connection = (HttpURLConnection) url.openConnection();

					// Allow Inputs &amp; Outputs.
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setUseCaches(false);

					// Set HTTP method to POST.
					connection.setRequestMethod("POST");

					connection.setRequestProperty("Connection", "Keep-Alive");
					connection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

					outputStream = new DataOutputStream(
							connection.getOutputStream());
					outputStream.writeBytes(twoHyphens + boundary + lineEnd);
					outputStream.writeBytes("Content-Disposition: form-data; name=\""+GlobalApplication.username+"\";filename=\""
									+pathToOurFile
									+ "\""
									+ lineEnd);
					outputStream.writeBytes(lineEnd);
					

					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					// Read file
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

					while (bytesRead > 0) {
						outputStream.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}

					outputStream.writeBytes(lineEnd);
					outputStream.writeBytes(twoHyphens + boundary + twoHyphens
							+ lineEnd);

					int response = connection.getResponseCode();
					
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					options.inSampleSize = 3;

					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
					
					fileInputStream.close();
					outputStream.flush();
					outputStream.close();
					return bitmap;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				dialog.setProgress(values[0]);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				dialog.dismiss();
				if(result!=null){
					GlobalApplication.bitmap = result;
					fullImage.setImageBitmap(result);
				}
				else{
					fullImage.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_user_default));
				}
				
			}
		}.execute(mUri);
	}

	private String getPath(Uri uri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		if (cursor.moveToFirst()) {

			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}
}
