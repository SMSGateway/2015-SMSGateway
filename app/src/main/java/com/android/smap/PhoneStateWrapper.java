package com.android.smap;

import android.app.Application;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 
 * @author Matt Witherow
 * 
 */
public class PhoneStateWrapper {

	private static final String	TAG	= PhoneStateWrapper.class
											.getCanonicalName();
	private PhoneState			mPhoneState;

	public PhoneStateWrapper(Application app) {

		mPhoneState = new PhoneState();

		TelephonyManager telephonyManager = (TelephonyManager) app
				.getSystemService(android.content.Context.TELEPHONY_SERVICE);
		telephonyManager.listen(mPhoneState,
				PhoneStateListener.LISTEN_CALL_STATE
						| PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	public PhoneState getPhoneState() {
		return mPhoneState;
	}

	public static class PhoneState extends PhoneStateListener {

		public int	state		= 0;
		public int	strength	= 0;

		public void onServiceStateChanged(ServiceState serviceState) {
			Log.d(TAG, "Service State Changed : " + serviceState.getState()
					+ " -- " + serviceState.getOperatorAlphaLong());
			state = serviceState.getState();
		}

		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			strength = signalStrength.getGsmSignalStrength();
		}
	}
}
