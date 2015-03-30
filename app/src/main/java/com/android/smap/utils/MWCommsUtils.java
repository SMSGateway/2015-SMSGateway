package com.android.smap.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MWCommsUtils {

	/**
	 * Easily create email intent
	 * 
	 * @param recipientsList
	 * @param ccList
	 * @param subject
	 * @param htmlBody
	 * @return email intent chooser
	 */
	public static Intent createEmailIntent(List<String> recipientsList,
			List<String> ccList, String subject, CharSequence htmlBody) {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		// emailIntent.setData(Uri.parse("mailto:"));

		if (recipientsList != null) {
			for (String email : recipientsList) {
				emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
			}
		}
		if (ccList != null) {
			for (String email : ccList) {
				emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
			}
		}
		if (subject != null)
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		if (htmlBody != null)
			emailIntent.putExtra(Intent.EXTRA_TEXT, htmlBody);

		return emailIntent;
	}

    public static void call(Context cxt, String number){
        String phoneUri = "tel:";
        phoneUri = phoneUri.concat(number);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneUri));
        cxt.startActivity(intent);
    }

}
