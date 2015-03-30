package com.android.smap.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Bradley Curren
 * 
 *         Green Gear Library https://github.com/bradley-curran/GreenGear
 * @param <T>
 */
public interface ViewBinder {

	public View newView(LayoutInflater inflator, int position, ViewGroup parent);

	public void bindView(Context context, View view, ViewQuery query,
			int position);
}
