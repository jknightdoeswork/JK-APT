package com.jknight.particletoy.engine;

import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleUI;

public class ParticleUI extends AngleUI {

	public ParticleUI(AngleActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		
		return true;
	}

}
