package com.android.smap.api.models;

import com.android.smap.controllers.Controller;
import com.android.smap.controllers.GojoController;

public enum Goal {

	SUBSCRIBE("sub",
		GojoController.class),
	UNSUBSCRIBE("unsub",
		GojoController.class),
	REDO("redo",
		GojoController.class),
	RESET("reset",
		GojoController.class),
	ANSWER("",
		GojoController.class);

	String						mCommand;
	Class<? extends Controller>	mHandler;

	Goal(String str, Class<? extends Controller> c) {
		mCommand = str;
		mHandler = c;
	}

	public static class GoalUtil {
		public static Goal goalForString(String command) {
			for (Goal goal : Goal.values()) {
				if (command.compareToIgnoreCase(goal.toString()) == 0) {
					return goal;
				}
			}
			return null;
		}
	}

}
