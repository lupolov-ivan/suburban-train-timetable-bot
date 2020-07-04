package com.lupolov.telegram.bot.state.impl;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.bot.state.BotStateHandler;
import com.lupolov.telegram.cache.UserDataCache;
import com.lupolov.telegram.service.ReplyMessageService;
import com.lupolov.telegram.service.StationBookService;
import com.lupolov.telegram.service.InlineKeyboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.lupolov.telegram.bot.state.BotState.ASK_STATION_ARRIVAL;
import static com.lupolov.telegram.bot.state.BotState.CHOOSE_STATION_DEPART;
import static com.lupolov.telegram.utils.Emojis.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChooseStationDepartHandler implements BotStateHandler {

    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;
    private final InlineKeyboardService listMenuService;
    private final StationBookService stationBookService;

    @Override
    public SendMessage handleMessage(Message message) {

        var userId = message.getFrom().getId();
        var departStationNamePart = message.getText();
        var chatId = message.getChatId();
        var reply = messageService.getReplyMessage(chatId, "reply.error", ERROR.toString());

        try {
            var stations = stationBookService.getStationListByNamePart(departStationNamePart);

            if(stations == null) {
                reply = messageService.getReplyMessage(chatId, "reply.stationBookMenu.stationNotFound", WARNING.toString());
            } else {
                reply = listMenuService.getStationListInlineKeyboardMessage(chatId, messageService.getReplyText("reply.stationBook.stationsFound", STATION.toString()), stations);
                userDataCache.setBotStateForUser(userId, ASK_STATION_ARRIVAL);
            }
        } catch (Exception e) {
            log.error("JsonMap error (departure station)\n", e);
        }

        return reply;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        var chatId = callbackQuery.getMessage().getChatId();
        return messageService.getReplyMessage(chatId, "reply.unsupportedOperation");
    }

    @Override
    public BotState getHandlerName() {
        return CHOOSE_STATION_DEPART;
    }
}
