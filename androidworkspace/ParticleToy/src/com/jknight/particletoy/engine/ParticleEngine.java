package com.jknight.particletoy.engine;

import com.android.angle.AngleObject;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleSurfaceView;
import com.jknight.particletoy.R;

public class ParticleEngine extends AngleObject {
	
	AngleSurfaceView _surface;
	SpriteFactory<Particle> _pFactory;
	
	public ParticleEngine(AngleSurfaceView surface) {
		_surface = surface;
		try {
			_pFactory = new SpriteFactory<Particle>(Particle.class, 10000, surface);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Adds a particle using a collective particle resource pool.
	 */
	public void addParticle() {
		addParticle(0,0);
	}
	public void addParticle(int x, int y) {
		Particle particle = _pFactory.getFreeObject();
		particle.mPosition.mX = x;
		particle.mPosition.mY = y;
		addObject(particle);
	}
	
	public void removeParticle(Particle particle) {
		
	}
}
