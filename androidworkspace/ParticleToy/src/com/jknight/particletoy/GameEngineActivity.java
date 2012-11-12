package com.jknight.particletoy;
import android.os.Bundle;

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

        // Add Children
        mGLSurfaceView.addObject(pEngine);
        setContentView(mGLSurfaceView);
    }
}