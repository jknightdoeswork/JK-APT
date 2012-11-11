package com.jknight.particletoy;
import android.os.Bundle;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.jknight.particletoy.engine.SpriteFactory;
import com.jknight.particletoy.engine.Particle;
import com.jknight.particletoy.engine.ParticleEngine;

public class GameEngineActivity extends AngleActivity {
	
	ParticleEngine pEngine;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pEngine = new ParticleEngine(mGLSurfaceView);

        // Add Children
        mGLSurfaceView.addObject(pEngine);        
        setContentView(mGLSurfaceView);
    }
}