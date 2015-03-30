package com.android.smap.sms;

import java.util.Calendar;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BootChecker just makes sure our service is running after the phone is booted
 */
public class ServiceRebooter extends BroadcastReceiver {

	public static final String	TAG	= ServiceRebooter.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent) {
		checkService(context);
	}

	public static boolean checkService(Context context) {
		// If we haven't yet initialized our modem, do so
		if (!isServiceRunning(context)) {
			startService(context);
			return false;
		} else {
			return true;
		}
	}

	private static void startService(Context context) {
		Log.d(TAG, "=================");
		Log.d(TAG, "STARTING SERVICES");
		Log.d(TAG, "=================");

		Intent serviceIntent = new Intent(context, GatewayService.class);
		context.startService(serviceIntent);
		scheduleRebootService(context);
	}

	private static boolean isServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.android.smap.sms.GatewayService"
					.equals(service.service.getClassName())) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Trigger a reboot tomorrow at midnight
	 */
	private static void scheduleRebootService(Context context) {
		Intent serviceIntent = new Intent(context, ServiceRebooter.class);
		PendingIntent sender = PendingIntent.getService(context, 0,
				serviceIntent, 0);

		// reboot each day to keep things happy
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// advance to tomorrow
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 24);

		// floor our date
		int day = now.get(Calendar.DAY_OF_MONTH);
		int mo = now.get(Calendar.MONTH);
		int yr = now.get(Calendar.YEAR);
		now.set(yr, mo, day, 0, 0);

		// set the alarm
		am.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), sender);
	}
}
