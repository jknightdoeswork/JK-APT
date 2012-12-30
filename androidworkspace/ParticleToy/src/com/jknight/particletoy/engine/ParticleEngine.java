package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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
    public int mParticleCenterHandle; // Center of particle
    public int mMVPMatrixHandle; // Model-View-Projection Matrix Shader Handle
    
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

    /*
     * Data to draw a square
     */
    private final FloatBuffer vertexBuffer;
    public final ShortBuffer drawListBuffer;
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
                                    -0.5f, -0.5f, 0.0f,   // bottom left
                                     0.5f, -0.5f, 0.0f,   // bottom right
                                     0.5f,  0.5f, 0.0f }; // top right

    public final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    
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
	
	public void draw(float[] mVMatrix, float[] mProjMatrix) {
    	// Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);
		for (int t=0;t<mChildsCount;t++) {
			Particle child = (Particle)mChilds[t];
			child.draw(mVMatrix, mProjMatrix);
		}
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
			
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
        MyGLRenderer.checkGlError("Vertex Shader");
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);
        MyGLRenderer.checkGlError("Fragment Shader");
		mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        Log.i("Program Handle", " " + mProgram);
        MyGLRenderer.checkGlError("glUseProgramLocation");
        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        MyGLRenderer.checkGlError("glGetAttribLocation");
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        MyGLRenderer.checkGlError("glGetUniformLocation");
        // get handle to fragment shader's particleCenter member
        // mParticleCenterHandle = GLES20.glGetUniformLocation(mProgram, "particleCenter");
        
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
}