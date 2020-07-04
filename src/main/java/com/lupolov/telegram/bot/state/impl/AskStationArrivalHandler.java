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

import static com.lupolov.telegram.bot.state.BotState.ASK_STATION_ARRIVAL;
import static com.lupolov.telegram.bot.state.BotState.CHOOSE_STATION_ARRIVAL;
import static com.lupolov.telegram.utils.Emojis.POINT_UP;
import static com.lupolov.telegram.utils.Emojis.WRITING_HAND;

@Component
@RequiredArgsConstructor
public class AskStationArrivalHandler implements BotStateHandler {

    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;

    @Override
    public SendMessage handleMessage(Message message) {
        var chatId = message.getChatId();
        return messageService.getReplyMessage(chatId, "reply.stationBook.missCallback", POINT_UP.toString());
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        var userId = callbackQuery.getFrom().getId();
        var chatId = callbackQuery.getMessage().getChatId();
        var requestData = userDataCache.getUserTrainSearchData(userId);
        var departStationId = callbackQuery.getData();

        requestData.setDepartureStationId(departStationId);

        userDataCache.saveTrainSearchData(userId, requestData);
        userDataCache.setBotStateForUser(userId, CHOOSE_STATION_ARRIVAL);
        return  messageService.getReplyMessage(chatId, "reply.stationBook.enterStationArrivalPartName", WRITING_HAND.toString());
    }

    @Override
    public BotState getHandlerName() {
        return ASK_STATION_ARRIVAL;
    }
}
