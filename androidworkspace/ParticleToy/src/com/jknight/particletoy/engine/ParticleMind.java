package com.jknight.particletoy.engine;

import android.util.FloatMath;

/*
 * An interface to a particles mind.
 * Controls the update step.
 * Static setup for only 1 new call per mind.
 * All like minded particles share memory space.
 */
public interface ParticleMind {
	public static float _globalHueRate = 1.0f;
	public static float _globalBlinkRate = 1.0f;
	public void think(Particle particle, float secondsElapsed);
	public static Sheep sheep = new Sheep();
	public static Blink blink = new Blink();
	public static Hue hue = new Hue();
	public static ComplexMind hueBlink = new ComplexMind(new ParticleMind[] {hue, blink});
	
	/*
	 * A complex mind is made up of multiple subminds.
	 */
	public static class ComplexMind implements ParticleMind{
		ParticleMind[] _thoughts;		
		public ComplexMind(ParticleMind[] thoughts){
			_thoughts = thoughts;
		}
		@Override
		public void think(Particle particle, float secondsElapsed) {
			for (ParticleMind thought : _thoughts) {
				thought.think(particle, secondsElapsed);
			}
		}
	}
	
	/*
	 * Sheep
	 * The default mind for all particles.
	 */
	public static class Sheep implements ParticleMind {
		@Override
		public void think(Particle particle, float secondsElapsed) {
			return;
		}
	}
	
	/*
	 * Blink
	 * Alpha fades in and out with age.
	 */
	public static class Blink implements ParticleMind {
		@Override
		public void think(Particle particle, float secondsElapsed) {
			particle.mAlpha = (float) Math.abs(FloatMath.cos(particle._age * particle.blinkRate * _globalBlinkRate));
		}
	}
	
	/*
	 * Hue
	 * Hue blink with age.
	 */
	public static class Hue implements ParticleMind {
		@Override
		public void think(Particle particle, float secondsElapsed) {
			float current = (float) FloatMath.cos(particle._age * particle.hueRate * _globalHueRate);
			float max = 1.0f;
			float min = -1.0f;
			float range = max - min;
	        float regionSize = range / 6;
	        if (current < min + regionSize)
	        {
	            particle.mRed = 1.0f;
	            particle.mBlue = 0;
	            float toMultiply = (current - min) / regionSize;
	            particle.mGreen = (int)(toMultiply * 1.0f);
	        }
	        else if (current < min + 2 * regionSize)
	        {
	            int tosubtract = (int)(((current - (min + regionSize)) / regionSize) * 1.0f);
	            particle.mRed = 1.0f - tosubtract;
	            particle.mBlue = 0;
	            particle.mGreen = 1.0f;
	        }
	        else if (current < min + 3 * regionSize)
	        {
	            particle.mRed = 0;
	            float toMultiply = ((current - (min + (2 * regionSize))) / regionSize);
	            particle.mBlue = (int)(toMultiply * 1.0f);
	            particle.mGreen = 1.0f;
	        }
	        else if (current < min + 4 * regionSize)
	        {
	            particle.mRed = 0;
	            particle.mBlue = 1.0f;
	            int toSubtract = (int)(((current - (min + (3 * regionSize))) / regionSize) * 1.0f);
	            particle.mGreen = 1.0f - toSubtract;
	        }
	        else if (current < min + 5 * regionSize)
	        {
	            float toMultiply = ((current - (min + (4 * regionSize))) / regionSize);
	            particle.mRed = (int)(toMultiply * 1.0f);
	            particle.mBlue = 1.0f;
	            particle.mGreen = 0;
	        }
	        else
	        {
	            particle.mRed = 1.0f;
	            int toSubtract = (int)(((current - (min + (5 * regionSize))) / regionSize) * 1.0f);
	            particle.mBlue = 1.0f - toSubtract;
	            particle.mGreen = 0;
	        }	        
		}
	}    
}

