package com.lupolov.telegram.cache;

import com.lupolov.telegram.bot.BotState;
import com.lupolov.telegram.bot.handlers.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {

    private final Map<BotState, UpdateHandler> botStateToHandlers =  new HashMap<>();

    public BotStateContext(List<UpdateHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.botStateToHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputData(BotState botState, Update update) {
        UpdateHandler messageHandler = findMessageHandler(botState);
        return messageHandler.handle(update);
    }

    private UpdateHandler findMessageHandler(BotState botState) {
        if (isTrainSearchState(botState)) {
            return botStateToHandlers.get(BotState.TRAINS_SEARCH);
        }

        if (isStationSearchState(botState)) {
            return botStateToHandlers.get(BotState.STATIONS_SEARCH);
        }

        return botStateToHandlers.get(botState);
    }

    private boolean isTrainSearchState(BotState currentState) {
        switch (currentState) {
            case TRAINS_SEARCH:
            case ASK_STATION_ARRIVAL:
            case ASK_STATION_DEPART:
            case TRAINS_SEARCH_STARTED:
            case TRAINS_SEARCH_FINISH:
            case ASK_STATIONS_NAMEPART_DEPART:
            case ASK_STATIONS_NAMEPART_ARRIVAL:
            case STATIONS_SEARCH_DEPART_RECEIVED:
            case STATIONS_SEARCH_ARRIVAL_RECEIVED:
                return true;
            default:
                return false;
        }
    }

    private boolean isStationSearchState(BotState currentState) {
        switch (currentState) {
            case ASK_STATION_NAMEPART:
            case STATION_NAMEPART_RECEIVED:
            case STATIONS_SEARCH:
                return true;
            default:
                return false;
        }
    }
}
