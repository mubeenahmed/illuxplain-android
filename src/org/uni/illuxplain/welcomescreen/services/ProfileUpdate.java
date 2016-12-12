package org.uni.illuxplain.welcomescreen.services;

import org.uni.illuxplain.GlobalApplication;
import org.uni.illuxplain.profile.ChangeInformation;
import org.uni.illuxplain.welcomescreen.WelcomeScreen;
import org.uni.jain.illuxplain.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileUpdate extends AppCompatActivity {

	private EditText gender;
	private EditText education;
	private EditText work;
	private EditText professional;

	public static String sGender;
	public static String sProfessional;
	public static String sEducation;
	public static String sWork;
	
	private String g;
	private String p;
	private String e;
	private String w;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_update);

		gender = (EditText) findViewById(R.id.edit_gender);
		work = (EditText) findViewById(R.id.edit_work);
		professional = (EditText) findViewById(R.id.edit_professional);
		education = (EditText) findViewById(R.id.edit_education);
		
		g = sGender = ProfileInfo.gender;
		p = sProfessional = ProfileInfo.professional;
		e = sEducation = ProfileInfo.education;
		w = sWork = ProfileInfo.work;
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		
		gender.setText(sGender);
		work.setText(sWork);
		professional.setText(sProfessional);
		education.setText(sEducation);
		
	}
	
	public void saveChanges(View v){
		sGender = gender.getText().toString();
		sProfessional = professional.getText().toString();
		sWork = work.getText().toString();
		sEducation = education.getText().toString();
		
		if(g.equals(sGender) || p.equals(sProfessional) || w.equals(sWork) || e.equals(sEducation)){
			//No Need to go to network..
			Toast.makeText(this, "No Changes Made..", Toast.LENGTH_LONG).show();
		}
		else {
		ChangeInformation updater = new ChangeInformation(this);
		updater.changeInformation(sGender,sProfessional,sWork,sEducation,GlobalApplication.username);
		}
	}
	
	
	public void discardChanges(View v){
		finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.profile_update, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
