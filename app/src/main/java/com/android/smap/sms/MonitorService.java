package com.android.smap.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.smap.commonsware.wakefull.WakefulIntentService;
import com.android.smap.utils.MWAPNUtils;
import com.android.smap.utils.MWConnectionUtils;

/**
 * Keep things moving and running smoothly.
 * 
 * @author Matt Witherow
 * 
 */
public class MonitorService extends WakefulIntentService {
	public static final String	TAG	= MonitorService.class.getCanonicalName();

	public MonitorService() {
		super(MonitorService.class.getName());

	}

	@Override
	protected void doWakefulWork(Intent intent) {

		Log.d(TAG, "==Check service running");
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		boolean toggleAirplane = prefs.getBoolean("toggle_airplane", false);
		// boolean toggleConnection =
		// Integer.parseInt(prefs.getString("pref_net",
		// "0")) < 2;
		// hm.

		// make sure our SMS modem is hooked up
		if (!ServiceRebooter.checkService(this.getApplicationContext())) {
			Log.d(TAG, "RelayService not started yet, waiting.");
			schedule(this.getApplicationContext());
			return;
		}

		// grab the relayer service, seeing if it started
		GatewayService relayer = GatewayService.get();

		if (relayer == null) {
			Log.d(TAG, "No RelayService started yet, awaiting.");
			return;
		}

		if (GatewayService.doReset && toggleAirplane) {
			Log.d(TAG, " RESTING PROCESS");
			try {
				Log.d(TAG, " REST - tickling airplane mode");
				MWConnectionUtils.tickleAirplaneMode(this);
				Log.d(TAG, " REST - done tickling airplane mode");
				MWAPNUtils.tickleDefaultAPN();
				Log.d(TAG, " REST - done tickling default APN mode");

				// disable the reset message
				GatewayService.doReset = false;
			}
			catch (Throwable t) {
				Log.d(TAG, "Error thrown checking network connectivity", t);
			}
		}

		try {
			// do all the work of sending messages and checking for new ones
			doCheckWork(relayer);

		}
		catch (Throwable t) {
			Log.d(TAG, "Error running check service.", t);
		}

		// reset our connect if need be
		MWConnectionUtils.restoreDefaultNetwork();

		// reschedule ourselves
		schedule(this.getApplicationContext());
	}

	protected void doCheckWork(GatewayService relayer) {

		// set our network to our default
		MWConnectionUtils.restoreDefaultNetwork();

		// if we have any local msgs with error status, resend them out.
		// TODO

		// if we have local pending messages to be sent
		// TODO
		/*
		 * try { Log.d(TAG, "  SENDING PENDING MESSAGES");
		 * relayer.sendPendingMessagesToServer(); } catch (IOException e) {
		 * 
		 * MWConnectionUtils.toggleConnection();
		 * relayer.sendPendingMessagesToServer(); //relay.resetConnection }
		 */

		// if we have succesfully sent stuff. mark the pendings as sent
		// TODO

		// Poll for new stuff from backend.
		// check outbox, use sendMessage.
		// TODO

		/*
		 * try { Log.d(TAG, "MARKING DELIVERIES"); relayer.markDeliveries(); }
		 * catch (IOException e) {
		 * 
		 * relayer.toggleConnection(); relayer.markDeliveries();
		 */

	}

	public static void schedule(Context context) {
		Log.d(TAG, "STARTING SCHEDULED TASK");
		WakefulIntentService.scheduleAlarms(
				new com.android.smap.sms.ScheduleMonitoringListener(),
				context);
	}
}
