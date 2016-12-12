package org.uni.illuxplain.canvas.fragments;

import java.io.File;

import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.xmpp.file.FileTransferVolley;
import org.uni.jain.illuxplain.R;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.qoppa.android.pdf.PDFException;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.android.pdfProcess.PDFPage;
import com.qoppa.android.pdfViewer.fonts.StandardFontTF;


public class PDFOptionsFragment extends Fragment{
	
	public interface PDFPageListener{
		public void getPageBitmap(PDFPage page);
	}
	
	private ImageButton right;
	private ImageButton left;
	private Button renderer;
	private Button getPdf;
	private EditText pageNumber;
	private EditText selectedPDFName;
	private int pageCount = 0;
	private String pdfFileName;
	
	private PDFPageListener listener;
	private static final String WEB_SERVICE_URL= "http://192.168.1.3:8082/IlluxplainWebServices/GroupUploadServlet";
	
	
	private File file;
	private String root;
	private int number;
	private String filePath;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.pdf_options_fragment, container,false);
		
		
		right = (ImageButton) v.findViewById(R.id.arrow_right);
		left = (ImageButton) v.findViewById(R.id.arrow_left);
		renderer = (Button) v.findViewById(R.id.renderer);
		getPdf = (Button) v.findViewById(R.id.get_pdf);
		pageNumber = (EditText) v.findViewById(R.id.page_number);
		selectedPDFName = (EditText) v.findViewById(R.id.pdf_name);
		
		listener = (PDFPageListener) getActivity();
		
		file = Environment.getExternalStorageDirectory();
		root = file.getAbsolutePath();
		
		filePath = root+"/illuxplain/output.jpg";
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();

		
		pageNumber.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				if(actionId == EditorInfo.IME_ACTION_DONE){
					getPdfPageByNumber();
					return true;
				}
				return true;
			}
		});
		
		getPdf.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String pdfName = selectedPDFName.getText().toString();
				if(!pdfName.equals("")){
					
					File extStore = Environment.getExternalStorageDirectory();
					
					File myFile1 = new File(extStore.getAbsolutePath()+"/"+pdfName+".pdf");
					if(myFile1.exists()){
						pdfFileName = myFile1.getPath();
						getPDF(0);
						return;
					}
					
					File myFile2 = new File(extStore.getAbsolutePath()+"/"+GlobalApplication.APPLICATION_FOLDER+"/"+pdfName+".pdf");
					if(myFile2.exists()){
						pdfFileName = myFile2.getPath();
						getPDF(0);
						return;
					}
					
					
					File myFile3 = new File(extStore.getAbsolutePath()+"/"+Environment.DIRECTORY_DOWNLOADS+"/"+pdfName+".pdf");
					if(myFile3.exists()){
						pdfFileName = myFile3.getPath();
						getPDF(0);
						return;
					}
					
					
					Toast.makeText(getActivity(), "No PDF Found ..", Toast.LENGTH_LONG).show();
					
				}
				else{
					Toast.makeText(getActivity(), "Enter File Name", Toast.LENGTH_LONG).show();
				}
				
			}

		});
		
		renderer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FileTransferVolley volley = new FileTransferVolley(getActivity(), filePath , WEB_SERVICE_URL);
				volley.shareFile();
			}
		});
		
		right.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (number  == pageCount -1) {
					right.setClickable(false);
					pageNumber.setText(number+"");
				}
				else if(number  < pageCount -1){
					left.setClickable(true);
					number++;
					pageNumber.setText(number+"");
					getPDF(number);
				}
			}
			
		});
		
		left.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(number == 0){
					left.setClickable(false);
					pageNumber.setText(number+"");
				}
				else{
					right.setClickable(true);
					number--;
					pageNumber.setText(number+"");
					getPDF(number);
					
				}
			}
			
		});
	}
	
	
	private void getPdfPageByNumber(){
		number  = Integer.parseInt(pageNumber.getText().toString());
		if(number > pageCount){
			Toast.makeText(getActivity(), "There are "+pageCount+" pages in Document", Toast.LENGTH_LONG).show();
		}
		else{
			getPDF(number);
		}
		
	}
	
	public void getPDF(int number){
		StandardFontTF.mAssetMgr = getActivity().getAssets();
		
         
         try {
        	//Selecting file from user is not implemented..
			PDFDocument pdf = new PDFDocument(pdfFileName, null);
			PDFPage page = pdf.getPage(number);
			pageCount = pdf.getPageCount();
			
			selectedPDFName.setText(pdf.getName()+"("+pageCount+")");
			listener.getPageBitmap(page);
		
            
		} catch (PDFException e) {
			Toast.makeText(getActivity(), "No PDF file Found ", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//	}


}








