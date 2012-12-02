package com.jknight.particletoy;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import com.android.angle.AngleSurfaceView;
import com.jknight.particletoy.engine.ParticleEngine;
import com.jknight.particletoy.engine.ParticleUI;

public class ParticleToyActivity extends Activity {
	
	public AngleSurfaceView mGLSurfaceView; // The main GL View
	public View optionsView; // The options dropdown
	ParticleEngine pEngine;
	ParticleUI pUI;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        // hide options
        optionsView = findViewById(R.id.frag_container);
        optionsView.setVisibility(View.INVISIBLE);
        
        // create surface
        try
		{
        	mGLSurfaceView = (AngleSurfaceView) findViewById(R.id.anglesurfaceview);
			Thread.sleep(100);
			mGLSurfaceView.setAwake(true);
			mGLSurfaceView.start();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
        
        // create particle engine
        pEngine = new ParticleEngine(this, 1000);
        mGLSurfaceView.addObject(pEngine);
        
        // create ui controller
        pUI = new ParticleUI(pEngine);
        mGLSurfaceView.addObject(pUI);
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
            	pEngine.clear();
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
		if (pUI != null)
			if (pUI.onTouchEvent(event))
				return true;
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		if (pUI != null)
			if (pUI.onTrackballEvent(event))
				return true;
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (pUI != null)
			if (pUI.onKeyDown(keyCode, event))
				return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mGLSurfaceView.onPause();
		if (pUI != null)
			pUI.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mGLSurfaceView.onResume();
		if (pUI != null)
			pUI.onResume();
	}

	@Override
	public void finish()
	{
		mGLSurfaceView.delete();
		super.finish();
	}
    
    /* Handles blink toggle */
    public void toggleBlink(View view){
    	return;
    }
}