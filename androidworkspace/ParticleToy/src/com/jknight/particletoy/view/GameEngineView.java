package com.jknight.particletoy.view;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.jknight.particletoy.engine.GameEngine;

public class GameEngineView extends SurfaceView implements SurfaceHolder.Callback
{
    /* Houses the logic for updating/drawing the game */
    GameEngine gEngine;
    
    /* Thread to update and draw the game engine */
    private PaintThread thread;

    /* Used to keep track of time between updates and amount of time to sleep for */
    long lastUpdate = 0;
    long sleepTime = 0;

    /* Reference which holds the surface related methods */
    SurfaceHolder surfaceHolder;
    
    /* Reference to the resources */
    Context context;

 
    /**
     * Init
     * Adds this view to the surface's callbacks,
     * Creates a new game engine
     * Creates a new paint thread
     * Takes focus
     */
    void Init(){
      //initialize our screen holder
      SurfaceHolder holder = getHolder();
      holder.addCallback(this);

      //initialize our game engine
      gEngine = new GameEngine();
      gEngine.Init(context.getResources());
  
      //initialize our Thread class. A call will be made to start it later
      thread = new PaintThread(holder, context, new Handler(), gEngine);
      setFocusable(true);
   }
 
   /**
    * Constructors
    * @param contextS
    * @param attrs
    * @param defStyle
    */
   public GameEngineView(Context contextS, AttributeSet attrs, int defStyle){
       super(contextS, attrs, defStyle);
       context=contextS;
       Init();
   }
   public GameEngineView(Context contextS, AttributeSet attrs){
       super(contextS, attrs);
       context=contextS;
       Init();
   }
 

   /** onTouchEvent
    * Handles the touch input for the main game screen.
    * @param event
    */
   @Override
   public boolean onTouchEvent(MotionEvent event) {
	return gEngine.onTouchEvent(event);
   }
   
   
    /**
     * surfaceChanged
     * Is called when this surface's characteristics are changed.
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
     */
    

    
    /**
     * surfaceDestroyed
     * Is called when this view is destroyed.
     */
    @Override 
    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean retry = true;
        // code to end gameloop
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

    
    /**
     * surfaceCreated
     * Creates a new thread if the game was paused, else calls start on the old thread.
     */
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

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {
	        thread.setSurfaceSize(width, height);
		
	}
}