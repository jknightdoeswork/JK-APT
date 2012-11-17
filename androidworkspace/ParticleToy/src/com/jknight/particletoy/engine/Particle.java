package com.jknight.particletoy.engine;

import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;

public class Particle extends AngleSprite {

	public float _age;
	public ParticleMind _mind; /* Controls how this particle updates */
	public ParticleCreator _birth; /* Controls how this particle is initialized. */
	public SpriteFactory<Particle> _pFactory;
	public static float _globalTimeScale = 1.0f;
	public float hueRate = 1.0f;
	public float blinkRate = 1.0f;
	
	public Particle(AngleSpriteLayout layout, SpriteFactory<Particle> pFactory) {
		super(layout);
		_pFactory = pFactory;
		_age=0.0f;
		_mind = ParticleMind.sheep;
	}
	
	@Override
	protected void onDie() {
		_pFactory.returnFreeObject(this);
	}
	
	@Override
	public void step(float secondsElapsed) {
		_age += secondsElapsed * _globalTimeScale;
		_mind.think(this, secondsElapsed);
	}

	public void init(float x, float y, ParticleCreator creator, ParticleMind mind) {
		_mind = mind;
		mPosition.mX = x;
		mPosition.mY = y;
		_age = 0.0f;
		// Creator will apply random variables on this particle
		creator.create(this);
	}
}
