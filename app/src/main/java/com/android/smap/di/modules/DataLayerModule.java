package com.android.smap.di.modules;

import com.android.smap.api.models.SqliteDataManager;
import com.android.smap.api.services.MessageResponder;
import com.android.smap.di.DataManager;
import com.google.inject.AbstractModule;

/**
 * All dependency injection bindings for the app data layer.
 * 
 * @author Matt Witherow
 * 
 */
public class DataLayerModule extends AbstractModule {

	/**
	 * This tells Guice that whenever it sees a dependency on a X, it should
	 * satisfy the dependency using Y.
	 * 
	 * bind(X).to(Y);
	 */
	@Override
	protected void configure() {
		bind(DataManager.class).to(SqliteDataManager.class);
	}

}
