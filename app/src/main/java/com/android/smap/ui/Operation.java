package com.android.smap.ui;

import android.util.Log;
import android.view.View;

/**
 * 
 * @author Bradley Curren
 * 
 *         Green Gear Library https://github.com/bradley-curran/GreenGear
 * @param <T>
 */
abstract class Operation<T extends View> {

	private static final String	TAG	= Operation.class.getSimpleName();
	/** Variable to store the type */
	private final Class<T>		mClass;

	Operation(Class<T> cls) {
		mClass = cls;
	}

	void run(View view) {
		T t = get(view, mClass);

		if (t != null) {
			execute(t);
		}
	}

	/**
	 * Execute the view operation
	 * 
	 * @param view
	 */
	abstract void execute(T view);

	/**
	 * Cast the view to the given type.
	 * 
	 * @param view
	 * @param cls
	 * @return null if the view is null or the view cannot be casted
	 */
	static <T extends View> T get(View view, Class<T> cls) {
		if (view == null) {
			Log.e(TAG, "View is null, cannot perform " + cls.getSimpleName());
			return null;
		}

		if (!cls.isInstance(view)) {
			Log.e(TAG, "View is not a " + cls.getSimpleName());
			return null;
		}

		return cls.cast(view);
	}
}
