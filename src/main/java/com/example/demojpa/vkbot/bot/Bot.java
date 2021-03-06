package com.example.demojpa.vkbot.bot;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.BotsLongPollException;
import api.longpoll.bots.exceptions.BotsLongPollHttpException;
import api.longpoll.bots.methods.messages.MessagesSend;
import api.longpoll.bots.model.events.messages.MessageNewEvent;
import api.longpoll.bots.model.objects.basic.Message;
import com.example.demojpa.vkbot.request.PostNotificationRequest;
import com.example.demojpa.vkbot.request.PostPersonRequest;
import com.example.demojpa.vkbot.response.FindNotificationResponse;
import com.example.demojpa.vkbot.response.FindPersonResponse;
import com.example.demojpa.vkbot.service.NotificationService;
import com.example.demojpa.vkbot.service.PersonService;
import com.example.demojpa.vkbot.utils.ButtonBuilder;
import com.example.demojpa.vkbot.utils.Command;
import com.example.demojpa.vkbot.utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private NotificationService notificationService;

    private Command lastCommand;

    private Command lastCommand2;

    private String notification;

    @Cacheable(value = "purposes")
    public PostNotificationRequest postNotificationVKId(PostNotificationRequest postNotificationRequest, Integer vkid) throws BotsLongPollException, BotsLongPollHttpException {
        new MessagesSend(this)
                .setPeerId(vkid)
                .setMessage(postNotificationRequest.getNotification()+"\n"+"Вы подтверждаете получение задачи?")
                .setKeyboard(ButtonBuilder.create()
                        .addButton("подтверждаю", POSITIVE)
                        .addButton("неподтверждаю", NEGATIVE)
                        .Build())
                .execute();


        this.notification =postNotificationRequest.getNotification();

        return  postNotificationRequest;

    }

    @Override
    public void onMessageNew(MessageNewEvent messageNewEvent) {
        Message message = messageNewEvent.getMessage();
        Command command = getCommand(message);

        try {
            FindPersonResponse findPersonResponse = personService.getPerson(message.getPeerId());


            if (findPersonResponse != null) {

                if(lastCommand2==Command.DELETE)
                {
                    notificationService.deleteNotification(message.getText(),Long.valueOf(message.getPeerId()));
                    new MessagesSend(this)
                            .setPeerId(message.getPeerId())
                            .setMessage("Держи:")
                            .setKeyboard(ButtonBuilder.create()
                                    .addButton("отписаться", POSITIVE)
                                    .addButton("узнать", NEGATIVE)
                                    .addButton("добавить", PRIMARY )
                                    .addButton("удалить задачу",NEGATIVE)
                                    .addButton("лисы", SECONDARY )
                                    .Build())
                            .execute();

                    lastCommand2=null;



                } else if (lastCommand==Command.ADD)
                {

                    try
                    {
                        List<String> stringList= Arrays.stream(message.getText().split(",")).toList();
                        List<String> list= stringList.stream().map(String::trim).toList();
                        notificationService.createNotification(new PostNotificationRequest(list.get(0),
                                        Status.PROCESS,
                                        LocalDateTime.now()
                                                .withDayOfMonth(Integer.parseInt(list.get(1)))
                                                .withHour(Integer.parseInt(list.get(2)))
                                                .withMinute(Integer.parseInt(list.get(3)))),
                                                 message.getPeerId());

                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Держи:")
                                .setKeyboard(ButtonBuilder.create()
                                        .addButton("отписаться", POSITIVE)
                                        .addButton("узнать", NEGATIVE)
                                        .addButton("добавить", PRIMARY )
                                        .addButton("удалить задачу",NEGATIVE)
                                        .addButton("лисы", SECONDARY )
                                        .Build())
                                .execute();

                        lastCommand = null;
                    }
                    catch (Exception e)
                    {
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Введены неккоректные данные")
                                .setKeyboard(ButtonBuilder.create()
                                        .addButton("отписаться", POSITIVE)
                                        .addButton("узнать", NEGATIVE)
                                        .addButton("добавить", PRIMARY )
                                        .addButton("удалить задачу",NEGATIVE)
                                        .addButton("лисы", SECONDARY )
                                        .Build())
                                .execute();

                        lastCommand = null;
                    }

                }
                else {

                    switch (command) {

                        case YES -> {

                            notificationService.putStatusNotification(notification);

                            new MessagesSend(this)
                                    .setPeerId(message.getPeerId())
                                    .setMessage("Держи")
                                    .setKeyboard(ButtonBuilder.create()
                                            .addButton("отписаться", POSITIVE)
                                            .addButton("узнать", NEGATIVE)
                                            .addButton("добавить", PRIMARY )
                                            .addButton("удалить задачу",NEGATIVE)
                                            .addButton("лисы", SECONDARY )
                                            .Build())
                                    .execute();
                        }

                        case NO -> {

                            notificationService.putTimeNotification(notification);

                            new MessagesSend(this)
                                    .setPeerId(message.getPeerId())
                                    .setMessage("Мы вас снова уведомим через 10 минут")
                                    .setKeyboard(ButtonBuilder.create()
                                            .addButton("отписаться", POSITIVE)
                                            .addButton("узнать", NEGATIVE)
                                            .addButton("добавить", PRIMARY )
                                            .addButton("удалить задачу",NEGATIVE)
                                            .addButton("лисы", SECONDARY )
                                            .Build())
                                    .execute();

                        }

                        case UNSUBSCRIBE -> {
                            personService.deletePerson(message.getPeerId());
                            new MessagesSend(this)
                                    .setPeerId(message.getPeerId())
                                    .setMessage("Лови")
                                    .setKeyboard(ButtonBuilder.create()
                                            .addButton("подписаться", POSITIVE)
                                            .addButton("лисы", SECONDARY)
                                            .Build())
                                    .execute();

                        }

                        case ADD -> {
                            lastCommand = command;
                            new MessagesSend(this)
                                    .setPeerId(message.getPeerId())
                                    .setMessage("Введите название цели и дату оповещения в формате: Цель,day,hour,minute")
                                    .execute();

                        }

                        case DELETE -> {
                            List<FindNotificationResponse.Notification> findPurposeResponse = notificationService.getNotification(message.getPeerId());
                            lastCommand2=Command.DELETE;
                            new MessagesSend(this)
                                    .setPeerId(message.getPeerId())
                                    .setMessage(findPurposeResponse.toString()+ "\n"+"Введите название цели,которую хотите удалить")
                                    .execute();

                        }

                        case FIND -> {
                            List<FindNotificationResponse.Notification> findPurposeResponse = notificationService.getNotification(message.getPeerId());
                            new MessagesSend(this)
                                    .setPeerId(message.getPeerId())
                                    .setMessage(findPurposeResponse.toString())
                                    .setKeyboard(ButtonBuilder.create()
                                            .addButton("отписаться", POSITIVE)
                                            .addButton("узнать", NEGATIVE)
                                            .addButton("добавить", PRIMARY)
                                            .addButton("удалить задачу",NEGATIVE)
                                            .addButton("лисы", SECONDARY)
                                            .Build())
                                    .execute();

                        }
                        case JOKE -> {
                            String string = "https://randomfox.ca/";
                            new MessagesSend(this)
                                    .setPeerId(message.getPeerId())
                                    .setMessage(string)
                                    .setKeyboard(ButtonBuilder.create()
                                            .addButton("отписаться", POSITIVE)
                                            .addButton("узнать", NEGATIVE)
                                            .addButton("добавить", PRIMARY)
                                            .addButton("удалить задачу",NEGATIVE)
                                            .addButton("лисы", SECONDARY)
                                            .Build())
                                    .execute();

                        }

                        case UNKNOWN -> new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Держи:")
                                .setKeyboard(ButtonBuilder.create()
                                        .addButton("отписаться", POSITIVE)
                                        .addButton("узнать", NEGATIVE)
                                        .addButton("добавить", PRIMARY)
                                        .addButton("удалить задачу",NEGATIVE)
                                        .addButton("лисы", SECONDARY)
                                        .Build())
                                .execute();

                    }
                }
            } else {

                switch (command) {
                    case SUBSCRIBE -> {
                        personService.createPerson(new PostPersonRequest(message.getPeerId()));
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage("Держи:")
                                .setKeyboard(ButtonBuilder.create()
                                        .addButton("отписаться", POSITIVE)
                                        .addButton("узнать", NEGATIVE)
                                        .addButton("добавить", PRIMARY )
                                        .addButton("удалить задачу",NEGATIVE)
                                        .addButton("лисы", SECONDARY )
                                        .Build())
                                .execute();
                    }

                    case JOKE -> {
                        String string="https://randomfox.ca/";
                        new MessagesSend(this)
                                .setPeerId(message.getPeerId())
                                .setMessage(string)
                                .setKeyboard(ButtonBuilder.create()
                                        .addButton("подписаться",POSITIVE)
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

                }


            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            command = Command.SERVER_ERROR;
            try {
                new MessagesSend(this)
                        .setPeerId(message.getPeerId())
                        .setMessage(command.getCommand())
                        .setKeyboard(ButtonBuilder.create()
                                .addButton("отписаться", POSITIVE)
                                .addButton("узнать", NEGATIVE)
                                .addButton("добавить", PRIMARY )
                                .addButton("удалить задачу",NEGATIVE)
                                .addButton("лисы", SECONDARY )
                                .Build())
                        .execute();

            } catch (BotsLongPollHttpException | BotsLongPollException ex) {
                e.printStackTrace();
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

