package com.jknight.particletoy.engine;

import com.jknight.particletoy.engine.particleengine.ParticleEngine;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class GameEngine {

	ParticleEngine pEngine;
	float camera_x;
	float camera_y;
	
	
	/** 
	 * Constructor
	 */
	public GameEngine() {
		pEngine = new ParticleEngine();
	}

	/**
	 * Init
	 * Re-initializes the particle engine.
	 * @param resources reference to the static resources
	 */
	public void Init(Resources resources) {
		pEngine.Init(resources);
		camera_x = 0.0f;
		camera_y = 0.0f;
	}
	
	public void Update(long elapsed) {
		// READ INPUT
		// APPLY FORCE
		// UPDATE PHYSICS
		pEngine.Update(elapsed, camera_x, camera_y);
	}
	
	public void Draw(Canvas c) {
		pEngine.Draw(c);
	}

	/**
    * Handles the touch input.
    * @param event
    */
   public boolean onTouchEvent(MotionEvent event) {
	   	float x = event.getX();
	   	float y = event.getY();
	   	pEngine.AddParticle(x, y);
		return true;
   }
	
	
}
