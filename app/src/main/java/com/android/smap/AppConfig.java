package com.android.smap;

import com.android.smap.api.ApiConstants;

public class AppConfig {

	public AppConfig(GatewayApp gatewayApp) {
		// convenience wrapper for android's application settings.
	}

	public int getTimeoutInMillis() {
		// TODO timeout for Volley requests
		return 0;
	}

	public String getRequestEndpoint() {
		return ApiConstants.DEV_URL;
	}

}
