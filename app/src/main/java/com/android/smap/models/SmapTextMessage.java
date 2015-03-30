package com.android.smap.models;

public class SmapTextMessage extends TextMessage{

    public static String messageBodyRegex="^#!{1,2}";
    public static String smapMessageRegex="^#!{1,2}.*";
    public static String commandMessageRegex="^#!!.*";

    public SmapTextMessage(String number, String message){
        super(number, message);

        // smap messages are always incoming
        this.direction = TextMessage.INCOMING;
        this.status = TextMessage.RECEIVED;
    }

    public boolean isSmapMessage() {
        return this.text.matches(smapMessageRegex);
    }

    public boolean isCommandSMS() {
        return this.text.matches(commandMessageRegex);
    }

    public String getMessageBody(){
        return this.text.replaceFirst(messageBodyRegex,"").trim();
    }
}
