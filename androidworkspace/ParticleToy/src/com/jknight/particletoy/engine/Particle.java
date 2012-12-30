package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.android.angle.AngleObject;
import com.jknight.particletoy.MyGLRenderer;
import com.jknight.particletoy.Utilities;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class Particle extends AngleObject{

	// String used to tag log cat messages
    private static final String LOG_TAG = "Particle";

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

	private SpriteFactory<Particle> mPFactory;
	public ParticleEngine mPEngine;
	private int mId;
	private float mX;
	private float mY;
	private float mVx;
	private float mVy;
	private float mScreenX;
	private float mScreenY;

    /** Open GL specific fields **/
    // Local Matrix for transforming this particle
    private float[] mMvpMatrix;
	

    public Particle(SpriteFactory<Particle> pFactory, ParticleEngine pEngine, int id) {
    	mPFactory = pFactory;
    	mPEngine = pEngine;
    	mId = id;
    	mMvpMatrix = new float[16];
    	
    }

    public void draw(float[] mVMatrix, float[] mProjMatrix) {
    	// Set up local transform matrix
    	Matrix.setIdentityM(mMvpMatrix, 0);
    	Matrix.translateM(mMvpMatrix, 0, mScreenX, mScreenY, 0.0f); // Translate before scale should be wrong but isnt....
    	Matrix.scaleM(mMvpMatrix, 0, 0.5f, 0.5f, 1.0f);
    	
    	// Apply view and projection matrices
    	Matrix.multiplyMM(mMvpMatrix, 0, mVMatrix, 0, mMvpMatrix, 0);
    	Matrix.multiplyMM(mMvpMatrix, 0, mProjMatrix, 0, mMvpMatrix, 0);
        
        // Set color for drawing the triangle
        GLES20.glUniform4fv(mPEngine.mColorHandle, 1, color, 0);
        
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mPEngine.mMVPMatrixHandle, 1, false, mMvpMatrix, 0);
        
        //GLES20.glUniform4f(mPEngine.mParticleCenterHandle, 0.0f, 0.0f, 0.0f, 1.0f);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");
        
        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mPEngine.drawOrder.length,
                              GLES20.GL_UNSIGNED_SHORT, mPEngine.drawListBuffer);

    }

    public void step(float timeElapsed) {
    	mX += mVx * timeElapsed;
    	mY += mVy * timeElapsed;
    }
    
    /**
     * x and y come in in screen coordinates.
     * @param x
     * @param y
     */
	public void init(float x, float y) {
		mX = x;
		mY = y-56; // TODO Get action bar size here
		mScreenX = (2*mX*mPEngine.renderer.ratio/mPEngine.renderer.viewportWidth) - mPEngine.renderer.ratio;
    	mScreenY = (-2*mY/mPEngine.renderer.viewportHeight) + 1;
	}
	
	public void onDie() {
		super.onDie();
		mPFactory.returnFreeObject(this);
	}
}