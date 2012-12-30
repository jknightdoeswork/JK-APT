package com.jknight.particletoy;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jknight.particletoy.engine.ParticleEngine;
import com.jknight.particletoy.engine.ParticleUI;

public class ParticleToyActivity extends Activity {
	
	public GLSurfaceView mGLSurfaceView; // The main GL View
	MyGLRenderer renderer;
	public View optionsView; // The options dropdown
	private final String LOG_TAG = "ParticleToyActivity";
	private TextView frameRateView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        // hide options
        optionsView = findViewById(R.id.frag_container);
        optionsView.setVisibility(View.INVISIBLE);
        

    	mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);

		// detect if OpenGL ES 2.0 support exists - if it doesn't, exit.
		if (detectOpenGLES20()) {
			// Tell the surface view we want to create an OpenGL ES 2.0-compatible
			// context, and set an OpenGL ES 2.0-compatible renderer.
			mGLSurfaceView.setEGLContextClientVersion(2);
			renderer = new MyGLRenderer();
			mGLSurfaceView.setRenderer(renderer);
		} 
		else { // quit if no support - get a better phone! :P
			this.finish();
		}

    }
    
	/**
	 * Detects if OpenGL ES 2.0 exists
	 * @return true if it does
	 */
	private boolean detectOpenGLES20() {
		ActivityManager am =
			(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		Log.d("OpenGL Ver:", info.getGlEsVersion());
		return (info.reqGlEsVersion >= 0x20000);
	}
	
    /* Inflates the options menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.actionbar, menu);
		return true;
    	
    }
    
    /* Handles option clicks */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.clear:
            	renderer.mPEngine.clear();
            	return true;
            case R.id.options:
            	if (optionsView.getVisibility() == View.VISIBLE) {
            		optionsView.setVisibility(View.GONE);
            	}
            	else {
            		optionsView.setVisibility(View.VISIBLE);
            	}
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
        if (optionsView.getVisibility() == View.VISIBLE) {
            optionsView.setVisibility(View.GONE);
            return;
        }
        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (renderer.mPEngine.mPUI != null)
			if (renderer.mPEngine.mPUI.onTouchEvent(event))
				return true;
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		if (renderer.mPEngine.mPUI != null)
			if (renderer.mPEngine.mPUI.onTrackballEvent(event))
				return true;
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (renderer.mPEngine.mPUI != null)
			if (renderer.mPEngine.mPUI.onKeyDown(keyCode, event))
				return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mGLSurfaceView.onPause();
		if (renderer.mPEngine.mPUI != null)
			renderer.mPEngine.mPUI.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mGLSurfaceView.onResume();
		try {
			renderer.mPEngine.mPUI.onResume();
		}
		catch (NullPointerException e) {
			
		}
		
	}
    
    /* Handles blink toggle */
    public void toggleBlink(View view){
    	return;
    }
}