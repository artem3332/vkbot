package com.example.demojpa.vkbot.service;

import com.example.demojpa.vkbot.request.DeletePurposeRequest;
import com.example.demojpa.vkbot.request.PurposeRequest;
import com.example.demojpa.vkbot.response.FindPurposeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class PurposeService {


    @Value("${dempjpa.service.purposeAll.url}")
    private String  getPurposeUrl;


    @Value("${dempjpa.service.purpose.url}")
    private String postPurposeUrl;

    @Value("${dempjpa.service.purposedelete.url}")
    private String deletePurposeUrl;


    public  String createPurpose(PurposeRequest purposeRequest,Integer vkid)
    {
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PurposeRequest> entity = new HttpEntity<>(purposeRequest, headers);
        return restTemplate.postForObject(postPurposeUrl+vkid, entity, String.class);

    }

    public void deletePurpose(String purpose,Long userId)
    {
        RestTemplate restTemplate=new RestTemplate();
        try {
            restTemplate.delete(deletePurposeUrl,new DeletePurposeRequest(purpose,userId));
        }
        catch (HttpClientErrorException e){
        }
    }



    public List<FindPurposeResponse.Purpose> getPurpose(Integer vkid) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<FindPurposeResponse.Purpose>> response = restTemplate.exchange(URI.create(getPurposeUrl + vkid), HttpMethod.GET, null, new ParameterizedTypeReference<List<FindPurposeResponse.Purpose>>() {
        });
        return response.getBody();
    }


}
