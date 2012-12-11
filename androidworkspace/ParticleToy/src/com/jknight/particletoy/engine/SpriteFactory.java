package com.jknight.particletoy.engine;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.util.Log;

import com.android.angle.AngleObject;

public class SpriteFactory<T extends AngleObject> {

	private Class<? extends AngleObject> _type;
	private int _size;
	private T[] _objects;
	private LinkedList<T> _freeList;
	private int _drawableId;
	
	
	public SpriteFactory(Class<? extends AngleObject> type, int size, int drawableId) throws InstantiationException, IllegalAccessException {
		_type = type;
		_size = size;
		_freeList = new LinkedList<T>();
		_drawableId = drawableId;
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
			Class<?>[] parameterTypes = {SpriteFactory.class};
			Object[] parameterValues = {this};
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
	public void freeAll() {
		_freeList.clear();
		for (T sprite : _objects) {
			_freeList.addLast(sprite);
		}
		
	}
}
