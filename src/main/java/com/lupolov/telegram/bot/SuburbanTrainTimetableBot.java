package com.lupolov.telegram.bot;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
public class SuburbanTrainTimetableBot extends TelegramWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Test =>>>>>>>> " + update.getMessage().getText());
        return sendMessage;
    }
}
