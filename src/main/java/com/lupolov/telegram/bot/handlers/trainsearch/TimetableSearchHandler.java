package com.lupolov.telegram.bot.handlers.trainsearch;

import com.lupolov.telegram.bot.BotState;
import com.lupolov.telegram.bot.handlers.UpdateHandler;
import com.lupolov.telegram.cache.UserDataCache;
import com.lupolov.telegram.model.Station;
import com.lupolov.telegram.model.TimetableEntry;
import com.lupolov.telegram.service.ReplyMessageService;
import com.lupolov.telegram.service.StationBookService;
import com.lupolov.telegram.service.StationListMenuService;
import com.lupolov.telegram.service.TimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.lupolov.telegram.bot.BotState.*;
import static com.lupolov.telegram.utils.Emojis.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimetableSearchHandler implements UpdateHandler {

    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;
    private final StationListMenuService listMenuService;
    private final StationBookService stationBookService;
    private final TimetableService timetableService;

    @Override
    public SendMessage handle(Update update) {
        var userId = -1;
        if (update.hasMessage()) {
            userId = update.getMessage().getFrom().getId();
        }
        if (update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId();
        }

        if(userDataCache.getCurrentBotStateByUserId(userId).equals(TRAINS_SEARCH)) {
            userDataCache.setBotStateForUser(userId, ASK_STATION_DEPART);
        }

        return processInputData(update);
    }

    private SendMessage processInputData(Update update) {
        var userAnswer = "";
        var userId = -1;
        long chatId = -1;

        if (update.hasMessage()) {
            userId = update.getMessage().getFrom().getId();
            userAnswer = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId();
            userAnswer = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        TrainSearchRequestData requestData = userDataCache.getUserTrainSearchData(userId);
        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.error");

        BotState botState = userDataCache.getCurrentBotStateByUserId(userId);

        if (botState == ASK_STATION_DEPART) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.trainSearch.enterStationDepart", WRITING_HAND.toString());
            userDataCache.setBotStateForUser(userId, ASK_STATIONS_NAMEPART_DEPART);
        }

        if (botState == ASK_STATIONS_NAMEPART_DEPART) {
            List<Station> stations = null;
            try {
                stations = stationBookService.getStationListByName(userAnswer);
                if(stations == null) {
                    replyToUser = messageService.getReplyMessage(chatId, "reply.stationBookMenu.stationNotFound", WARNING.toString());
                    userDataCache.setBotStateForUser(userId, ASK_STATIONS_NAMEPART_DEPART);
                } else {
                    replyToUser = listMenuService.sendInlineKeyboardMessage(chatId, messageService.getReplyText("reply.stationBook.stationsFound", STATION.toString()), stations);
                    userDataCache.setBotStateForUser(userId, STATIONS_SEARCH_DEPART_RECEIVED);
                }
            } catch (Exception e) {
                replyToUser = messageService.getReplyMessage(chatId, "reply.error", ERROR.toString());
                userDataCache.setBotStateForUser(userId, ASK_STATIONS_NAMEPART_DEPART);
                log.error("JsonMap error (departure station)\n", e);
            }
        }

        if (botState == STATIONS_SEARCH_DEPART_RECEIVED) {
            if (update.hasCallbackQuery()) {
                var departStationId = update.getCallbackQuery().getData();
                requestData.setDepartureStationId(departStationId);
                replyToUser = messageService.getReplyMessage(chatId, "reply.trainSearch.enterStationArrival", WRITING_HAND.toString());
                userDataCache.setBotStateForUser(userId, ASK_STATIONS_NAMEPART_ARRIVAL);
            } else {
                replyToUser = messageService.getReplyMessage(chatId, "reply.stationBook.missCallback", POINT_UP.toString());
            }
        }

        if (botState == ASK_STATIONS_NAMEPART_ARRIVAL) {
            List<Station> stations = null;
            try {
                stations = stationBookService.getStationListByName(userAnswer);
                if (stations == null) {
                    replyToUser = messageService.getReplyMessage(chatId, "reply.stationBookMenu.stationNotFound", WARNING.toString());
                    userDataCache.setBotStateForUser(userId, ASK_STATIONS_NAMEPART_ARRIVAL);
                } else {
                    replyToUser = listMenuService.sendInlineKeyboardMessage(chatId,messageService.getReplyText("reply.stationBook.stationsFound", STATION.toString()), stations);
                    userDataCache.setBotStateForUser(userId, TRAINS_SEARCH_STARTED);
                }
            } catch (Exception e) {
                replyToUser = messageService.getReplyMessage(chatId, "reply.error", ERROR.toString());
                userDataCache.setBotStateForUser(userId, ASK_STATIONS_NAMEPART_ARRIVAL);
                log.error("JsonMap error (arrival station)\n", e);
            }
        }

        if (botState == TRAINS_SEARCH_STARTED) {
            if (update.hasCallbackQuery()) {
                var arrivalStationId = update.getCallbackQuery().getData();
                requestData.setArrivalStationId(arrivalStationId);
                List<TimetableEntry> timetableEntries = null;

                try {
                    timetableEntries = timetableService.getTimetableByStation(requestData.getDepartureStationId(),
                            requestData.getArrivalStationId());
                    if (timetableEntries.isEmpty()) {
                        replyToUser = messageService.getReplyMessage(chatId, "reply.notFoundTimetable", MAN_SHRUGGING.toString());
                    } else {
                        replyToUser = getAndFormatSearchTimetableResult(chatId, timetableEntries);
                    }

                } catch (Exception e) {
                    replyToUser = messageService.getReplyMessage(chatId, "reply.error");
                    log.error("Parser error\n", e);
                }
                userDataCache.setBotStateForUser(userId, SHOW_MAIN_MENU);
            } else {
                replyToUser = messageService.getReplyMessage(chatId, "reply.stationBook.missCallback", POINT_UP.toString());
            }
        }

        userDataCache.saveTrainSearchData(userId, requestData);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return TRAINS_SEARCH;
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
