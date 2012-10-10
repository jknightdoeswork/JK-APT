package com.jknight.particletoy.engine.particleengine;

import java.util.Dictionary;
import java.util.Enumeration;
import java.lang.reflect.*;

import com.jknight.particletoy.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

/**
 * Particle
 * 
 * Most basic Particle class.
 * Has velocity, acceleration, and age components.
 * Lives until the end of it's life and then dies.
 */
public class Particle {
	
	public float world_x; 				// In World Coordinates, bottom left is (0,0)
	public float world_y;
	
	public float screen_x;				// In Camera Coordinates, bottom left is (0,0)
	public float screen_y;
	
	public int width;					// Dimensions
	public int height;
	
	public float x;						// Position
	public float y;

	public float dx;					// Velocity
	public float dy;
	
	public float ax;					// Acceleration
	public float ay;
	
	public float age;					//TODO In microseconds?
	public float max_age;
	
	BitmapDrawable drawable;
	
	public Particle() {
		Init();
	}
	
	public void Init() {
		this.world_x = 0.0f;
		this.world_y = 0.0f;
		this.dx = 0.0f;
		this.dy = 0.0f;
		this.ax = 0.0f;
		this.ay = 0.0f;
		this.width = drawable.getIntrinsicWidth();
		this.height = drawable.getIntrinsicHeight();
	}
	
	public void Init(Dictionary<String, Object> kwargs, Context context) {
		// Default values
		this.world_x = 0.0f;
		this.world_y = 0.0f;
		this.dx = 0.0f;
		this.dy = 0.0f;
		this.ax = 0.0f;
		this.ay = 0.0f;
		this.width = drawable.getIntrinsicWidth();
		this.height = drawable.getIntrinsicHeight();
		Resources res = context.getResources();
		this.drawable = (BitmapDrawable) res.getDrawable(R.drawable.stary_aura);
		
		// I miss python style keyword-arguments
		Enumeration<String> keys = kwargs.keys();
		Class<? extends Particle> particle_class = getClass();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			try {
				Field field = particle_class.getField(key);
				field.set(this, kwargs.get(key));
			} catch (NoSuchFieldException e) {
				// TODO Auto Generated
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				// TODO Auto Generated
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Update
	 * @param elapsed The time in microseconds since the last update.
	 * @return true if this particle still has life left to live, false otherwise.
	 */
	public boolean Update(long elapsed, float camera_x, float camera_y) {
		// TODO FILL THIS IN
		screen_x = world_x - camera_x;
		screen_y = world_y - camera_y;
		return true;
	}
	
	/**
	 * Draw
	 * Draws this particle at the calculated screen coordinates
	 * @param c the android canvas to draw on
	 * @param camera_x the x coordinate of the camera so that draw_x = world_x - camera_x
	 * @param camera_y the x coordinate of the camera so that draw_y = world_y - camera_y
	 */
	public void Draw(Canvas c) {
		drawable.draw(c);
		return;
	}
}
