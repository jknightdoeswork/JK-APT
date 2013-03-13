package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * TexturedQuad
 * Stores the data to draw a quad with texture coordinates.
 * @author jknight
 */
public class TexturedQuad extends Quad {
	static float textureCoords[] = { 0.0f, 0.0f, 				
		0.0f, 1.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,
		1.0f, 0.0f}; // top right

	static int textureCoordsPerVertex = 2;
    
    public final FloatBuffer texCoordBuffer;

	public int textureDataHandle; // handle to the texture data we loaded onto GPU
	
	public TexturedQuad() {
		super();
	    ByteBuffer tcbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
	    tcbb.order(ByteOrder.nativeOrder());
	    texCoordBuffer = tcbb.asFloatBuffer();
		texCoordBuffer.put(textureCoords);
		texCoordBuffer.position(0);
	}
	public static void uploadTexture(int handle, Bitmap data) {
        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, data, 0);
	}
}
