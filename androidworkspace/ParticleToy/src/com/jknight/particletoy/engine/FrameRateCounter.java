package com.jknight.particletoy.engine;

import android.util.Log;

/**
 * Class to record, log and display frame rate averaged over a variable number of frames.
 * @author jknight
 *
 */
public class FrameRateCounter {

	private float[] elapsed; // Array of elapsed times between frames
	private int n; // Number of times to average frame rate over
	private float average; // Current average frame rate
	private int cursor; // Index of value to overwrite in elapsed
	private float total; // Sum of elapsed
	
	private int i; // Simple loop counter. To prevent allocation at runtime
	
	public Boolean loggingEnabled; // If true, logs every frame
	private final String LOG_TAG = "FrameRateCounter";
	
	public FrameRateCounter(int numFramesToTrack) {
		n = numFramesToTrack;
		elapsed = new float[n];
		for (i = 0; i < n; i++)
			elapsed[i] = 0.0f;
		average = 0;
		cursor = 0;
	}
	
	/**
	 * Record a frame's time.
	 */
	public void step(float secondsElapsed) {
		elapsed[cursor] = secondsElapsed;
		cursor = (cursor + 1) % n;
		total = 0.0f;
		for (i = 0; i < n; i++) {
			total += elapsed[i];
		}
		if (total != 0)
			average = n / total;
		if (loggingEnabled) {
			Log.i(LOG_TAG, "" + average);
		}
	}

	/**
	 * Returns the current average frame rate (frames per second)
	 * @return
	 */
	public float getFrameRate() {
		return average;
	}
}
