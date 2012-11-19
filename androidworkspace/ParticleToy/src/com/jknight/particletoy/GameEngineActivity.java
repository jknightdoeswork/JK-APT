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

public class GameEngineActivity extends Activity {
	
	public AngleSurfaceView mGLSurfaceView; // The main GL View
	ParticleEngine pEngine;
	ParticleUI pUI;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.game_engine_layout);
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
        pEngine = new ParticleEngine(this, 1000);
        pUI = new ParticleUI(pEngine);
        mGLSurfaceView.addObject(pEngine);
        mGLSurfaceView.addObject(pUI);
        

        
//        // Add Options Menu To View
//        // Create an instance of ExampleFragment
//        OptionsFragment options_fragment = new OptionsFragment();
//        getFragmentManager().beginTransaction()
//                .add(R.layout.game_engine_layout, options_fragment).commit();
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
            default:
                return super.onOptionsItemSelected(item);
        }
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