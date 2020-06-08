package com.lupolov.telegram.app;

import com.lupolov.telegram.bot.SuburbanTrainTimetableBot;
import com.lupolov.telegram.bot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final BotConfig botConfig;

    @Bean
    public SuburbanTrainTimetableBot suburbanTrainTimetableBot() {
        SuburbanTrainTimetableBot bot = new SuburbanTrainTimetableBot();

        bot.setBotUsername(botConfig.getUsername());
        bot.setBotToken(botConfig.getToken());
        bot.setBotPath(botConfig.getWebHookPath());

        return bot;
    }

}
