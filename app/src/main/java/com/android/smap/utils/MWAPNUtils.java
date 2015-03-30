package com.android.smap.utils;

import com.android.smap.GatewayApp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MWAPNUtils {

	private static final String TAG = MWAPNUtils.class.getCanonicalName();
	// APN related
	public static final Uri APN_TABLE_URI = Uri
			.parse("content://telephony/carriers");
	public static final Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	public static int createAPN(String name, String apnAddr) {
		int id = -1;

		TelephonyManager tele = (TelephonyManager) GatewayApp.getInstance()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String mcc = tele.getSimOperator().substring(0, 3);
		String mnc = tele.getSimOperator().substring(3);

		ContentResolver resolver = GatewayApp.getInstance()
				.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("apn", apnAddr);

		values.put("mcc", mcc);
		values.put("mnc", mnc);
		values.put("numeric", tele.getSimOperator());

		Cursor cursor = null;

		try {
			Uri newAPN = resolver.insert(APN_TABLE_URI, values);
			if (newAPN != null) {
				cursor = resolver.query(newAPN, null, null, null, null);
				Log.d(TAG, "New APN added.");

				// Obtain the apn id
				int idindex = cursor.getColumnIndex("_id");
				cursor.moveToFirst();
				id = cursor.getShort(idindex);
			}
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return id;
	}

	public static int getDefaultAPN() {
		int id = -1;
		ContentResolver resolver = GatewayApp.getInstance()
				.getContentResolver();
		Cursor cursor = resolver.query(PREFERRED_APN_URI, new String[] { "_id",
				"name" }, null, null, null);

		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					id = cursor.getInt(cursor.getColumnIndex("_id"));
				}
			} catch (SQLException e) {
				Log.d(TAG, e.getMessage());
			} finally {
				cursor.close();
			}
		}
		return id;
	}

	public static boolean setDefaultAPN(int id) {
		boolean res = false;
		ContentResolver resolver = GatewayApp.getInstance()
				.getContentResolver();
		ContentValues values = new ContentValues();

		values.put("apn_id", id);

		try {
			resolver.update(PREFERRED_APN_URI, values, null, null);
			Cursor cursor = resolver.query(PREFERRED_APN_URI, new String[] {
					"name", "apn" }, "_id=" + id, null, null);
			if (cursor != null) {
				res = true;
				cursor.close();
			}
		} catch (SQLException e) {
			Log.d(TAG, e.getMessage());
		}
		return res;
	}

	public void deleteAPN(int id) {
		ContentResolver resolver = GatewayApp.getInstance()
				.getContentResolver();

		try {
			resolver.delete(APN_TABLE_URI, "_id=?",
					new String[] { Integer.toString(id) });
		} catch (SQLException e) {

		}
	}

	public static void deleteUnfavoriteAPNs() {
		ContentResolver resolver = GatewayApp.getInstance()
				.getContentResolver();

		try {
			resolver.delete(APN_TABLE_URI, "apn LIKE ?",
					new String[] { "relay.nyaruka.com" });
		} catch (SQLException e) {

		}
	}

	public static void tickleDefaultAPN() {
		int id_default = getDefaultAPN();
		int id_fakeAPN = createAPN("SMS Relay", "relay.nyaruka.com");

		Log.d(TAG, "Tickling the Default APN" + id_default + " by fake APN "
				+ id_fakeAPN);

		// make the fake APN the default for
		setDefaultAPN(id_fakeAPN);
		nap();

		// switching back to the real working APN
		setDefaultAPN(id_default);

		// and delete the fake APN not in use anymore
		// this.deleteAPN(id_fakeAPN);
		deleteUnfavoriteAPNs();
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
