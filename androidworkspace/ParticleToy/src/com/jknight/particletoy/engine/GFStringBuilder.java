package com.jknight.particletoy.engine;

/**
 * Singleton Character Buffer Class to prevent the memory allocation of the String class in java
 * Taken from  David Scherfgen
 * http://stackoverflow.com/questions/2484079/how-can-i-avoid-garbage-collection-delays-in-java-games-best-practices
 * which links to
 * http://pastebin.com/s6ZKa3mJ
 */
public final class GFStringBuilder
{
	private final char[] result;
	private String format;
	private int formatCursor;
	private int resultCursor;
	
	private static GFStringBuilder inst = new GFStringBuilder(128);
	
	private GFStringBuilder(int capacity)
	{
		result = new char[capacity];
	}
	
	// Startet die Formatierung mit einem Format-String.
	public static GFStringBuilder format(String format)
	{
		inst.format = format;
		inst.formatCursor = 0;
		inst.resultCursor = 0;
		return inst;
	}
	
	// Ersetzt den nächsten Platzhalter durch einen Integer.
	public GFStringBuilder eat(int value)
	{
		moveToNextPlaceholder();
		
		if (value < 0)
		{
			result[resultCursor++] = '-';
			value = -value;
		}
		else if (value == 0)
		{
			result[resultCursor++] = '0';
			return inst;
		}
		
		// Integer in Text umwandeln.
		int t = 1000000000;
		while (value <= t) t /= 10;
		while (t > 0)
		{
			int d = value / t;
			result[resultCursor++] = (char)('0' + d);
			value -= d * t;
			t /= 10;
		}
		
		return inst;
	}
	
	// Ersetzt den nächsten Platzhalter durch einen String.
	public GFStringBuilder eat(char[] seq)
	{
		moveToNextPlaceholder();
		
		for (int i = 0; seq[i] != 0 && i < seq.length; ++i) result[resultCursor++] = seq[i];
		return inst;
	}
	
	// Ersetzt den nächsten Platzhalter durch einen String.
	public GFStringBuilder eat(String str)
	{
		moveToNextPlaceholder();
		
		int length = str.length();
		for (int i = 0; i < length; ++i) result[resultCursor++] = str.charAt(i);
		return inst;
	}
	
	// Liefert den finalen String.
	public char[] get()
	{
		result[resultCursor] = 0;
		return result;
	}
	
	private void moveToNextPlaceholder()
	{
		while (true)
		{
			char c = format.charAt(formatCursor++);
			if (c == '%') break;
			result[resultCursor++] = c; 
		}
	}
}
