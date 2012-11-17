package com.jknight.particletoy;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.angle.AngleActivity;
import com.jknight.particletoy.engine.ParticleEngine;
import com.jknight.particletoy.engine.ParticleUI;

public class GameEngineActivity extends AngleActivity {
	
	ParticleEngine pEngine;
	ParticleUI pUI;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pEngine = new ParticleEngine(this, 1000);
        pUI = new ParticleUI(this, pEngine);
        setUI(pUI);

        // Add Children To Surface
        mGLSurfaceView.addObject(pEngine);
        
        setContentView(R.layout.game_engine_layout);
        
        // Add Surface To View 
        addContentView(mGLSurfaceView, null);
        
        // Add Options Menu To View
        // Create an instance of ExampleFragment
        OptionsFragment options_fragment = new OptionsFragment();
        getFragmentManager().beginTransaction()
                .add(R.layout.game_engine_layout, options_fragment).commit();
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
    
    /* Handles blink toggle */
    public void toggleBlink(View view){
    	return;
    }
}