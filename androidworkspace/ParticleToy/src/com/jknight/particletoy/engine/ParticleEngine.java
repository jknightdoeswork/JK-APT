package com.jknight.particletoy.engine;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.util.Log;

import com.android.angle.AngleObject;
import com.jknight.particletoy.MyGLRenderer;
import com.jknight.particletoy.R;

public class ParticleEngine extends AngleObject {
	
	/** PARTICLE ENGINE FIELDS **/
	SpriteFactory<Particle> _pFactory;
	public ParticleUI mPUI;

	/** SHADER FIELDS **/
    public int mProgram; // Currently running shader
    public int mPositionHandle; // Handles to uniforms of currently running shader
    public int mColorHandle;
    public int mMVPMatrixHandle;
    
    /** Renderer reference **/
    public MyGLRenderer renderer;
    
    /** TEMP SHADER CODE **/
    private final String vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +

        "attribute vec4 vPosition;" +
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        "  gl_Position =  uMVPMatrix * vPosition; \n" +
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}";

	public ParticleEngine(int maxChildren, MyGLRenderer pRenderer) {
		super(maxChildren);
		renderer = pRenderer;
		/** PARTICLE ENGINE CONSTRUCTION **/
		try {
			_pFactory = new SpriteFactory<Particle>(Particle.class, maxChildren, this);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		mPUI = new ParticleUI(this);
		
		attachShader(vertexShaderCode, fragmentShaderCode);
	}
	
	/**
	 * Attaches the supplied shader code to the gl context.
	 * Stores program handle in mProgram.
	 * Stores uniform handles mPositionHandle, mColorHandle, mMVPMatrixHandle.
	 * @param vertexShaderCode String of compilable vertex shader code.
	 * @param fragmentShaderCode String of compilable fragment shader code.
	 */
	public void attachShader(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);
		mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        Log.i("Program Handle", " " + mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        
	}
	public void addParticle(float x, float y) {
		Particle particle = _pFactory.getFreeObject();
		if (null != particle) {
			particle.init(x, y);
			addObject(particle);
		}
	}
	
	public void removeParticle(Particle particle) {
		particle.mDie = true; // Particle will hand itself back to the pFactory
	}

	public void clear() {
		removeAll();
		_pFactory.freeAll();
	}

	public void draw(float[] mVMatrix, float[] mProjMatrix) {
		for (int t=0;t<mChildsCount;t++) {
			Particle child = (Particle)mChilds[t];
			child.draw(mVMatrix, mProjMatrix);
		}
			
	}
}
