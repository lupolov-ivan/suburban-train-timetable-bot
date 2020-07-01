package com.lupolov.telegram.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    private String username;
    private String token;
    private String webHookPath;
}
