package com.android.smap.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.smap.R;
import com.android.smap.activities.BaseActivity;
import com.android.smap.ui.views.FontableTextView;

/**
 * <pre>
 * BaseFragment acts as a superclass for all major fragments in the application.
 *
 * BaseFragment provides:
 *      The new/next device-agnostic navigation
 *      Resetting the action bar title as you navigate.
 * </pre>
 * <p/>
 * BaseFragment also acts as a suitable bottleneck to attach behaviors such as
 * Google Analytics or Deep Linking.
 *
 * @author matt witherow
 */

public abstract class BaseFragment extends Fragment {

    public static final String EXTRA_DEEP_LINK_DATA = BaseFragment.class
            .getName()
            + ".EXTRA_DEEP_LINK_DATA";
    private RelativeLayout mLayoutContainer;
    private RelativeLayout mLayoutProgress;
    private View mContentView;

    public abstract View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (hasActionBarTitle()) {
            getActionBar().setTitle("");
            getActionBar().setCustomView(R.layout.view_action_bar);
            ((FontableTextView) (getActionBar().getCustomView())).setText(getActionBarTitle());
        }

    }

    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   ViewGroup container,
                                   Bundle savedInstanceState) {

        mLayoutContainer = (RelativeLayout) inflater
                .inflate(R.layout.fragment_base_data, null);
        mLayoutProgress = (RelativeLayout) mLayoutContainer
                .findViewById(R.id.layout_loading);

        mLayoutProgress.requestDisallowInterceptTouchEvent(false);
        mLayoutProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // add content to the view
        mContentView = onCreateContentView(inflater,
                savedInstanceState);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        mLayoutContainer.addView(mContentView, 0, params);

//        mLayoutError = new ErrorView(getActivity(), this);
//        mLayoutContainer.addView(mLayoutError, 1);

        return mLayoutContainer;
    }

    public void showLoading(boolean loading) {
        if (mLayoutProgress != null) {
            mLayoutProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (hasActionBarTitle()) {
            ((FontableTextView) (getActionBar().getCustomView())).setText("");
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        BaseActivity b = ((BaseActivity) getActivity());
        b.setupActionBar();
    }

    /**
     * Function to get the action bar instance
     *
     * @return
     */
    public ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * full screen regardless of tablet or phone
     *
     * @return
     */
    public boolean isFullScreen() {
        return false;
    }

    /**
     * Whether or not this fragment wants to display a custom action bar title.
     * Subclasses that return true can return the action bar title that they
     * wish to display from getActionBarTitle. When a fragment that returns true
     * in hasActionBarTitle is detached the action bar title will be reset to
     * the app name.
     *
     * @return
     */
    public boolean hasActionBarTitle() {
        return false;
    }

    /**
     * The action bar title to use when this fragment is displayed. Note: if
     * this fragment wants this title to be displayed it must also override
     * hasActionBarTitle and return true
     *
     * @return
     */
    public String getActionBarTitle() {
        return "";
    }

    public void popFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

    /**
     * Push a new fragment onto the stack.
     *
     * @param cls The fragment class to push.
     */
    public void pushFragment(Class<?> cls) {
        pushFragment(cls, null);
    }

    /**
     * Push a new fragment onto the stack.
     *
     * @param cls  The fragment class to push.
     * @param args The arguments provided to the fragment.
     */
    public void pushFragment(Class<?> cls, Bundle args) {
        Fragment f = Fragment.instantiate(getActivity(), cls.getName());
        f.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container, f);
        ft.addToBackStack(f.getTag());
        ft.commit();
    }

    /**
     * Replaces the current fragment. This can be used for adding the initial
     * fragment as well as replacing.
     *
     * @param cls The fragment that will replace the current fragment.
     */
    public void replaceFragment(Class<?> cls) {
        replaceFragment(cls, null);
    }

    /**
     * Replaces the current fragment. This can be used for adding the initial
     * fragment as well as replacing.
     *
     * @param cls  The fragment that will replace the current fragment.
     * @param args The arguments provided to the fragment.
     */
    public void replaceFragment(Class<?> cls, Bundle args) {
        Fragment f = Fragment.instantiate(getActivity(), cls.getName());
        f.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, f);
        ft.commit();
    }

}
