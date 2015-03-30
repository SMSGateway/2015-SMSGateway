package com.android.smap.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;

/**
 * 
 * @author Bradley Curren
 * 
 *         Green Gear Library https://github.com/bradley-curran/GreenGear
 * @param <T>
 */
public abstract class ArrayVelocAdapter<T> extends VelocAdapter {

	private List<T>	mList;

	public ArrayVelocAdapter(Context context) {
		super(context);
		mList = new ArrayList<T>();
	}

	public ArrayVelocAdapter(Context context, List<T> list) {
		super(context);
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	public void add(T object) {
		mList.add(object);
		notifyDataSetChanged();
	}

	public void addAll(Collection<? extends T> collection) {
		mList.addAll(collection);
		notifyDataSetChanged();
	}

	public void addAll(T... items) {
		Collections.addAll(mList, items);
		notifyDataSetChanged();
	}

	public void insert(T object, int index) {
		mList.add(index, object);
		notifyDataSetChanged();
	}

	public void setList(List<T> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void remove(T object) {
		mList.remove(object);
		notifyDataSetChanged();
	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}
}
