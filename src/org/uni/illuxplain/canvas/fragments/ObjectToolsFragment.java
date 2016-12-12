package org.uni.illuxplain.canvas.fragments;

import org.uni.jain.illuxplain.R;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ObjectToolsFragment extends Fragment implements OnClickListener{
 
	public interface OnObjectToolsListener{
		public void displayOjbect(String tool);
	}
	
	private ImageButton building;
	private ImageButton houseBuilding;
	private ImageButton humanMen;
	private ImageButton humanWomen;
	private ImageButton road;
	private ImageButton city;
	private ImageButton deleter;
	private ImageButton car;
	
	private OnObjectToolsListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.object_tools_fragment, container, false);
		
		building = (ImageButton) v.findViewById(R.id.office_building);
		houseBuilding = (ImageButton) v.findViewById(R.id.house_building);
		humanMen = (ImageButton) v.findViewById(R.id.human_men);
		humanWomen = (ImageButton) v.findViewById(R.id.human_women);
		city  = (ImageButton) v.findViewById(R.id.city_builds);
		road = (ImageButton) v.findViewById(R.id.road);
		deleter = (ImageButton) v.findViewById(R.id.delete);
//		car = v.findViewById(R.id.car);
		
		building.setOnClickListener(this);
		houseBuilding.setOnClickListener(this);
		humanMen.setOnClickListener(this);
		humanWomen.setOnClickListener(this);
		road.setOnClickListener(this);
		city.setOnClickListener(this);
		deleter.setOnClickListener(this);
//		car.setOnClickListener(this);
		
		listener = (OnObjectToolsListener) getActivity();
		
		return v;
	}

	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.office_building) {
			listener.displayOjbect("office_building");
		} 
		else if(v.getId() == R.id.house_building){
			listener.displayOjbect("house_building");
		}
		else if(v.getId() == R.id.human_men){
			
			listener.displayOjbect("human_men");
		}
		else if(v.getId() == R.id.human_women){
			
			listener.displayOjbect("human_women");
		}
		
		else if(v.getId() == R.id.city_builds){
			
			listener.displayOjbect("city_building");
		}
		else if (v.getId() == R.id.road) {

			listener.displayOjbect("road");
		}
		else if (v.getId() == R.id.delete) {
			
			listener.displayOjbect("deleter");
		}
//		else if (v.getId() == R.id.car) {
//			
//			listener.displayOjbect("car");
//		}

	}
	
}
