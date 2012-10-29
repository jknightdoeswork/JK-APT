package com.jknight.particletoy.engine.particleengine;

import java.util.LinkedList;

import android.content.res.Resources;
import android.graphics.Canvas;

public class ParticleEngine {

	public static int MAX_PARTICLES = 1000;
	public static ParticleFactory pFactory = new ParticleFactory(1000);
	
	public LinkedList<Particle> particles;

	/**
	 * Constructor
	 */
	public ParticleEngine() {
		particles = new LinkedList<Particle>();
	}
	
	
	/**
	 * Init
	 * Constructs the factory if it doesn't exist.
	 * @param resources
	 */
	public void Init(Resources resources) {
		return;
	}
	
	
	public void AddParticle(float x, float y) {
		Particle to_add = pFactory.get_free_particle();
		to_add.world_x = x;
		to_add.world_y = y;
		particles.add(to_add);
	}
	
	
	public void Update(long elapsed, float camera_x, float camera_y) {
		int size = particles.size();
		for (int i = 0; i < size; i++) {
			Particle current = particles.removeFirst();
			if (current.Update(elapsed, camera_x, camera_y)) {
				particles.addLast(current);
			} else {
				pFactory.return_particle(current);
			}
		}
	}
	

	public void Draw(Canvas c) {
		for (Particle current : particles) {
			current.Draw(c);
		}
	}
}
