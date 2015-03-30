package com.android.smap.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author Bradley Curren
 * 
 *         Green Gear Library https://github.com/bradley-curran/GreenGear
 * @param <T>
 */
public abstract class VelocAdapter extends BaseAdapter implements ViewBinder {

	private Context	mContext;

	public VelocAdapter(Context context) {
		mContext = context;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (convertView == null) {
			view = newView(LayoutInflater.from(mContext), position, parent);
			view.setTag(new ViewQuery(view));
		}

		ViewQuery query = (ViewQuery) view.getTag();
		bindView(mContext, view, query, position);

		return view;
	}

	public Context getContext() {
		return mContext;
	}
}
