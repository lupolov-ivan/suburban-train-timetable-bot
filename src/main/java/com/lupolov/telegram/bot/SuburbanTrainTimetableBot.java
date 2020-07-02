package com.lupolov.telegram.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class SuburbanTrainTimetableBot extends TelegramWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;

    private final TelegramFacade telegramFacade;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }
}
