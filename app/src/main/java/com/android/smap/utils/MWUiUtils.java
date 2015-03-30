package com.android.smap.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.smap.R;

public class MWUiUtils {

	/**
	 * Hide keyboard
	 * 
	 * @param Activity
	 */
	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm != null && activity != null) {
			View currentFocus = activity.getCurrentFocus();

			if (currentFocus != null) {
				imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
			}
		}
	}

	/**
	 * Turns a textview into a clickable hyperlink with the provided text and
	 * attaches an OnClickListener.
	 * 
	 * @param text
	 * @param view
	 * @param lis
	 */
	public static void makeHyperlink(String text, TextView view,
			OnClickListener lis) {
		SpannableString ss = new SpannableString(text);

		ss.setSpan(new UnderlineSpan(), 0, ss.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(ss);
		view.setMovementMethod(LinkMovementMethod
				.getInstance());
		view.setOnClickListener(lis);
	}

	/**
	 * Show simple error popup messages
	 * 
	 * @param messageResource
	 */
	public static void showMessagePopup(Context context, String message) {
		showMessagePopup(context, message,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});
	}

	/**
	 * Used to show error popup messages.
	 * 
	 * @param messageResource
	 */
	private static void showMessagePopup(Context context,
			String message,
			DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(R.string.ok, listener)
				.show();
	}

	public static float dipToPixels(Context context, float dip) {
		Resources r = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}

}
