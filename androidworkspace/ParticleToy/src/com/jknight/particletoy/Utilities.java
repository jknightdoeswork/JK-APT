package com.jknight.particletoy;

import android.util.Log;

public class Utilities {
    public static void displayMatrix(String tag, float[] matrix) {
    	String toDisplay = "\n";
    	for (int i=0; i < 4; i++) {
    		for(int j = 0; j< 4; j++) {
        		toDisplay += matrix[i+(j*4)] + "\t\t\t";
        	}
    		toDisplay += "\n";
    	}
    	Log.i(tag, toDisplay);
    }
    
    public static void displayVector(String tag, float[] matrix) {
    	String toDisplay = "\n";
    	for (int i=0; i < 4; i++) {
    		toDisplay += matrix[i] + "\t";
    	}
    	Log.i(tag, toDisplay);
    }
}
