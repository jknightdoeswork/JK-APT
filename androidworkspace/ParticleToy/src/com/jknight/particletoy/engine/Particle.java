package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.android.angle.AngleObject;
import com.jknight.particletoy.MyGLRenderer;

import android.opengl.GLES20;
import android.util.Log;

public class Particle extends AngleObject{

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
                                    -0.5f, -0.5f, 0.0f,   // bottom left
                                     0.5f, -0.5f, 0.0f,   // bottom right
                                     0.5f,  0.5f, 0.0f }; // top right

    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

	private SpriteFactory<Particle> mPFactory;
	public ParticleEngine mPEngine;

    public Particle(SpriteFactory<Particle> pFactory, ParticleEngine pEngine) {
    	mPFactory = pFactory;
    	mPEngine = pEngine;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    public void draw(float[] mvpMatrix) {
    	Log.i("Particle", "program "+ mPEngine.mProgram);
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPEngine.mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPEngine.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mPEngine.mColorHandle, 1, color, 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mPEngine.mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                              GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPEngine.mPositionHandle);
    }

	public void init(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
	public void onDie() {
		super.onDie();
		mPFactory.returnFreeObject(this);
	}
}