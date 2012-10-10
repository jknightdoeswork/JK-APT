package com.jknight.particletoy;
import com.jknight.particletoy.view.GameEngineView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class GameEngineActivity extends Activity {

	GameEngineView gameEngineView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particle_layout);
    }

    public void startGame(View view) {
    	gameEngineView = (GameEngineView) findViewById(R.id.gameengineview);
    }

    public void openOptions(View view) {
    	return;
    }
}