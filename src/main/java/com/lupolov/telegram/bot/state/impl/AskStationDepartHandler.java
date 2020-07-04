package com.lupolov.telegram.bot.state.impl;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.bot.state.BotStateHandler;
import com.lupolov.telegram.cache.UserDataCache;
import com.lupolov.telegram.service.ReplyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.lupolov.telegram.bot.state.BotState.ASK_STATION_DEPART;
import static com.lupolov.telegram.bot.state.BotState.CHOOSE_STATION_DEPART;
import static com.lupolov.telegram.utils.Emojis.WRITING_HAND;

@Component
@RequiredArgsConstructor
public class AskStationDepartHandler implements BotStateHandler {

    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;

    @Override
    public SendMessage handleMessage(Message message) {
        var userId = message.getFrom().getId();
        var chatId = message.getChatId();

        userDataCache.setBotStateForUser(userId, CHOOSE_STATION_DEPART);
        return messageService.getReplyMessage(chatId, "reply.stationBook.enterStationDepartPartName", WRITING_HAND.toString());
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        var chatId = callbackQuery.getMessage().getChatId();
        return messageService.getReplyMessage(chatId, "reply.unsupportedOperation");
    }

    @Override
    public BotState getHandlerName() {
        return ASK_STATION_DEPART;
    }
}
