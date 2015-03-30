package com.android.smap.sms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.android.smap.GatewayApp;
import com.android.smap.commonsware.wakefull.WakefulIntentService;

public class ScheduleMonitoringListener implements
		WakefulIntentService.AlarmSheduleListener {

	// 0 means we should look it up in the preferences
	private long	mInterval	= 0;

	public ScheduleMonitoringListener(long interval) {
		mInterval = interval;
	}

	public ScheduleMonitoringListener() {}

	public long getMaxAge() {
		return AlarmManager.INTERVAL_HOUR;
	}

	public void scheduleAlarms(AlarmManager mgr, PendingIntent pi, Context ctxt) {

		if (mInterval == 0) {
			mInterval = GatewayApp.getPreferenceWrapper().getUpdateInterval();
		}
		mgr.set(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + mInterval, pi);

	}

	public void sendWakefulWork(Context ctxt) {

		WakefulIntentService.sendWakefulWork(ctxt, MonitorService.class);
	}
}
