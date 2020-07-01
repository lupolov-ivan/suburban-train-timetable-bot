package com.lupolov.telegram.contoller;

import com.lupolov.telegram.bot.SuburbanTrainTimetableBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebHookController {

    private final SuburbanTrainTimetableBot bot;

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        //log.info("New request: '{}'", update.getMessage().getText());
        return bot.onWebhookUpdateReceived(update);
    }
}
