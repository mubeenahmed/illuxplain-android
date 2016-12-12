package org.uni.illuxplain.canvas.fragments;


import org.uni.illuxplain.opentok.MultipartyActivity;
import org.uni.jain.illuxplain.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FeatureToolsFragment extends Fragment implements OnClickListener {

	public interface OnFeatureClickListener {
		public void onCameraSelection();

		public void onStartNewSelection();

		public void onSaveSelection();
	}

	private ImageButton camera;
	private ImageButton startNew;
	private ImageButton save;
//	private ImageButton chatApplication;

	private OnFeatureClickListener selection;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.feature_tools_fragment, container,
				false);

		selection = (OnFeatureClickListener) getActivity();

		camera = (ImageButton) v.findViewById(R.id.take_camera);
		startNew = (ImageButton) v.findViewById(R.id.start_new);
		save = (ImageButton) v.findViewById(R.id.save_canvas);
//		chatApplication = (ImageButton) v.findViewById(R.id.connected_users);

		camera.setOnClickListener(this);
		startNew.setOnClickListener(this);
//		chatApplication.setOnClickListener(this);
		save.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.take_camera) {
			selection.onCameraSelection();
		} 
		else if (v.getId() == R.id.start_new) {
			selection.onStartNewSelection();
		}
		else if (v.getId() == R.id.save_canvas) {
			selection.onSaveSelection();
		}
		else if(v.getId() == R.id.connected_users){
			Intent intent = new Intent(getActivity(),MultipartyActivity.class);
			getActivity().startActivity(intent);
			
		}
		

	}

}
