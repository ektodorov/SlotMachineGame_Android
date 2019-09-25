package com.blogspot.techzealous.slotmachine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.blogspot.techzealous.slotmachine.utils.SMConstants;

public class EulaActivity extends Activity {

	private Button buttonCancel;
	private Button buttonOk;
	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.eula);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		buttonCancel = (Button) findViewById(R.id.buttonEulaCancel);
		buttonOk = (Button) findViewById(R.id.buttonEulaOk);
		
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prefs.edit().putBoolean(SMConstants.PREF_EULA, true).commit();
				Intent i = new Intent(EulaActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
}
