package com.android.smap.api.services;

import com.android.smap.models.TextMessage;

public interface MessageSender {
    public void sendMessage(TextMessage message);
}
