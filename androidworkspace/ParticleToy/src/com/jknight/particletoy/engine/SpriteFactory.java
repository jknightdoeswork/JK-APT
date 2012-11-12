package com.jknight.particletoy.engine;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.util.Log;

import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleSurfaceView;

public class SpriteFactory<T extends AngleSprite> {

	private Class<? extends AngleSprite> _type;
	private int _size;
	private T[] _objects;
	private LinkedList<T> _freeList;
	private int _drawableId;
	private AngleSurfaceView _surface;	
	
	
	public SpriteFactory(Class<? extends AngleSprite> type, int size, int drawableId, AngleSurfaceView surface) throws InstantiationException, IllegalAccessException {
		_type = type;
		_size = size;
		_freeList = new LinkedList<T>();
		_drawableId = drawableId;
		_surface = surface;
		allocateObjects();
	}
	/**
	 * Allocate size many objects of type type.
	 * @param <T>
	 * @param type
	 * @param size
	 * @return 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public void allocateObjects() {
		_freeList.clear();
		_objects = (T[]) Array.newInstance(_type, _size);
		for (int i = 0; i < _size; i++) {
			AngleSpriteLayout layout = new AngleSpriteLayout(_surface, _drawableId);
			Class<?>[] parameterTypes = {AngleSpriteLayout.class, SpriteFactory.class};
			Object[] parameterValues = {layout, this};
			try {
				_objects[i] = (T) _type.getConstructor(parameterTypes).newInstance(parameterValues);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_freeList.addLast(_objects[i]);
		}		
	}
	
	public T getFreeObject() {
		try {
			T freeObject = _freeList.removeFirst();
			return freeObject;
		}
		catch (NoSuchElementException e) {
			Log.w("SpriteFactoryEmpty", _type.toString(), e);
			return null;
		}
	}
	
	public void returnFreeObject(T toReturn) {
		_freeList.addLast(toReturn);
	}
}
