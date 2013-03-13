package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

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
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "attribute vec2 texCoord;" +
        "varying vec2 texCoordOut;" +
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        "  texCoordOut = texCoord; \n" +
        "  gl_Position = uMVPMatrix * vPosition; \n" +
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "varying vec2 texCoordOut;" +
        "uniform sampler2D u_texture;" +
        "void main() {" +
        " vec4 texColor = texture2D(u_texture, texCoordOut);\n" +
        "  gl_FragColor = texColor;" +
        "}";

    
    
	public FluidEngine() {
		solver = new NavierStokesSolver(8);
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
        
        //         Upload the texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        MyGLRenderer.checkGlError("Texture bind");
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // TODO MUST SPECIFY WIDTH/HEIGHT.
        
        ByteBuffer _denseBuffer = ByteBuffer.allocateDirect(solver.SIZE * 4);
	    _denseBuffer.order(ByteOrder.nativeOrder());
	    FloatBuffer denseBuffer = _denseBuffer.asFloatBuffer();
		denseBuffer.put(solver.dense); // TODO DOES PUT COPY?
		denseBuffer.position(0);
		
		// EXTENSION: GL_FLOAT NOT INNATELY SUPPORTED
		// http://www.khronos.org/registry/gles/extensions/OES/OES_texture_float.txt
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_ALPHA, solver.SIZE, 1, 0, GLES20.GL_ALPHA, GLES20.GL_FLOAT, denseBuffer);
        MyGLRenderer.checkGlError("Texture bind");
        // Set the uniform to the data in texture unit 0
        GLES20.glUniform1i(mTextureHandle, 1);
        MyGLRenderer.checkGlError("Texture bind");
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);
	}
	
	public void loadShader() {
		mProgram = ShaderHelper.compileAndLinkShader(vertexShaderCode, fragmentShaderCode);
		
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		
		mTextureHandle = GLES20.glGetUniformLocation(mProgram, "u_texture");
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "texCoord");
        
        MyGLRenderer.checkGlError("Fluid Engine Load Shader");
	}
}
