package com.android.smap.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.smap.R;
import com.android.smap.utils.MWAssetsUtils;

/**
 * TextView implementation for setting the custom font in resources.<br/>
 * The font has to be set in the in styles.xml using the customTextFont
 * attribute. <br/>
 * This class checks if the font file is inside "assets/fonts/" and if the font
 * is present initializes the text with the specified font.
 * 
 * @author Matt Witherow
 */
public class FontableTextView extends TextView {

	public FontableTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	public FontableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public FontableTextView(Context context) {
		super(context);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs,
				R.styleable.FontableTextView);
		String customFont = a
				.getString(R.styleable.FontableTextView_customFont);
		String editorText = a
				.getString(R.styleable.FontableTextView_editorText);

		if (isInEditMode()) {
			// set editor text value
			if (!TextUtils.isEmpty(editorText)) {
				setText(editorText);
			} else {
				setText(FontableTextView.class.getSimpleName());
			}
		} else {
			// set font only when not in editor mode
			setCustomAssetFont(customFont);
		}

		a.recycle();

		setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}

	/**
	 * @param ctx
	 * @param font
	 * @return
	 */
	public boolean setCustomAssetFont(String font) {
		Context ctx = getContext();
		Typeface tf = MWAssetsUtils.getCachedFont(ctx, font);
		// check if could not find font
		if (tf == null) {
			return false;
		}

		setTypeface(tf);
		return true;
	}

}
