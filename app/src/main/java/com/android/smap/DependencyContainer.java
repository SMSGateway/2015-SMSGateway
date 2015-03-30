package com.android.smap;

import com.android.smap.di.DataManager;
import com.android.smap.di.modules.DataLayerModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class DependencyContainer {

	private Injector	mInjector;
	private DataManager	mDataManager;

	public Injector getInjector() {
		if (mInjector == null) {
			mInjector = Guice.createInjector(new DataLayerModule());
		}
		return mInjector;
	}

	public DataManager getDataManager() {
		if (mDataManager == null) {
			mDataManager = getInjector().getInstance(DataManager.class);
		}
		return mDataManager;
	}

}
