package com.android.smap.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.android.smap.R;

public class FragmentWrapperActivity extends BaseActivity {

	public static final String	FRAGMENT_CLASS	= "fragmentClassName";
	public static final String	FRAGMENT_ARGS	= "fragmentArgs";

	private String				mFragmentClassName;
	private Bundle				mFragmentArgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(android.R.drawable.ic_menu_preferences);// whatevs

		Bundle args = getIntent().getExtras();
		String fragmentClassName = args.getString(FRAGMENT_CLASS);
		mFragmentArgs = args.getBundle(FRAGMENT_ARGS);
		try {
			mFragmentClassName = fragmentClassName;
			Class.forName(mFragmentClassName);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			this.finish();
		}
		setContentView(R.layout.activity_main);
		setFragmentInContainer();
	}

	private void setFragmentInContainer() {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = Fragment.instantiate(this, mFragmentClassName);
		fragment.setArguments(mFragmentArgs);
		ft.replace(R.id.container, fragment);
		ft.commit();
	}

}
