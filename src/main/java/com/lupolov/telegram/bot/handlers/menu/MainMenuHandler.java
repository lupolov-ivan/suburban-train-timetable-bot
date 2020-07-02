package com.lupolov.telegram.bot.handlers.menu;

import com.lupolov.telegram.bot.BotState;
import com.lupolov.telegram.bot.handlers.UpdateHandler;
import com.lupolov.telegram.service.MainMenuService;
import com.lupolov.telegram.service.ReplyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.lupolov.telegram.bot.BotState.SHOW_MAIN_MENU;

@Component
@RequiredArgsConstructor
public class MainMenuHandler implements UpdateHandler {

    private final ReplyMessageService messageService;
    private final MainMenuService mainMenuService;

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getMessage().getChatId();
        return mainMenuService.getMainMenuMessage(chatId, messageService.getReplyText("reply.mainMenu.welcomeMessage"));
    }

    @Override
    public BotState getHandlerName() {
        return SHOW_MAIN_MENU;
    }
}
