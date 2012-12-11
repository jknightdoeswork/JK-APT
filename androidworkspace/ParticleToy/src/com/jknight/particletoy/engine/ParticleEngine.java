package com.jknight.particletoy.engine;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AngleObject;
import com.jknight.particletoy.R;

public class ParticleEngine extends AngleObject {
	
	SpriteFactory<Particle> _pFactory;
	
	public ParticleEngine(int maxChildren) {
		super(maxChildren);
		try {
			_pFactory = new SpriteFactory<Particle>(Particle.class, maxChildren, R.drawable.stary_aura);
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
			particle.init(x, y, ParticleComponent.sheep);
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

	public void draw(float[] mMVPMatrix) {
		for (int t=0;t<mChildsCount;t++) {
			Particle child = (Particle)mChilds[t];
			child.draw(mMVPMatrix);
		}
			
	}
}
