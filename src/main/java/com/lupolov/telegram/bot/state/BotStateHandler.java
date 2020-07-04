package com.lupolov.telegram.bot.state;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotStateHandler {

    SendMessage handleMessage(Message message);
    SendMessage handleCallbackQuery(CallbackQuery callbackQuery);
    BotState getHandlerName();
}
