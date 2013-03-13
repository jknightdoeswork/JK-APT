package com.jknight.particletoy.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;

import com.jknight.particletoy.R;

/**
 * Quad
 * Stores vertex data to draw a quad.
 * @author jknight
 *
 */
public class Quad {
    //DRAW DATA
    public final FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static float squareCoords[] = {-1.0f, 1.0f, 0.0f,				
								   -1.0f, -1.0f, 0.0f,
									1.0f, 1.0f, 0.0f, 
								   -1.0f, -1.0f, 0.0f, 				
									1.0f, -1.0f, 0.0f,
									1.0f, 1.0f, 0.0f,}; // top right
    static int squareCoordsPerVertex = 3;

    
    public Quad() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4); // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
    }
}
