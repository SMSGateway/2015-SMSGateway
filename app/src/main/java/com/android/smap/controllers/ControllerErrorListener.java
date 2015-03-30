package com.android.smap.controllers;

/**
 * In the case when a controller fails to execute its action, pass the error to
 * the correct handler
 * 
 * @author Matt Witherow
 * 
 */
public interface ControllerErrorListener {
	public void onControllerError(ControllerError error);
}
