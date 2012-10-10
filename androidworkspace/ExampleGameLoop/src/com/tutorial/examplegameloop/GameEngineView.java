package com.tutorial.examplegameloop;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameEngineView extends SurfaceView implements SurfaceHolder.Callback
{
    //used to keep track of time between updates and amount of time to sleep for
    long lastUpdate = 0;
    long sleepTime=0;
    //Game engine
    GameEngine gEngine;

    //objects which house info about the screen
    SurfaceHolder surfaceHolder;
    Context context;
 
    //our Thread class which houses the game loop
    private PaintThread thread;
 
    //initialization code
    void InitView(){
      //initialize our screen holder
      SurfaceHolder holder = getHolder();
      holder.addCallback( this);

      //initialize our game engine
      gEngine = new GameEngine();
      gEngine.Init(context.getResources());
  
      //initialize our Thread class. A call will be made to start it later
      thread = new PaintThread(holder, context, new Handler(), gEngine);
      setFocusable(true);
   }
 
   //class constructors
   public GameEngineView(Context contextS, AttributeSet attrs, int defStyle){
       super(contextS, attrs, defStyle);
       context=contextS;
       InitView();
   }
   public GameEngineView(Context contextS, AttributeSet attrs){
       super(contextS, attrs);
       context=contextS;
       InitView();
   }
 

    //these methods are overridden from the SurfaceView super class. They are automatically called 
    //when a SurfaceView is created, resumed or suspended.
    @Override 
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

    @Override 
    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean retry = true;
        //code to end gameloop
        thread.state=PaintThread.PAUSED;
        while (retry) {
            try {
                //code to kill Thread
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    @Override 
    public void surfaceCreated(SurfaceHolder arg0) {
        if(thread.state==PaintThread.PAUSED){
            //When game is opened again in the Android OS
            thread = new PaintThread(getHolder(), context, new Handler(), gEngine);
            thread.start();
        }else{
            //creating the game Thread for the first time
            thread.start();
        }
    }
}