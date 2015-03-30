package com.android.smap.controllers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by matt on 26/09/14.
 */
public class ContactImportController {

    private static final String LOG_TAG = ContactImportController.class.getName();
    public static final int PICK_CONTACTS = 1;
    private Context context;
    private Intent intent;

    class ContactQueryException extends Exception {
        private static final long serialVersionUID = 1L;

        public ContactQueryException(String message) {
            super(message);
        }
    }

    public ContactImportController(Context aContext, Intent anIntent) {
        this.context = aContext;
        this.intent = anIntent;
    }


    public String getContactName() throws ContactQueryException {
        Cursor cursor = null;
        String name = null;
        try {
            cursor = context.getContentResolver().query(intent.getData(), null,
                    null, null, null);
            if (cursor.moveToFirst())
                name = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new ContactQueryException(e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return name;
    }


    public String getContactPhone() throws ContactQueryException {
        Cursor cursor = null;
        String phone = null;
        try {

            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                    new String[] { intent.getData().getLastPathSegment() },
                    null);

            if (cursor.moveToFirst())
                phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new ContactQueryException(e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return phone;
    }

}