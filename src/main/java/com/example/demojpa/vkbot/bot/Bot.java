package com.example.demojpa.vkbot.bot;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.BotsLongPollException;
import api.longpoll.bots.exceptions.BotsLongPollHttpException;
import api.longpoll.bots.methods.messages.MessagesSend;
import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.objects.basic.Message;
import com.example.demojpa.vkbot.Command;
import com.example.demojpa.vkbot.request.PersonRequest;
import com.example.demojpa.vkbot.request.PurposeRequest;
import com.example.demojpa.vkbot.response.FindPersonResponse;
import com.example.demojpa.vkbot.response.FindPurposeResponse;
import com.example.demojpa.vkbot.service.PersonService;
import com.example.demojpa.vkbot.service.PurposeService;
import com.example.demojpa.vkbot.utils.ButtonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

import static api.longpoll.bots.model.objects.additional.Button.ButtonColor.*;

@Slf4j
public class Bot extends LongPollBot {

    @Value("${dempjpa.bot.getAccessToken.token}")
    private String token;

    @Value("${dempjpa.bot.getGroupId.id}")
    private Integer  id;



    @Autowired
    private PersonService personService;

    @Autowired
    private PurposeService purposeService;

    private Command lastCommand;



    public void postPurposeVKId(PurposeRequest purposeRequest, Integer vkid) throws BotsLongPollException, BotsLongPollHttpException {
        new MessagesSend(this)
                .setPeerId(vkid)
                .setMessage(purposeRequest.getPurpose())
                .execute();

    }



    @Override
    public void onMessageNew(MessageNewEvent messageNewEvent) {
        Message message = messageNewEvent.getMessage();
        Command command = getCommand(message);



        try {
            FindPersonResponse findPersonResponse = personService.getPerson(message.getPeerId());


            if (findPersonResponse != null) {

                if (lastCommand==Command.ADD)
                {



                    try
                    {

                        String[] stringList=message.getText().split(",");
                        String[] stringtime=stringList[1].split(":");
                        purposeService.createPurpose(new PurposeRequest(stringList[0],
                                        LocalDateTime.now()
                                                .withHour(Integer.parseInt(stringtime[0]))
                                                .withMinute(Integer.parseInt(stringtime[1]))),
                                                    message.getPeerId());

                        lastCommand = null;
                    }
                    catch (Exception e)
                    {
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Введены неккоректные данные")
                                .execute();
                    }


                }

                switch (command) {

                    case ADD -> {
                        lastCommand = command;
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Введите название цели и время оповещения в формате: Цель,hh:min")
                                .execute();



                    }


                    case FIND -> {
                        List<FindPurposeResponse.Purpose> findPurposeResponse = purposeService.getPurpose(message.getPeerId());
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage(findPurposeResponse.toString())
                                .execute();

                    }
                    case JOKE -> {
                        String string="https://randomfox.ca/";
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage(string)
                                .execute();

                    }


                    case UNKNOWN ->
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Держи:")
                                .setKeyboard(ButtonBuilder.create()
                                        .addButton("узнать", NEGATIVE)
                                        .addButton("добавить", PRIMARY )
                                        .addButton("лисы", SECONDARY )
                                        .Build())
                                .execute();

                }
            } else {



                switch (command) {
                    case SUBSCRIBE -> {
                        personService.createPerson(new PersonRequest(message.getPeerId()));
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Держи:")
                                .setKeyboard(ButtonBuilder.create()
                                        .addButton("узнать", NEGATIVE)
                                        .addButton("добавить", PRIMARY )
                                        .addButton("лисы", SECONDARY )
                                        .Build())
                                .execute();
                    }


                    case UNKNOWN -> new MessagesSend(this)
                            .setPeerId(message.getPeerId())
                            .setMessage("Лови")
                            .setKeyboard(ButtonBuilder.create()
                                    .addButton("подписаться",POSITIVE)
                                    .addButton("лисы", SECONDARY )
                                    .Build())
                            .execute();

                    //call общий метод

                }


            }
            // успешная идентификация
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            command = Command.SERVER_ERROR;
            try {
                new MessagesSend(this)
                        .setPeerId(message.getPeerId())
                        .setMessage(command.getCommand())
                        .execute();

            } catch (BotsLongPollHttpException | BotsLongPollException ex) {
                e.printStackTrace();

                // ошибка сервера
            }


        }
    }




    private Command getCommand(Message message){
        return Command.of(message.getText());
    }

    @Override
    public String getAccessToken() {
        return token;
    }

    @Override
    public int getGroupId() {
        return id;
    }
}

