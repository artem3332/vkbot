package com.example.demojpa.vkbot.service;

import com.example.demojpa.vkbot.request.PersonRequest;
import com.example.demojpa.vkbot.response.FindPersonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PersonService {

    @Value("${dempjpa.service.createPerson.url}")
    private String createPersonUrl;


    @Value("${dempjpa.service.getPerson.url}")
    private String getPersonUrl;

    @Value("${dempjpa.service.deletePerson.url}")
    private String deletePersonUrl;


    public String createPerson(PersonRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PersonRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(createPersonUrl, entity, String.class);
    }

    public void deletePerson(Integer vkid)
    {
        RestTemplate restTemplate=new RestTemplate();
        try {
             restTemplate.delete(deletePersonUrl+vkid);
        }
        catch (HttpClientErrorException e){
        }
    }


    public FindPersonResponse getPerson(Integer vkId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(getPersonUrl + vkId, FindPersonResponse.class);
        }catch (HttpClientErrorException.NotFound e){
            return null;
        }
    }
}
