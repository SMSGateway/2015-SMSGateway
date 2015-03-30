package com.android.smap.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This class is used to simplify wiring up UI. Create a new class by calling
 * <code>ViewQuery q = new ViewQuery(root);</code>
 * <p/>
 * Manipulate the UI by using the various accessor methods.
 * Green Gear Library
 * https://github.com/bradley-curran/GreenGear
 * 
 * @author bradleycurran
 */
public class ViewQuery {

	private static final int				INITIAL_CACHE_SIZE	= 8;

	/** The root view of this query */
	private final View						mView;

	/** Cache to store the view and its children */
	private final SparseArray<ViewQuery>	mCachedQueries;

	public ViewQuery(Activity activity) {
		this(activity.findViewById(android.R.id.content));
	}

	public ViewQuery(View view) {
		mView = view;
		mCachedQueries = new SparseArray<ViewQuery>(INITIAL_CACHE_SIZE);
	}

	/**
	 * Get the instance of the view that this ViewQuery is holding
	 * 
	 * @return
	 */
	public View get() {
		return mView;
	}

	/**
	 * Get the instance of the view in the given type
	 * 
	 * @param cls
	 *            type
	 * @return
	 */
	public <T extends View> T get(Class<T> cls) {
		return Operation.get(mView, cls);
	}

	/**
	 * Get the view query based on the given view id.</br> This id can be the
	 * child view of the root view
	 * 
	 * @param id
	 * @return ViewQuery of the given view
	 */
	public ViewQuery find(int id) {
		ViewQuery query = mCachedQueries.get(id);

		if (query == null) {
			if (mView == null) {
				query = new ViewQuery(mView);
			} else {
				query = new ViewQuery(mView.findViewById(id));
			}

			mCachedQueries.append(id, query);
		}

		return query;
	}

	public ViewQuery find(int... ids) {
		View view = mView;

		for (int i = 0; i < ids.length; i++) {
			if (view == null) {
				break;
			}

			view = view.findViewById(ids[i]);
		}

		return new ViewQuery(view);
	}

	/**
	 * Set {@link OnClickListener} to the view.
	 * 
	 * @param listener
	 * @return
	 * @see {@link android.view.View#setOnClickListener(OnClickListener)
	 *      View.setOnClickListener}
	 */
	public ViewQuery onClick(final OnClickListener listener) {
		return run(new Operation<View>(View.class) {

			@Override
			void execute(View view) {
				view.setOnClickListener(listener);
			}
		});
	}

	/**
	 * Set the view to be {@link android.view.View#GONE View.GONE} or
	 * {@link android.view.View#VISIBLE View.VISIBLE}
	 * 
	 * @param gone
	 * @return
	 * @see {@link android.view.View#setVisibility(int) View.setVisibility}
	 */
	public ViewQuery gone(final boolean gone) {
		return run(new Operation<View>(View.class) {

			@Override
			void execute(View view) {
				view.setVisibility(gone ? View.GONE : View.VISIBLE);
			}
		});
	}

	/**
	 * Set the view to be {@link View.INVISIBLE} or {@link View.VISIBILE}
	 * 
	 * @param invisible
	 * @return
	 * @see {@link android.view.View#setVisibility(int) View.setVisibility}
	 */
	public ViewQuery invisible(final boolean invisible) {
		return run(new Operation<View>(View.class) {

			@Override
			void execute(View view) {
				view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
			}
		});
	}

	/**
	 * Enable / Disable the view
	 * 
	 * @param enabled
	 * @return
	 * @see {@link android.view.View#setEnabled(boolean) View.setEnable}
	 */
	public ViewQuery enable(final boolean enabled) {
		return run(new Operation<View>(View.class) {

			@Override
			void execute(View view) {
				view.setEnabled(enabled);
			}
		});
	}

	/**
	 * Add child view to the current view
	 * 
	 * @param childView
	 * @return
	 * @see {@link android.view.ViewGroup#addView(View) ViewGroup.addView}
	 */
	public ViewQuery addView(final View childView) {
		return run(new Operation<ViewGroup>(ViewGroup.class) {

			@Override
			void execute(ViewGroup viewGroup) {
				viewGroup.addView(childView);
			}
		});
	}

	/**
	 * Add child view with the layout parameters to the current view
	 * ViewGroup.addView}
	 * 
	 * @param childView
	 * @param params
	 * @return
	 * @see {@link android.view.ViewGroup#addView(View, LayoutParams)

	 */
	public ViewQuery addView(final View childView, final LayoutParams params) {
		return run(new Operation<ViewGroup>(ViewGroup.class) {

			@Override
			void execute(ViewGroup viewGroup) {
				viewGroup.addView(childView, params);
			}
		});
	}

	/**
	 * Remove all views from the current view
	 * 
	 * @return
	 * @see {@link android.view.ViewGroup#removeAllViews()
	 *      ViewGroup.removeAllViews}
	 */
	public ViewQuery removeAllViews() {
		return run(new Operation<ViewGroup>(ViewGroup.class) {

			@Override
			void execute(ViewGroup viewGroup) {
				viewGroup.removeAllViews();
			}
		});
	}

	/**
	 * Set image to the current ImageView by resource id
	 * ImageView.setImageResource}
	 * 
	 * @param resId
	 * @return
	 * @see {@link android.widget.ImageView#setImageResource(int)

	 */
	public ViewQuery image(final int resId) {
		return run(new Operation<ImageView>(ImageView.class) {

			@Override
			void execute(ImageView view) {
				view.setImageResource(resId);
			}
		});
	}

