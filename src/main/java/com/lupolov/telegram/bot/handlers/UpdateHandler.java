package com.lupolov.telegram.bot.handlers;

import com.lupolov.telegram.bot.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    SendMessage handle(Update update);

    BotState getHandlerName();
}
