package com.example.demojpa.vkbot.service;

import com.example.demojpa.vkbot.request.DeleteNotificationRequest;
import com.example.demojpa.vkbot.request.PostNotificationRequest;
import com.example.demojpa.vkbot.request.PutNotificationRequest;
import com.example.demojpa.vkbot.response.FindNotificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class NotificationService {


    @Value("${dempjpa.service.purpose.url}")
    private String postNotificationUrl;

    @Value("${dempjpa.service.purposeAll.url}")
    private String getNotificationAllUrl;


    @Value("${dempjpa.service.purposestatus.url}")
    private String putStatusNotificationUrl;

    @Value("${dempjpa.service.purposetime.url}")
    private String putTimeNotificationUrl;



    @Value("${dempjpa.service.purposedelete.url}")
    private String deleteNotificationUrl;


    public  String createNotification(PostNotificationRequest postNotificationRequest, Integer vkid)
    {
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PostNotificationRequest> entity = new HttpEntity<>(postNotificationRequest, headers);
        return restTemplate.postForObject(postNotificationUrl+vkid, entity, String.class);

    }

    public void deleteNotification(String notification, Long userId)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate=new RestTemplate();

        DeleteNotificationRequest deleteNotificationRequest=new DeleteNotificationRequest(notification,userId);
        HttpEntity<DeleteNotificationRequest> entity = new HttpEntity<>(deleteNotificationRequest, headers);

        try {

            restTemplate.exchange(deleteNotificationUrl,HttpMethod.DELETE, entity, String.class);

        }
        catch (HttpClientErrorException e){
        }
    }

    public void putStatusNotification(String notification)
      {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate=new RestTemplate();

        PutNotificationRequest putNotificationRequest=new PutNotificationRequest(notification);

        HttpEntity<PutNotificationRequest> entity = new HttpEntity<>(putNotificationRequest, headers);
        try {


            restTemplate.exchange(putStatusNotificationUrl,HttpMethod.PUT,entity,String.class);

        }
        catch (HttpClientErrorException e){
        }

    }

    public void putTimeNotification(String notification)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate=new RestTemplate();

        PutNotificationRequest putNotificationRequest=new PutNotificationRequest(notification);

        HttpEntity<PutNotificationRequest> entity = new HttpEntity<>(putNotificationRequest, headers);

        try {

            restTemplate.exchange(putTimeNotificationUrl,HttpMethod.PUT,entity,String.class);

        }
        catch (HttpClientErrorException e){
        }

    }




    public List<FindNotificationResponse.Notification> getNotification(Integer vkid) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<FindNotificationResponse.Notification>> response = restTemplate.exchange(URI.create(getNotificationAllUrl + vkid), HttpMethod.GET, null, new ParameterizedTypeReference<List<FindNotificationResponse.Notification>>() {
        });
        return response.getBody();
    }


}