	/**
	 * Set image to the current ImageView by resource bitmap
	 * 
	 * @param resource
	 * @return
	 * @see {@link android.widget.ImageView#setImageBitmap(Bitmap)
	 *      ImageView.setImageBitmap}
	 */
	public ViewQuery image(final Bitmap resource) {
		return run(new Operation<ImageView>(ImageView.class) {

			@Override
			void execute(ImageView view) {
				view.setImageBitmap(resource);
			}
		});
	}

	/**
	 * Set text to the current TextView by resource id
	 * 
	 * @param resId
	 * @return
	 * @see {@link android.widget.TextView#setText(int) TextView.setText}
	 */
	public ViewQuery text(final int resId) {
		return run(new Operation<TextView>(TextView.class) {

			@Override
			void execute(TextView view) {
				view.setText(resId);
			}
		});
	}

	/**
	 * Set text to the current TextView by CharSequence
	 * 
	 * @param str
	 * @return
	 * @see {@link android.widget.TextView#setText(CharSequence)
	 *      TextView.setText}
	 */
	public ViewQuery text(final CharSequence str) {
		return run(new Operation<TextView>(TextView.class) {

			@Override
			void execute(TextView view) {
				view.setText(str);
			}
		});
	}

	/**
	 * Set text style to the current TextView
	 * 
	 * @param style
	 * @return
	 * @see {@link android.widget.TextView#setTypeface(android.graphics.Typeface, int)
	 *      TextView.setTypeface}
	 */
	public ViewQuery typefaceStyle(final int style) {
		return run(new Operation<TextView>(TextView.class) {

			@Override
			void execute(TextView view) {
				view.setTypeface(view.getTypeface(), style);
			}
		});
	}

	/**
	 * Set text color to the current TextView
	 * 
	 * @param color
	 * @return
	 * @see {@link android.widget.TextView#setTextColor(int)
	 *      TextView.setTextColor}
	 */
	public ViewQuery textColor(final int color) {
		return run(new Operation<TextView>(TextView.class) {

			@Override
			void execute(TextView view) {
				view.setTextColor(color);
			}
		});
	}

	/**
	 * Set {@link smoothScrollTo} to the current view
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @see {@link android.widget.HorizontalScrollView#smoothScrollTo(int, int)
	 *      HorizontalScrollView.smoothScrollTo}
	 */
	public ViewQuery smoothScrollTo(final int x, final int y) {
		return run(new Operation<HorizontalScrollView>(
				HorizontalScrollView.class) {

			@Override
			void execute(HorizontalScrollView view) {
				view.smoothScrollTo(x, y);
			}
		});
	}

	/**
	 * Set background resource to the current view
	 * 
	 * @param resId
	 * @return
	 * @see {@link android.view.View#setBackgroundResource(int)
	 *      View.setBackgroundResource}
	 */
	public ViewQuery background(final int resId) {
		return run(new Operation<View>(View.class) {

			@Override
			void execute(View view) {
				view.setBackgroundResource(resId);
			}
		});
	}

	/**
	 * Set adapter to the current {@link AdapterView}
	 * 
	 * @param adapter
	 * @return
	 * @see {@link android.widget.AdapterView#setAdapter(Adapter)
	 *      AdapterView.setAdapter}
	 */
	@SuppressWarnings("rawtypes")
	public ViewQuery adapter(final Adapter adapter) {
		return run(new Operation<AdapterView>(AdapterView.class) {

			@SuppressWarnings("unchecked")
			@Override
			void execute(AdapterView view) {
				view.setAdapter(adapter);
			}
		});
	}

	/**
	 * Set {@link OnItemClickListener} to the current {@link AdapterView}
	 * 
	 * @param listener
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ViewQuery onItemClicked(final OnItemClickListener listener) {
		return run(new Operation<AdapterView>(AdapterView.class) {

			@Override
			void execute(AdapterView view) {
				view.setOnItemClickListener(listener);
			}
		});
	}

	/**
	 * Set {@link OnItemSelectedListener} to the current {@link AdapterView}
	 * 
	 * @param listener
	 * @return
	 * @see {@link android.widget.AdapterView#setOnItemSelectedListener(OnItemSelectedListener)
	 *      AdapterView.setOnItemSelectedListener}
	 */
	@SuppressWarnings("rawtypes")
	public ViewQuery onItemSelected(final OnItemSelectedListener listener) {
		return run(new Operation<AdapterView>(AdapterView.class) {

			@Override
			void execute(AdapterView view) {
				view.setOnItemSelectedListener(listener);
			}
		});
	}

	/**
	 * Set the selected item in the current {@link Spinner} view
	 * 
	 * @param position
	 * @return
	 * @see {@link android.widget.AbsSpinner#setSelection(int, boolean)
	 *      Spinner.setSelection}
	 */
	public ViewQuery selection(final int position) {
		return run(new Operation<Spinner>(Spinner.class) {

			@Override
			void execute(Spinner view) {
				view.setSelection(position, false);
			}
		});
	}

	/**
	 * Determine if the current TextView has text.
	 * <p>
	 * Calling this will prevent you from further method chaining.
	 * 
	 * @return true if the current TextView has text.
	 *         <p>
	 *         false if there is no text or if the View is not a TextView.
	 */
	public boolean hasText() {
		return !TextUtils.isEmpty(getText());
	}

	/**
	 * Get the contents of the current TextView.
	 * <p>
	 * Calling this will prevent you from further method chaining.
	 * 
	 * @return The text of the current TextView.
	 */
	public String getText() {
		TextView tv = Operation.get(mView, TextView.class);

		if (tv == null) {
			return "";
		}

		return tv.getText().toString();
	}

	/**
	 * Function to run the current query with the given operation
	 * 
	 * @param o
	 * @return
	 */
	private <T extends View> ViewQuery run(Operation<T> o) {
		o.run(mView);
		return this;
	}
}
