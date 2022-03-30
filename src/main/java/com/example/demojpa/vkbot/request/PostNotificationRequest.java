package com.example.demojpa.vkbot.request;

import com.example.demojpa.vkbot.utils.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostNotificationRequest
{
    private final String notification;
    private final Status status;
    private final LocalDateTime time;


}
