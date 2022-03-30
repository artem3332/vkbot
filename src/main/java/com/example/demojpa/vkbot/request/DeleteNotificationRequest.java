package com.example.demojpa.vkbot.request;

import lombok.Data;

@Data
public class DeleteNotificationRequest
{
    private final String notification;
    private final Long userId;

}
