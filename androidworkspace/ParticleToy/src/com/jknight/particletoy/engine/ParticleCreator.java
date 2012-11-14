package com.jknight.particletoy.engine;

import java.util.Random;

/**
 * Particle Creator
 * Controls the initialization of particle entities.
 * A particle can be born many times, as particles memory is never given up.
 * Controls random effects on the particle itself.
 */
public interface ParticleCreator {
	public void create(Particle particle);
	public static JitterCreator jitterCreator = new JitterCreator();
	
	public static class JitterCreator implements ParticleCreator {
		/* Fields effecting uniqueness of a particle */
		public static Random rand = new Random();
		public static float hueJitter = 0.0f;
		public static float blinkJitter = 0.0f;
		
		private static float epsilon;
		
		public void create(Particle particle) {
			if (hueJitter > epsilon || hueJitter < -1.0f * epsilon) {
				particle.hueRate = rand.nextFloat() * hueJitter;
			}
			if (blinkJitter > epsilon || blinkJitter < -1.0f * epsilon) {
				particle.blinkRate = rand.nextFloat() * blinkJitter;
			}
			particle.mAlpha = 1.0f;
			particle.mRed = 1.0f;
			particle.mGreen = 1.0f;
			particle.mBlue = 1.0f;
		}
	}
}
