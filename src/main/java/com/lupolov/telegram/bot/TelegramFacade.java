package com.lupolov.telegram.bot;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.cache.UserDataCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.lupolov.telegram.bot.state.BotState.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramFacade {

    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            log.info("New callbackQuery from User: {} with data: {}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    update.getCallbackQuery().getData());

        }

        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            log.info("New message from User: {}, chatId {}, with test: {}",
                    update.getMessage().getFrom().getUserName(),
                    update.getMessage().getChatId(),
                    update.getMessage().getText());
        }
        replyMessage = handleInputData(update);

        return replyMessage;
    }

    private SendMessage handleInputData(Update update) {
        var message = update.getMessage();
        var inputData = "";
        var userId = -1;
        if (update.hasMessage()) {
            inputData = message.getText();
            userId = message.getFrom().getId();
        }
        if (update.hasCallbackQuery()) {
            inputData = update.getCallbackQuery().getData();
            userId = update.getCallbackQuery().getFrom().getId();
        }
        BotState botState;
        SendMessage replyMessage;

        switch (inputData) {
            case "/start":
                botState = SHOW_MAIN_MENU;
                break;
            case "Пошук розкладу":
                botState = ASK_STATION_DEPART;
                break;
            case "Інформація про бота":
                botState = SHOW_ABOUT_BOT;
                break;
            default:
                botState = userDataCache.getCurrentBotStateByUserId(userId);
                break;
        }

        userDataCache.setBotStateForUser(userId, botState);

        replyMessage = botStateContext.processInputData(botState, update);

        return replyMessage;
    }
}
