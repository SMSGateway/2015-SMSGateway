package com.android.smap.api.models;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;

/**
 * Goal Oriented Java Object
 * 
 * @author Matt Witherow
 * 
 */
public class Gojo {

	@Expose
	public Goal		goal;
	@Expose
	public String	model;
	public String	reference;

	@SuppressLint("NewApi")
	public void go() {
/*		try {
			Controller c = goal.mHandler.newInstance();
			c.start();
		}
		catch (InstantiationException | IllegalAccessException e) {

			Log.e(Gojo.class.getName(), "NO CONTROLLER FOUND");
		}*/
	}
}
