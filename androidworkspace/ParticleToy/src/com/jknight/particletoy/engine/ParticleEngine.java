package com.jknight.particletoy.engine;

import com.android.angle.AngleObject;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleSurfaceView;
import com.jknight.particletoy.R;

public class ParticleEngine extends AngleObject {
	
	/**
	 * Adds a particle using a collective particle resource pool.
	 */
	public void addParticle() {
		addParticle(0,0);
	}
	public void addParticle(int x, int y) {
		AngleSurfaceView angleSurfaceView = getSurfaceView();
		AngleSpriteLayout particleSpriteLayout = new AngleSpriteLayout(angleSurfaceView, R.drawable.stary_aura);
		Particle particle = new Particle(particleSpriteLayout);
		addObject(particle);
	}
}
