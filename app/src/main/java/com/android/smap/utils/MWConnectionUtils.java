package com.android.smap.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.smap.GatewayApp;
import com.android.smap.PhoneStateWrapper.PhoneState;

public class MWConnectionUtils {

	private static final String TAG = MWConnectionUtils.class
			.getCanonicalName();

	/**
	 * isOnline - Check if there is a NetworkConnection
	 * 
	 * @return boolean
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Toggles our connection from WIFI and vice versa. If it does not and we
	 * are on WIFI, then we try to switch to the mobile network.
	 */
	public static void toggleConnection() {

		WifiManager wifi = (WifiManager) GatewayApp.getInstance()
				.getSystemService(Context.WIFI_SERVICE);

		// well that didn't work, let's flip our connection status, that might
		// just help.. we sleep a bit so things can connect
		boolean newWifiState = !wifi.isWifiEnabled();
		Log.d(TAG,
				"Toggling Connection: Connection test failed, flipping WIFI state to: "
						+ newWifiState);
		wifi.setWifiEnabled(newWifiState);

		nap();
	}

	/**
	 * Checks whether we have a mobile network connected. This hopefully catches
	 * the case where the phone drops its connection for some reason.
	 * 
	 * @param context
	 * @return
	 */
	public boolean isRadioOn() {
		Context context = GatewayApp.getInstance();

		boolean isOn = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networks = cm.getAllNetworkInfo();
		for (int i = 0; i < networks.length; i++) {
			if (networks[i].getType() == ConnectivityManager.TYPE_MOBILE
					&& networks[i].isConnectedOrConnecting()) {
				isOn = true;
			}
		}

		// if our radio is off, output some debugging
		Log.d(TAG, "_RADIO STATUS");
		for (int i = 0; i < networks.length; i++) {
			Log.d(TAG, "  " + networks[i].getTypeName() + "  connection? "
					+ networks[i].isConnectedOrConnecting());
		}

		// check our telephony manager
		TelephonyManager tele = (TelephonyManager) GatewayApp.getInstance()
				.getSystemService(Context.TELEPHONY_SERVICE);
		Log.d(TAG, "  call state: " + tele.getCallState());
		Log.d(TAG, "  data state: " + tele.getDataState());
		Log.d(TAG, "  network type: " + tele.getNetworkType());

		PhoneState ps = GatewayApp.getInstance().getPhoneStateWrapper()
				.getPhoneState();
		Log.d(TAG, "  phone state: " + ps.state);
		Log.d(TAG, "  signal strength: " + ps.strength);

		return isOn;
	}

	public static void tickleAirplaneMode(IntentService requester) {
		
		Context context = GatewayApp.getInstance();
		Settings.System.putInt(context.getContentResolver(),
				Settings.Global.AIRPLANE_MODE_ON, 1);

		// reload our settings to take effect
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", true);
		requester.sendBroadcast(intent);

		nap();

		// then toggle back
		Settings.System.putInt(context.getContentResolver(),
				Settings.Global.AIRPLANE_MODE_ON, 0);

		// reload our settings to take effect
		intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", false);
		requester.sendBroadcast(intent);

		nap();
	}

	/**
	 * Restores our WIFI/DATA state to whatever is in our preference file. No-op
	 * if our current state is the same as our preferred state.
	 */
	public static void restoreDefaultNetwork() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(GatewayApp.getInstance());
		boolean isWifiPreferred = Integer.parseInt(prefs.getString("pref_net",
				"0")) % 2 == 0;
		WifiManager wifi = (WifiManager) GatewayApp.getInstance()
				.getSystemService(Context.WIFI_SERVICE);

		if (wifi.isWifiEnabled() != isWifiPreferred) {
			// toggle back to the preferred network
			wifi.setWifiEnabled(isWifiPreferred);

			nap();
		}
	}

	/**
	 * Take a nap. Sleep 30 seconds to give the network hardware a chance to
	 * connect or similar.
	 */
	private static void nap() {

		try {
			Thread.sleep(30000);
		} catch (Throwable tt) {
		}

	}
}
