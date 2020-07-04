package com.lupolov.telegram.bot.state.impl;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.bot.state.BotStateHandler;
import com.lupolov.telegram.service.MainMenuService;
import com.lupolov.telegram.service.ReplyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.lupolov.telegram.bot.state.BotState.SHOW_MAIN_MENU;

@Component
@RequiredArgsConstructor
public class ShowMainMenuHandler implements BotStateHandler {

    private final ReplyMessageService messageService;
    private final MainMenuService mainMenuService;

    @Override
    public SendMessage handleMessage(Message message) {
        var chatId = message.getChatId();
        return mainMenuService.getMainMenuMessage(chatId, messageService.getReplyText("reply.mainMenu.welcomeMessage"));
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        var chatId = callbackQuery.getMessage().getChatId();
        return messageService.getReplyMessage(chatId, "reply.unsupportedOperation");
    }

    @Override
    public BotState getHandlerName() {
        return SHOW_MAIN_MENU;
    }
}
