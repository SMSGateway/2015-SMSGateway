package com.android.smap.api.services;


import android.content.res.Resources;
import com.android.smap.R;
import com.android.smap.GatewayApp;
import com.android.smap.api.models.Dialogue;
import com.android.smap.models.SmapTextMessage;
import com.android.smap.models.TextMessage;

import org.smap.DialogueHandler;
import org.smap.SurveyConversation;

public class SmsDialogueHandler implements DialogueHandler {
    private final Dialogue dialogue;
    private final MessageSender sender;
    private final SmapTextMessage message;
    private Resources resource;

    public SmsDialogueHandler(Dialogue dialogue, MessageSender sender, SmapTextMessage message) {
        this.dialogue = dialogue;
        this.sender = sender;
        this.message = message;
        this.resource = GatewayApp.getInstance().getApplicationContext().getResources();
    }

    public SmsDialogueHandler(Dialogue dialogue, MessageSender sender) {
        this.dialogue = dialogue;
        this.sender = sender;
        this.message = null;
        this.resource = GatewayApp.getInstance().getApplicationContext().getResources();
    }

    @Override
    public String loadData() {
        return dialogue.getSerializedState();
    }

    @Override
    public void saveData(String data, String answers,int questionNumber) {
        dialogue.saveData(data, answers,questionNumber);
    }

    @Override
    public String getAnswerText() {
        return message.getMessageBody();
    }

    @Override
    public void reply(String response) {
        TextMessage replyMessage = new TextMessage(dialogue.getPhoneNumber(), response);
        dialogue.logMessage(replyMessage);
        sender.sendMessage(replyMessage);
    }

    @Override
    public void handleComplete() {
        dialogue.markAsComplete();
        TextMessage replyMessage = new TextMessage(dialogue.getPhoneNumber(),resource.getString(R.string.survey_complete));
        dialogue.logMessage(replyMessage);
        sender.sendMessage(replyMessage);
    }

    @Override
    public String getFormXml() {
        return dialogue.getFormXML();
    }

    @Override
    public void recordSurveyDetails(SurveyConversation surveyConversation) {
        dialogue.setInstanceXml(surveyConversation.getAnswers());
        dialogue.save();
    }
}
