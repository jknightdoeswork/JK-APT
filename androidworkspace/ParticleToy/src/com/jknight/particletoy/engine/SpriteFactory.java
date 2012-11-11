package com.jknight.particletoy.engine;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSurfaceView;

public class SpriteFactory<T extends AngleSprite> {

	private Class<? extends AngleSprite> _type;
	private int _size;
	private T[] _objects;
	private LinkedList<T> _freeList;
	private AngleSurfaceView _surface;
	
	
	public SpriteFactory(Class<? extends AngleSprite> type, int size, AngleSurfaceView surface) throws InstantiationException, IllegalAccessException {
		_type = type;
		_size = size;
		_freeList = new LinkedList<T>();
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
			Class<?>[] parameterTypes = {AngleSurfaceView.class};
			Object[] parameterValues = {_surface};
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
		T freeObject = _freeList.removeFirst();
		return freeObject;
	}
	
	public void returnFreeObject(T toReturn) {
		_freeList.addLast(toReturn);
	}
}
