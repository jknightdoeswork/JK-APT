package com.jknight.particletoy.engine.particleengine;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ParticleFactory {
	private Particle[] particles;
	private LinkedList<Particle> free;
	
	public ParticleFactory(int max_particles) {
		particles = new Particle[max_particles];
		free = new LinkedList<Particle>();
		for (int i = 0; i < max_particles; i++) {
			particles[i] = new Particle();
			free.add(particles[i]);
		}
	}
	
	public Particle get_free_particle() {
		try {
			Particle first = free.removeFirst();
			first.Init();
			return first;
		} catch(NoSuchElementException e) {
			return null;
		}
	}
	
	public void return_particle(Particle particle) {
		free.add(particle);
	}
}
