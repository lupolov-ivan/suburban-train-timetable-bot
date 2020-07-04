package com.lupolov.telegram.bot.state.impl;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.bot.state.BotStateHandler;
import com.lupolov.telegram.service.ReplyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.lupolov.telegram.bot.state.BotState.SHOW_ABOUT_BOT;
import static com.lupolov.telegram.utils.Emojis.UA_FLAG;


@Component
@RequiredArgsConstructor
public class ShowAboutBotHandler implements BotStateHandler {

    private final ReplyMessageService messageService;

    @Override
    public SendMessage handleMessage(Message message) {
        var chatId = message.getChatId();
        return messageService.getReplyMessage(chatId, "reply.mainMenu.aboutMessage", UA_FLAG.toString());
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        var chatId = callbackQuery.getMessage().getChatId();
        return messageService.getReplyMessage(chatId, "reply.unsupportedOperation");
    }

    @Override
    public BotState getHandlerName() {
        return SHOW_ABOUT_BOT;
    }
}
