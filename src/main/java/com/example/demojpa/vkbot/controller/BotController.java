package com.example.demojpa.vkbot.controller;


import api.longpoll.bots.exceptions.BotsLongPollException;
import api.longpoll.bots.exceptions.BotsLongPollHttpException;
import com.example.demojpa.vkbot.bot.Bot;
import com.example.demojpa.vkbot.request.PostNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    private Bot bot;


    @PostMapping("/{vkid}")
    public ResponseEntity<?> test(@RequestBody PostNotificationRequest postNotificationRequest, @PathVariable Integer vkid) throws BotsLongPollException, BotsLongPollHttpException
    {
        bot.postNotificationVKId(postNotificationRequest,vkid);
        return ResponseEntity.ok("Всё ок");
    }
}
