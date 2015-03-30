package com.android.smap.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.android.smap.R;
import com.android.smap.utils.MWCommsUtils;

/**
 * Controller to send email. Future idea is to make this completely automated.
 * (requires like 3 external jars) Currently, app will ask user for email
 * client.
 * 
 * @author Matt Witherow
 * 
 */
public class EmailController implements Controller {

	private List<String>	mAddress;
	private String			mSubject;
	private String			mMessage;
	Context					mContext;

	public EmailController(Context context, String addr, String subject,
			String msg) {

		this.mContext = context;
		this.mAddress = new ArrayList<String>();
		mAddress.add(addr);
		this.mSubject = subject;
		this.mMessage = msg;
	}

	@Override
	public ControllerListener getControllerListener() {

		return null;
	}

	@Override
	public ControllerErrorListener getControllerErrorListener() {
		return null;
	}

	@Override
	public void start() {

		Intent intent = MWCommsUtils.createEmailIntent(mAddress, null,
                mSubject, mMessage);

		Intent mailer = Intent.createChooser(intent,
				mContext.getString(R.string.contact_choose_email_client));
		mailer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(mailer);
	}

	@Override
	public void cancel() {
		// too bad.

	}

}
