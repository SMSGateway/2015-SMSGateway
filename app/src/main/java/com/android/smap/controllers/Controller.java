package com.android.smap.controllers;

/**
 * Base controller, handle data, supply action.
 * 
 * @author Matt Witherow
 * 
 */
public interface Controller {
	ControllerListener getControllerListener();

	ControllerErrorListener getControllerErrorListener();

	void start();

	void cancel();
}
