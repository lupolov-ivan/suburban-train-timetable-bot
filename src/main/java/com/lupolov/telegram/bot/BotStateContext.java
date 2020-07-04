package com.lupolov.telegram.bot;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.bot.state.BotStateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {

    private final Map<BotState, BotStateHandler> botStateToHandlers =  new HashMap<>();

    public BotStateContext(List<BotStateHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.botStateToHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputData(BotState botState, Update update) {
        var stateHandler = findMessageHandler(botState);
        if (update.hasMessage()) {
            return stateHandler.handleMessage(update.getMessage());
        }
        return stateHandler.handleCallbackQuery(update.getCallbackQuery());
    }

    private BotStateHandler findMessageHandler(BotState botState) {
        return botStateToHandlers.get(botState);
    }

}
