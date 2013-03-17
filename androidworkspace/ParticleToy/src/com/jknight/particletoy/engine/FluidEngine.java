package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class FluidEngine {

	private NavierStokesSolver solver;
	private TexturedQuad quad;
	
	//  Shader Handles
	int mProgram;
	int mMVPMatrixHandle;
	int mPositionHandle;
	int mTextureHandle;
	int mTexCoordHandle;
	int mTextureDataHandle;
	
    private final String vertexShaderCode =
        "uniform mat4 uMVPMatrix;					\n" +
        "attribute vec4 vPosition;					\n" +
        "attribute float texCoord;					\n" +
        "varying float texCoordOut;					\n"	+
        "void main() {								\n" +
        "  texCoordOut = texCoord;					\n" +
        "  gl_Position = uMVPMatrix * vPosition; 	\n" +
        "}											\n";

    private final String fragmentShaderCode =
    	"#pragma optimize(off)\n" +
        "precision mediump float;					\n" +
        "varying vec2 texCoordOut;					\n" +
        "uniform float densities[100];				\n" +
        "void main() {								\n" +
        "  float i = texCoordOut.x;					\n" +
        "  float j = texCoordOut.y;					\n" +
        "  int denseIndex = int(j*10.0 + i);		\n" +
        "  float denseValue = densities[denseIndex];\n" +
        "  vec4 color = vec4(1.0, 1.0, 1.0, 1.0) * denseValue;\n" +
        "  gl_FragColor = color;					\n" +
        "}";

//    	private final String fragmentShaderCode =
//			"void main() {								\n" +
//			"  gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);\n" +
//			"}											\n";

    
    
	public FluidEngine() {
		loadShader();
		solver = new NavierStokesSolver(8);
		Log.i("Fluid Engine", "Solver Size: "+ solver.SIZE);
		quad = new TexturedQuad();
		
	}
	
	public void update(float elapsed) {
		solver.tick(elapsed, 1.0f, 1.0f);
	}

	public void draw() {
		GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        // Upload the vertices and tex coords
        GLES20.glVertexAttribPointer(mPositionHandle, Quad.squareCoordsPerVertex,
                                     GLES20.GL_FLOAT, false,
                                     0, quad.vertexBuffer);
        
        GLES20.glVertexAttribPointer(mTexCoordHandle, TexturedQuad.textureCoordsPerVertex,
        							 GLES20.GL_FLOAT, false,
        							 0, quad.texCoordBuffer);
        GLES20.glUniform1fv(mTextureHandle, solver.SIZE, solver.dense, 0);
        Log.i("FLUID ENGINE", "SIZE: " + solver.SIZE + " textureHandle: " + mTextureHandle + " texCoordHandle: " + mTexCoordHandle + " positionhandle: " + mPositionHandle);;
        MyGLRenderer.checkGlError("Texture bind");

        // TODO MUST SPECIFY WIDTH/HEIGHT.
        
//        ByteBuffer _denseBuffer = ByteBuffer.allocateDirect(solver.SIZE * 4);
//	    _denseBuffer.order(ByteOrder.nativeOrder());
//	    FloatBuffer denseBuffer = _denseBuffer.asFloatBuffer();
//		denseBuffer.put(solver.dense); // TODO DOES PUT COPY?
//		denseBuffer.position(0);
//        
//		//         Upload the texture
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
//        MyGLRenderer.checkGlError("Texture bind");
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//		// EXTENSION: GL_FLOAT NOT INNATELY SUPPORTED
//		// http://www.khronos.org/registry/gles/extensions/OES/OES_texture_float.txt
//        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_ALPHA, solver.SIZE, 1, 0, GLES20.GL_ALPHA, GLES20.GL_FLOAT, denseBuffer);
//        MyGLRenderer.checkGlError("Texture bind");
//        // Set the uniform to the data in texture unit 0
//        GLES20.glUniform1i(mTextureHandle, 1);
//        MyGLRenderer.checkGlError("Texture bind");
        
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);
	}
	
	public void loadShader() {
		mProgram = ShaderHelper.compileAndLinkShader(vertexShaderCode, fragmentShaderCode);
		MyGLRenderer.checkGlError("Fluid Engine Load Shader");
		GLES20.glUseProgram(mProgram);
		MyGLRenderer.checkGlError("Fluid Engine Load Shader");
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		
		mTextureHandle = GLES20.glGetUniformLocation(mProgram, "densities");
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "texCoord");

        Log.i("FluidEngine", "mProgram: " + mProgram + " mMVPMatrixHandle: " + mMVPMatrixHandle + " mTextureHandle: " + mTextureHandle + " mPositionHandle: " + mPositionHandle + " mTexCoordHandle " + mTexCoordHandle);
        
        MyGLRenderer.checkGlError("Fluid Engine Load Shader");
	}
}
