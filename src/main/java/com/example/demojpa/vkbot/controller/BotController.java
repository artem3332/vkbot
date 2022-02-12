package com.example.demojpa.vkbot.controller;


import api.longpoll.bots.exceptions.BotsLongPollException;
import api.longpoll.bots.exceptions.BotsLongPollHttpException;
import com.example.demojpa.vkbot.bot.Bot;
import com.example.demojpa.vkbot.request.PurposeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BotController {

    @Autowired
    Bot bot;


    @PostMapping("/bot/test/{vkid}")
    public ResponseEntity<?> test(@RequestBody PurposeRequest purposeRequest, @PathVariable Integer vkid) throws BotsLongPollException, BotsLongPollHttpException
    {
        bot.postPurposeVKId(purposeRequest,vkid);
        return ResponseEntity.ok("Всё ок");
    }
}
