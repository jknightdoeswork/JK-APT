package com.jknight.particletoy.engine;

import com.android.angle.AngleObject;
import com.android.angle.AngleSurfaceView;
import com.jknight.particletoy.ParticleToyActivity;
import com.jknight.particletoy.R;

public class ParticleEngine extends AngleObject {
	
	AngleSurfaceView _surface;
	SpriteFactory<Particle> _pFactory;
	
	public ParticleEngine(ParticleToyActivity activity, int maxChildren) {
		super(maxChildren);
		_surface = activity.mGLSurfaceView;
		try {
			_pFactory = new SpriteFactory<Particle>(Particle.class, maxChildren, R.drawable.stary_aura, _surface);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addParticle(float x, float y) {
		Particle particle = _pFactory.getFreeObject();
		if (null != particle) {
			particle.init(x, y, ParticleComponent.hueBlink);
			addObject(particle);
		}
	}
	
	public void removeParticle(Particle particle) {
		particle.mDie = true; // Particle will hand itself back to the pFactory
	}

	public void clear() {
		removeAll();
		_pFactory.freeAll();
	}
}
