package com.android.smap.models;

public class MenuDrawerItem {

	private final String	mName;
	private final int		mImageId;

	public MenuDrawerItem(String name, int id) {
		mName = name;
		mImageId = id;
	}

	public String getName() {
		return mName;
	}

	public int getId() {
		return mImageId;
	}
}
