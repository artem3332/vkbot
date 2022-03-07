package com.example.demojpa.vkbot.request;

import lombok.Data;

@Data
public class DeletePurposeRequest
{
    private final String purpose;
    private final Long userId;

}
