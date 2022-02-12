package com.example.demojpa.vkbot.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurposeRequest
{
    private final String purpose;
    private final LocalDateTime time;


}
