package com.jknight.particletoy.engine;

import android.util.FloatMath;
import android.util.Log;

/*
 * An interface to a particles mind.
 * Controls the update step.
 * Static setup for only 1 new call per mind.
 * All like minded particles share memory space.
 */
public interface ParticleComponent {
	public void step(Particle particle, float secondsElapsed);
	public void init(Particle particle);
	public static SimpleBrush sheep = new SimpleBrush();
	public static Blink blink = new Blink();
	public static Hue hue = new Hue();
	public static ComplexMind hueBlink = new ComplexMind(new ParticleComponent[] {hue, blink});
	public static float _globalHueRate = 3.0f;
	public static float _globalBlinkRate = 1.6180f * _globalHueRate;
	
	/*
	 * A complex mind is made up of multiple subminds.
	 */
	public static class ComplexMind implements ParticleComponent{
		ParticleComponent[] _thoughts;		
		public ComplexMind(ParticleComponent[] thoughts){
			_thoughts = thoughts;
		}
		@Override
		public void step(Particle particle, float secondsElapsed) {
			for (ParticleComponent thought : _thoughts) {
				thought.step(particle, secondsElapsed);
			}
		}
		@Override
		public void init(Particle particle) {
			for (ParticleComponent thought : _thoughts) {
				thought.init(particle);
			}
		}
	}
	
	/*
	 * Sheep
	 * The default mind for all particles.
	 */
	public static class SimpleBrush implements ParticleComponent {
		@Override
		public void step(Particle particle, float secondsElapsed) {
			return;
		}

		@Override
		public void init(Particle particle) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/*
	 * Blink
	 * Alpha fades in and out with age.
	 */
	public static class Blink implements ParticleComponent {
		@Override
		public void step(Particle particle, float secondsElapsed) {
			particle.mAlpha = (float) Math.abs(FloatMath.cos(particle._age * particle.blinkRate * _globalBlinkRate));
		}

		@Override
		public void init(Particle particle) {
			particle.mAlpha = 1.0f;
			
		}
	}
	
	/*
	 * Hue
	 * Hue blink with age.
	 */
	public static class Hue implements ParticleComponent {
		@Override
		public void step(Particle particle, float secondsElapsed) {
			float current = (float) FloatMath.cos(particle._age * particle.hueRate * _globalHueRate);
			float max = 1.0f;
			float min = -1.0f;
			float range = max - min;
	        float regionSize = range / 6.0f;
	        if (current < min + regionSize) {
	            particle.mRed = 1.0f;
	            particle.mBlue = 0;
	            float toMultiply = (current - min) / regionSize;
	            particle.mGreen = (toMultiply * 1.0f);
	        }
	        else if (current < min + 2 * regionSize) {
	            float tosubtract = (((current - (min + regionSize)) / regionSize) * 1.0f);
	            particle.mRed = 1.0f - tosubtract;
	            particle.mBlue = 0;
	            particle.mGreen = 1.0f;
	        }
	        else if (current < min + 3 * regionSize) {
	            particle.mRed = 0;
	            float toMultiply = ((current - (min + (2 * regionSize))) / regionSize);
	            particle.mBlue = (toMultiply * 1.0f);
	            particle.mGreen = 1.0f;
	        }
	        else if (current < min + 4 * regionSize) {
	            particle.mRed = 0;
	            particle.mBlue = 1.0f;
	            float toSubtract = (((current - (min + (3 * regionSize))) / regionSize) * 1.0f);
	            particle.mGreen = 1.0f - toSubtract;
	        }
	        else if (current < min + 5 * regionSize) {
	            float toMultiply = ((current - (min + (4 * regionSize))) / regionSize);
	            particle.mRed = (toMultiply * 1.0f);
	            particle.mBlue = 1.0f;
	            particle.mGreen = 0;
	        }
	        else {
	            particle.mRed = 1.0f;
	            float toSubtract = (((current - (min + (5 * regionSize))) / regionSize) * 1.0f);
	            particle.mBlue = 1.0f - toSubtract;
	            particle.mGreen = 0;
	        }
	        //Log.i("COLOR", "R: " + particle.mRed + " G: " + particle.mBlue + " B: " + particle.mGreen);
		}

		@Override
		public void init(Particle particle) {
			particle.mBlue = 1.0f;
			particle.mRed = 1.0f;
			particle.mGreen = 1.0f;			
		}
	}    
}

