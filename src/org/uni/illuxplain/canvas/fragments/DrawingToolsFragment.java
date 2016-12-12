package org.uni.illuxplain.canvas.fragments;

import org.uni.jain.illuxplain.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DrawingToolsFragment extends Fragment implements OnClickListener {

	public interface OnDrawingToolSelected{
		public void onMarkerSelection();
		public void onLinerSelection();
		public void onEraserSelection();
		public void onColorChanges(String colorName);
		public void onWidthChanges(int width);
	}
	
	
	private ImageButton markerButton;
	private ImageButton linerButton;
	private ImageButton eraserButton;
	private ImageButton redButton;
	private ImageButton greenButton;
	private ImageButton blueButton;
	private ImageButton blackButton;
	private SeekBar widthToogler;
	private int width = 0;
	
	private OnDrawingToolSelected selection;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.drawing_too_fragment, container, false);
		
		markerButton = (ImageButton) v.findViewById(R.id.marker);
		linerButton = (ImageButton) v.findViewById(R.id.liner);
		eraserButton = (ImageButton) v.findViewById(R.id.eraser);
		
		blackButton = (ImageButton) v.findViewById(R.id.black);
		blueButton = (ImageButton) v.findViewById(R.id.blue);
		greenButton = (ImageButton) v.findViewById(R.id.green);
		redButton = (ImageButton) v.findViewById(R.id.red);
		widthToogler = (SeekBar) v.findViewById(R.id.width_toggler);
		
		selection = (OnDrawingToolSelected) getActivity();
		
		markerButton.setOnClickListener(this);
		linerButton.setOnClickListener(this);
		eraserButton.setOnClickListener(this);
		greenButton.setOnClickListener(this);
		redButton.setOnClickListener(this);
		blackButton.setOnClickListener(this);
		blueButton.setOnClickListener(this);
		
		return v;

	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		
		widthToogler.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
//				width = seekBar.getMax();
				selection.onWidthChanges(progress);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.marker){
			selection.onMarkerSelection();
		}
		else if(v.getId() == R.id.liner){
			selection.onLinerSelection();
		}
		else if(v.getId() == R.id.eraser){
			selection.onEraserSelection();
		}
		
		else if(v.getId() == R.id.black){
			selection.onColorChanges("Black");
		}
		
		else if(v.getId() == R.id.blue){
			selection.onColorChanges("Blue");
		}
		else if(v.getId() == R.id.green){
			selection.onColorChanges("Green");
		}
		
		else if(v.getId() == R.id.red){
			selection.onColorChanges("Red");
		}
		
		
		
	}
}





