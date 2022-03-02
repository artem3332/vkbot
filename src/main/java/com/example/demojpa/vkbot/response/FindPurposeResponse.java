package com.example.demojpa.vkbot.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FindPurposeResponse
{
    private List<Purpose> purposes;

    @Data
    public static class Purpose{
        private String purpose;
        private LocalDateTime time;


        @Override
        public String toString() {



                String stroka="Цель:"+purpose+"  "+"\n"+"Время выполнения: "+time.getDayOfMonth()+"day,"+time.getHour()+" : "+time.getMinute()+"\n";



            return   stroka;

        }
    }




}


