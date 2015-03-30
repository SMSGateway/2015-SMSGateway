package com.android.smap.api.models.validator;

import java.lang.reflect.Field;
import java.util.List;

import android.util.Log;

public class ModelValidator {

	private static final String	TAG	= ModelValidator.class.getSimpleName();

	/**
	 * Validates a given model object. Attach @Required to any fields that
	 * should not be null in your model class. Once you have a model object,
	 * call this method to determine if any of those fields are null.
	 * 
	 * @param o
	 *            The model object.
	 * @return false if any @Required fields are null. true otherwise.
	 */
	public static boolean isModelValid(Object o) {
		if (o == null) {
			return false;
		}

		Field[] fields = o.getClass().getFields();

		for (Field field : fields) {
			Required required = field.getAnnotation(Required.class);

			if (required != null) {
				if (!isRequiredFieldValid(o, field)) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean isRequiredFieldValid(Object parent, Field field) {
		try {
			Object required = field.get(parent);

			if (required == null) {
				Log.e(TAG, "null object found for field " + field.getName());
				return false;
			}

			if (required instanceof List) {
				List<?> list = (List<?>) required;

				for (Object listItem : list) {
					if (!isModelValid(listItem)) {
						return false;
					}
				}
			} else if (!isModelValid(required)) {
				return false;
			}
		}
		catch (IllegalArgumentException e) {
			Log.e(TAG, "Object not compatible with the Field", e);
			return false;
		}
		catch (IllegalAccessException e) {
			Log.e(TAG, "security risk : cannot access field", e);
			return false;
		}

		return true;
	}
}
