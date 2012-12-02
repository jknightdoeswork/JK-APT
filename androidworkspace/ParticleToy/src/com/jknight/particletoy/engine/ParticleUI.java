package com.jknight.particletoy.engine;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;

public class ParticleUI extends AngleObject {

	ParticleEngine _pEngine;
	public ParticleUI(ParticleEngine pEngine) {
		super();
		_pEngine = pEngine;
	}

	public boolean onTouchEvent(MotionEvent e) {
		_pEngine.addParticle(e.getX(), e.getY());
		return true;
	}

	public boolean onTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	public void onResume() {
		// TODO Auto-generated method stub
		
	}

}
