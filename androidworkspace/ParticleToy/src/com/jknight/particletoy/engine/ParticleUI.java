package com.jknight.particletoy.engine;

import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleUI;

public class ParticleUI extends AngleUI {

	ParticleEngine _pEngine;
	public ParticleUI(AngleActivity activity, ParticleEngine pEngine) {
		super(activity);
		_pEngine = pEngine;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		_pEngine.addParticle(e.getX(), e.getY());
		return true;
	}

}
