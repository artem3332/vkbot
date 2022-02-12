package com.example.demojpa.vkbot.utils;


import api.longpoll.bots.model.objects.additional.Button;
import api.longpoll.bots.model.objects.additional.Keyboard;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ButtonBuilder {
    public List<Button> buttonList;
    public Keyboard keyboard;

    public static ButtonBuilder create(){
        return new ButtonBuilder();
    }


    public ButtonBuilder() {
        this.keyboard = new Keyboard().setOneTime(true);
        this.buttonList = new ArrayList<>();
    }

    public ButtonBuilder addButton(String name, Button.ButtonColor color) {
        JsonObject jsonObject = new JsonObject();
        buttonList.add(new Button(color, new Button.TextAction(name, jsonObject.toString())));
        return this;
    }

    public Keyboard Build() {
        keyboard.setButtons(List.of(buttonList));
        return keyboard;
    }
}
