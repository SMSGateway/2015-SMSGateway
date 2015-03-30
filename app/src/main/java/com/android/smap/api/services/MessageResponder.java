package com.android.smap.api.services;

import android.content.res.Resources;
import android.util.Log;

import com.android.smap.R;
import com.android.smap.api.models.Contact;
import com.android.smap.api.models.Dialogue;
import com.android.smap.di.DataManager;
import com.android.smap.models.SmapTextMessage;
import com.android.smap.samuel.Samuel;
import com.google.inject.Inject;

import org.smap.DialogueHandler;
import org.smap.surveyConverser.SurveyConverser;

public class MessageResponder {

    private static final String TAG = MessageResponder.class.getCanonicalName();
    private final DataManager dataManager;
    private Resources resource;

    @Inject
    public MessageResponder(DataManager dataManager) {
        this.dataManager = dataManager;

    }

    public void setResource(Resources res){
        this.resource = res;
    }

    public void handleMessage(MessageSender sender, SmapTextMessage message) {
        Contact contact = dataManager.findContactByPhoneNumber(message.getPhoneNumber());

        if (contact == null) {
            Log.e(TAG, "Contact Not A Smap Contact : "+message.getPhoneNumber());
            return;
        }

        if(!contact.isActive()){
            notifySenderNotActive(sender, message);
            return;
        }

        if(message.isCommandSMS()){
            handleCommandSMS(contact, message);
            return;
        }

        //get Dialogue is actually active for
        Dialogue dialogue = contact.getActiveDialogue();

        DialogueHandler handler = new SmsDialogueHandler(dialogue, sender, message);
        SurveyConverser.handleDialog(handler);
    }

    private void handleCommandSMS(Contact contact, SmapTextMessage message) {
        String log = "SMAP COMMAND REQUESTED FROM "+message.getPhoneNumber();
        Log.i(Samuel.class.getName(), log);
    }

    public void notifySenderNotActive(MessageSender sender, SmapTextMessage message){
        SmapTextMessage reply = new SmapTextMessage(message.getPhoneNumber(),
                resource.getString(R.string.response_contact_not_active));
        sender.sendMessage(reply);
    }
}
