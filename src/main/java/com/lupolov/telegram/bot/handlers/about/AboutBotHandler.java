package com.lupolov.telegram.bot.handlers.about;

import com.lupolov.telegram.bot.BotState;
import com.lupolov.telegram.bot.handlers.UpdateHandler;
import com.lupolov.telegram.service.ReplyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.lupolov.telegram.bot.BotState.SHOW_ABOUT_BOT;
import static com.lupolov.telegram.utils.Emojis.UA_FLAG;

@Component
@RequiredArgsConstructor
public class AboutBotHandler implements UpdateHandler {

    private final ReplyMessageService messageService;

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getMessage().getChatId();
        return messageService.getReplyMessage(chatId, "reply.mainMenu.aboutMessage", UA_FLAG.toString());
    }

    @Override
    public BotState getHandlerName() {
        return SHOW_ABOUT_BOT;
    }
}
