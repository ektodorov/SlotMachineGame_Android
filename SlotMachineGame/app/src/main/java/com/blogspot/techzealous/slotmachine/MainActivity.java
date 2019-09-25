package com.blogspot.techzealous.slotmachine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.techzealous.slotmachine.utils.MyView;
import com.blogspot.techzealous.slotmachine.utils.SMAnimator;
import com.blogspot.techzealous.slotmachine.utils.SMCalculator;
import com.blogspot.techzealous.slotmachine.utils.SMConstants;

public class MainActivity extends Activity {

	private TextView textViewPlusSign;
	private TextView textViewWinAmmount;
	private TextView textViewDamage;
	private Button buttonSpin;
	private Button buttonStop;
	private LinearLayout linearLayoutDamage;
	private LinearLayout linearLayoutToShock;
	private MyView myView;
	private SharedPreferences prefs;
	
	private boolean gotDimensions;
	private boolean isSpinning;
	private boolean hasStopped;
	private boolean isHit;
	private int damage;
	
	private Animation animHitLeft;
	private SMAnimator animator;
	private SMCalculator calculator;
	
	private SensorManager sm;
	private SensorEventListener se;
	private float xAxis;
	private boolean first = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        textViewPlusSign = (TextView) findViewById(R.id.textViewPlusSign);
        textViewWinAmmount = (TextView) findViewById(R.id.textViewCurrentWining);
        textViewDamage = (TextView) findViewById(R.id.textViewDamage);
        buttonSpin = (Button) findViewById(R.id.buttonSpin);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        linearLayoutDamage = (LinearLayout) findViewById(R.id.linearLayoutForDamage);
        linearLayoutToShock = (LinearLayout) findViewById(R.id.linearLayoutToShock);
        myView = (MyView) findViewById(R.id.myViewClouds);
        
        animHitLeft = AnimationUtils.loadAnimation(this, R.anim.centertoleft);
        
        calculator = new SMCalculator(this, this);
		animator = new SMAnimator(this, this, calculator);
		
		calculator.setScore(prefs.getInt(SMConstants.PREF_SCORE, 0));
        
        buttonSpin.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		//get the size of MyView
        		if(!gotDimensions) {
        			gotDimensions = true;
        			animator.setDimensions(myView.getWidth(), myView.getHeight());
        		}
        		
        		if(isSpinning) {
        			// do nothing it is already spinning
        		} else {
        			isHit = false;
        			hasStopped = false;
        			isSpinning = true;
        			
        			calculator.substractFromSpin();
        			
        			textViewPlusSign.setVisibility(View.GONE);
        			textViewWinAmmount.setVisibility(View.GONE);
        			
        			/* Start a Thread to animate the TextViews with the help of the Handler */
        			Thread threadFirst = new Thread(new  Runnable() {
        				public void run() {
        					while(isSpinning) {
        						animator.animate();
        					}
        				}
        			});
        			threadFirst.start();
        		}
        	}
        });
        
        buttonStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isSpinning = false;
				if(!hasStopped) {
					hasStopped = true;
					
					calculator.setPositions(animator.getFirst(), animator.getSecond(), animator.getThird());
					calculator.calculateScore();
					
				}
			}
		});
        
        //what to do when the machine is punched/hit by the player
        linearLayoutDamage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP && hasStopped && !isHit) {

					linearLayoutToShock.startAnimation(animHitLeft);
	
					damage++;
					isHit = true;
					textViewDamage.setText(String.valueOf(damage) + "%");
												
					if(textViewWinAmmount.getVisibility() == View.GONE) {
						Thread myThread = new Thread(new Runnable() {
							public void run() {
								animator.animateHit();
							}
						});
						myThread.start();
					}
					Toast.makeText(MainActivity.this, "You damaged the machine, ouch", Toast.LENGTH_SHORT).show();
					return true;
				}
				return true;
			}
		});
        
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        se = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					float newXAxis = event.values[0];
					if(newXAxis > xAxis + 7 || newXAxis < xAxis - 7 && first) {
						Log.i("MainActivity", "xAxis = " + xAxis + ", newXAxis = " + newXAxis);
					}
					first = !first;
					xAxis = newXAxis;
				}
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				
			}
		};
		Sensor acel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	isSpinning = false;
    	hasStopped = true;
    	calculator.calculateScore();
    	
    	prefs.edit().putInt(SMConstants.PREF_SCORE, calculator.getScore()).commit();
    	prefs.edit().putInt(SMConstants.PREF_DAMAGE, damage).commit();
    	
    	sm.unregisterListener(se);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	calculator.setScore(prefs.getInt(SMConstants.PREF_SCORE, 0));
    	damage = prefs.getInt(SMConstants.PREF_DAMAGE, 0);
    	
    	sm.registerListener(se, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_clear:
    		calculator.setScore(0);
    		damage = 0;
    		
    		prefs.edit().remove(SMConstants.PREF_SCORE).commit();
    		prefs.edit().remove(SMConstants.PREF_DAMAGE).commit();
    		
    		textViewDamage.setText(String.valueOf(damage) + "%");
    		return true;
    		
    	case R.id.menu_more:
    		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(SMConstants.MY_STORE_SITE));
    		startActivity(i);
    		return true;
    	}
    	return false;
    }
}
