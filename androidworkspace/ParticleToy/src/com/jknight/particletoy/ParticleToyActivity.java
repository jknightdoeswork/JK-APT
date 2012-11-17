package com.jknight.particletoy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParticleToyActivity extends Activity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startGame(View view) {
    	Intent intent = new Intent(ParticleToyActivity.this, GameEngineActivity.class);
    	startActivity(intent);
    }

    public void openOptions(View view) {
    	return;
    }
}