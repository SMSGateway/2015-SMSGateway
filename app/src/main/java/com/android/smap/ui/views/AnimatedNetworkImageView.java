package com.android.smap.ui.views;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.webkit.URLUtil;

public class AnimatedNetworkImageView extends NetworkImageView {

	private static final int	FADE_IN_TIME_MS	= 250;
	private long				loadTime;

	public AnimatedNetworkImageView(Context context) {
		super(context);
	}

	public AnimatedNetworkImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AnimatedNetworkImageView(Context context,
			AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		long now = System.currentTimeMillis();
		if (now - loadTime > 50) {
			TransitionDrawable td = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(android.R.color.transparent),
					new BitmapDrawable(getResources(), bm) });
			setImageDrawable(td);
			td.startTransition(FADE_IN_TIME_MS);
		} else {
			setImageDrawable(new BitmapDrawable(getResources(), bm));
		}

	}

	@Override
	public void setImageUrl(String urlString, ImageLoader imageLoader) {
		loadTime = System.currentTimeMillis();
		if (URLUtil.isValidUrl(urlString)) {
			super.setImageUrl(urlString, imageLoader);
		}
	}

}
