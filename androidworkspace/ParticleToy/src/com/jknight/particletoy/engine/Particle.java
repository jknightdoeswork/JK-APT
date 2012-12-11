package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.jknight.particletoy.MyGLRenderer;

public class Particle extends AngleObject {

	public float _age;
	public ParticleComponent _mind; /* Controls how this particle updates */
	public SpriteFactory<Particle> _pFactory;
	public static float _globalTimeScale = 1.0f;
	public float hueRate = 1.0f;
	public float blinkRate = 1.0f;
	private final String vertexShaderCode =
	        // This matrix member variable provides a hook to manipulate
	        // the coordinates of the objects that use this vertex shader
	        "uniform mat4 uMVPMatrix;" +

	        "attribute vec4 vPosition;" +
	        "void main() {" +
	        // the matrix must be included as a modifier of gl_Position
	        "  gl_Position = vPosition * uMVPMatrix;" +
	        "}";
	private final String fragmentShaderCode =
	        "precision mediump float;" +
	        "uniform vec4 vColor;" +
	        "void main() {" +
	        "  gl_FragColor = vColor;" +
	        "}";

	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
	private final int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
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
    float mPosition[];
    float localMatrix[];

	public Particle(SpriteFactory<Particle> pFactory) {
		super();
		_pFactory = pFactory;
		mPosition = new float[4];
		localMatrix = new float[16];;
		_age=0.0f;
		_mind = ParticleComponent.sheep;
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

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
	}
	
	@Override
	protected void onDie() {
		_pFactory.returnFreeObject(this);
	}
	
	@Override
	public void step(float secondsElapsed) {
		_age += secondsElapsed * _globalTimeScale;
		_mind.step(this, secondsElapsed);
	}

	public void init(float x, float y, ParticleComponent mind) {
		_mind = mind;
		mPosition[0] = x;
		mPosition[1] = y;
		mPosition[2] = 1.0f;
		mPosition[3] = 0.0f;
		_age = 0.0f;
		_mind.init(this);
	}
	
	public void draw(float[] mvpMatrix) {
		// Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
//        Matrix.translateM(localMatrix, 0, mvpMatrix, 0, mPosition[0], mPosition[1], mPosition[2]);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, localMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                              GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
