package com.example.demojpa.vkbot.config;


import api.longpoll.bots.BotsLongPoll;
import api.longpoll.bots.exceptions.BotsLongPollException;
import api.longpoll.bots.exceptions.BotsLongPollHttpException;
import com.example.demojpa.vkbot.bot.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Configuration
public class VKBotConfig
{
    @Bean
    public Bot bot(){
        return new Bot();
    }

    @Bean
    public BotsLongPoll BotsLongPoll() {
        BotsLongPoll poll = new BotsLongPoll(bot());
        CompletableFuture.runAsync(() -> {
            try {
                poll.run();
            } catch (BotsLongPollHttpException | BotsLongPollException e) {
                log.error(e.getMessage(), e);
            }
        });
        return poll;
    }
}
