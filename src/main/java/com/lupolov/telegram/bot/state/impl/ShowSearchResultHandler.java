package com.lupolov.telegram.bot.state.impl;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.bot.state.BotStateHandler;
import com.lupolov.telegram.cache.UserDataCache;
import com.lupolov.telegram.model.TimetableEntry;
import com.lupolov.telegram.service.ReplyMessageService;
import com.lupolov.telegram.service.TimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.lupolov.telegram.bot.state.BotState.SHOW_MAIN_MENU;
import static com.lupolov.telegram.bot.state.BotState.SHOW_SEARCH_RESULT;
import static com.lupolov.telegram.utils.Emojis.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowSearchResultHandler implements BotStateHandler {

    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;
    private final TimetableService timetableService;

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
        var arrivalStationId = callbackQuery.getData();
        var reply = messageService.getReplyMessage(chatId, "reply.error");

        requestData.setArrivalStationId(arrivalStationId);

        try {
            var timetableEntries = timetableService.getTimetableByStation(requestData.getDepartureStationId(),
                                                                          requestData.getArrivalStationId());
            if (timetableEntries.isEmpty()) {
                reply =  messageService.getReplyMessage(chatId, "reply.notFoundTimetable", NEUTRAL_FACE.toString());
            } else {
                reply =  getAndFormatSearchTimetableResult(chatId, timetableEntries);
            }
            userDataCache.setBotStateForUser(userId, SHOW_MAIN_MENU);

        } catch (Exception e) {
            log.error("Parser error\n", e);
        }
        return reply;
    }

    @Override
    public BotState getHandlerName() {
        return SHOW_SEARCH_RESULT;
    }

    private SendMessage getAndFormatSearchTimetableResult(long chatId, List<TimetableEntry> entries) {

        var builder = new StringBuilder();

        builder.append(UA_FLAG).append(" Результати вашого запиту:\n\n");

        entries.forEach(entry -> {
            builder.append(STATION).append(" Потяг №").append(entry.getTrainNum()).append("\n");
            builder.append(SCHEDULE).append(" Обіг: ").append(entry.getSchedule()).append("\n");
            builder.append(ROUTE).append(" Маршрут прямування: ").append(entry.getRoute()).append("\n");
            builder.append(DEPART).append(" Відправлення: ").append(entry.getDeparture()).append("\n");
            builder.append(ARRIVAL).append(" Прибуття: ").append(entry.getArrival()).append("\n");
            builder.append(TRAVEL_TIME).append(" В дорозі: ").append(entry.getTravelTime()).append("\n");
            builder.append("\n");
        });

        var maybeEntry = entries.stream().findFirst();
        if (maybeEntry.isPresent()) {
            var entry = maybeEntry.get();
            builder.append(DISTANCE).append(" Відстань: ").append(entry.getDistance()).append(" км").append("\n");
            builder.append(CHECK_MARK).append(" Розклад дісний з ").append(entry.getFromRelevance()).append(" по ").append(entry.getToRelevance()).append("\n");
        }

        return new SendMessage()
                .setChatId(chatId)
                .setText(builder.toString());
    }
}
