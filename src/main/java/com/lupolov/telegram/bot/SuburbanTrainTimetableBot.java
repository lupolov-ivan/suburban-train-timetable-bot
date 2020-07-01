package com.lupolov.telegram.bot;

import com.lupolov.telegram.model.TimetableEntry;
import com.lupolov.telegram.uz.parser.Parser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.lupolov.telegram.utils.Emojis.*;

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
