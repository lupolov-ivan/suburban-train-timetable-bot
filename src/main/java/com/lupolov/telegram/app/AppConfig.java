package com.lupolov.telegram.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lupolov.telegram.bot.SuburbanTrainTimetableBot;
import com.lupolov.telegram.bot.BotConfig;
import com.lupolov.telegram.bot.TelegramFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final BotConfig botConfig;

    @Bean
    public SuburbanTrainTimetableBot suburbanTrainTimetableBot(TelegramFacade telegramFacade) {
        SuburbanTrainTimetableBot bot = new SuburbanTrainTimetableBot(telegramFacade);

        bot.setBotUsername(botConfig.getUsername());
        bot.setBotToken(botConfig.getToken());
        bot.setBotPath(botConfig.getWebHookPath());

        return bot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
