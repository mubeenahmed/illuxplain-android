package org.uni.illuxplain.canvas.fragments;

import org.uni.jain.illuxplain.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class IlluxplainToolsFragment extends Fragment{

	public interface DrawingToolsClickListener{
		public void onDrawingOptionsDemand(ImageButton v);
	}
	
	private ImageButton drawing;
	private ImageButton objects;
	private ImageButton features;
	private ImageButton pdfLoader;
	
	private DrawingToolsClickListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.tools_options_layout, container, false);

		
		drawing = (ImageButton) v.findViewById(R.id.drawing_tools_options);
		objects = (ImageButton) v.findViewById(R.id.objects_tools_options);
		features = (ImageButton) v.findViewById(R.id.features_tools_options);
		pdfLoader = (ImageButton) v.findViewById(R.id.pdf_tools_options);
		
		listener  = (DrawingToolsClickListener) getActivity();
		return v;
	
	}
	
	@Override
	public void onStart() {
		super.onStart();

		drawing.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				listener.onDrawingOptionsDemand(drawing);
			}
			
		});
		
		objects.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				listener.onDrawingOptionsDemand(objects);
			}
			
		});
		
		features.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				listener.onDrawingOptionsDemand(features);
			}
			
		});
		
		pdfLoader.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onDrawingOptionsDemand(pdfLoader);
			}
		});
		
		
		
		
	}
}
