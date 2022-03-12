package com.example.demojpa.vkbot.request;

import com.example.demojpa.vkbot.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurposeRequest
{
    private final String purpose;
    private final Status status;
    private final LocalDateTime time;


}
