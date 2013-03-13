package com.jknight.particletoy.engine;
/** Taken from
 * https://raw.github.com/learnopengles/Learn-OpenGLES-Tutorials/master/android/AndroidOpenGLESLessons/src/com/learnopengles/android/common/ShaderHelper.java 
 * Used under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
 
import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper
{
	public static int compileAndLinkShader(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = compile(vertexShaderCode, GLES20.GL_VERTEX_SHADER);
		
		int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
		                  fragmentShaderCode);
		
		int mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		
		return mProgram;
	}
	
	/**
	 * Returns a handle to a shader.
	 * @param shaderCode A string of glsl code.
	 * @param type must be either GLES20.GL_VERTEX_SHADER or GLES20.GLFRAGMENT_SHADER.
	 * @return a handle of the shader compiled on the GPU.
	 */
	private static int compile(String shaderCode, int type) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
	}
}