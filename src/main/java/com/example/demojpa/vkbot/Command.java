package com.example.demojpa.vkbot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Command {
    SUBSCRIBE("подписаться"),
    UNSUBSCRIBE("отписаться"),
    ADD("добавить"),
    FIND("узнать"),
    JOKE("лисы"),


    SERVER_ERROR("ошибка сервера"),
    UNKNOWN("")
    ;

    private final String command;

    public static Command of(String txt){
        return Arrays.stream(values())
                .filter(e-> e.getCommand().equalsIgnoreCase(txt)).findFirst()
                .orElse(UNKNOWN);
    }
}