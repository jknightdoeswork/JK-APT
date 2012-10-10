package com.jknight.particletoy.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.jknight.particletoy.engine.GameEngine;

public class PaintThread extends Thread{
    private SurfaceHolder mSurfaceHolder;
    private Handler mHandler;
    private Context mContext;
    private Paint mLinePaint;
    private Paint blackPaint;
    GameEngine gEngine;
    
    /** TEMP */
    private Bitmap mBackgroundImage;
    
    //for consistent rendering
    private long sleepTime;
    //amount of time to sleep for (in milliseconds)
    private long delay=70;
    
    //state of game (Running or Paused).
    int state = 1;
	private long before_time;
    public final static int RUNNING = 1;
    public final static int PAUSED = 2;

    public PaintThread(SurfaceHolder surfaceHolder, Context context, Handler handler, 
                               GameEngine gEngineS) {
            
        //data about the screen
        mSurfaceHolder = surfaceHolder;
        this.mHandler = handler;
        this.mContext = context;
        Resources res = context.getResources();
        
        //standard game painter. Used to draw on the canvas
        mLinePaint = new Paint();
        mLinePaint.setARGB(255, 0, 255, 0);
        //black painter below to clear the screen before the game is rendered
        blackPaint = new Paint();
        blackPaint.setARGB(255, 255, 255, 255);
        //mLinePaint.setAntiAlias(true);
        
        mBackgroundImage = BitmapFactory.decodeResource(res,
                com.jknight.particletoy.R.drawable.earthrise);
        gEngine=gEngineS;
    }


    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (mSurfaceHolder) {

            // don't forget to resize the background image
            mBackgroundImage = Bitmap.createScaledBitmap(
                    mBackgroundImage, width, height, true);
        }
    }
    
    //This is the most important part of the code. It is invoked when the call to start() is
    //made from the SurfaceView class. It loops continuously until the game is finished or
    //the application is suspended.
    @Override
    public void run() {

	     //UPDATE
	     while (state==RUNNING) {
	           //time before update
	           long current_time = System.nanoTime();
	           long elapsed_time = current_time - before_time;
	           before_time = elapsed_time;
	           //This is where we update the game engine
	           gEngine.Update(elapsed_time);
	
	     //DRAW
	     Canvas c = null;
	     try {
	           //lock canvas so nothing else can use it
	           c = mSurfaceHolder.lockCanvas(null);
	           synchronized (mSurfaceHolder) {
	                //clear the screen with the black painter.
	                c.drawRect(0, 0, c.getWidth(), c.getHeight(), blackPaint);
	                //This is where we draw the game engine.
//	                gEngine.Draw(c);
//	                c.drawCircle(0, 0, 100.0f, blackPaint);
	         }
	     } finally {
	         // do this in a finally so that if an exception is thrown
	         // during the above, we don't leave the Surface in an
	         // inconsistent state
	         if (c != null) {
	             mSurfaceHolder.unlockCanvasAndPost(c);
	         }
	     }
	
	
	
	     //SLEEP
	     //Sleep time. Time required to sleep to keep game consistent
	     //This starts with the specified delay time (in milliseconds) then subtracts from that the
	     //actual time it took to update and render the game. This allows our game to render smoothly.
	     this.sleepTime = delay-((System.nanoTime()-current_time)/1000000L);
	
	            try {
	                //actual sleep code
	                if(sleepTime>0){
	                this.sleep(sleepTime);
	                }
	            } catch (InterruptedException ex) {
	                Logger.getLogger(PaintThread.class.getName()).log(Level.SEVERE, null, ex);
	            }
	     }

    }
}