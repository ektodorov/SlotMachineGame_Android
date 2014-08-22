package com.blogspot.techzealous.slotmachine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.blogspot.techzealous.slotmachine.utils.SMConstants;

public class LoadActivity extends Activity {

	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(prefs.getBoolean(SMConstants.PREF_EULA, false)) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		} else {
			Intent y = new Intent(this, EulaActivity.class);
			startActivity(y);
		}
		
		finish();
	}
}
