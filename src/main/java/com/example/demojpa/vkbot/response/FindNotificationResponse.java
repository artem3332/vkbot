package com.example.demojpa.vkbot.response;

import com.example.demojpa.vkbot.utils.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FindNotificationResponse
{
    private List<Notification> notifications;

    @Data
    public static class Notification{

        private String notification;
        private Status status;
        private LocalDateTime time;


        @Override
        public String toString() {



                String stroka=notification+"  "+"\n"+"Время выполнения: "+
                        time.getDayOfMonth()+" day,"+time.getHour()+" : "+time.getMinute()+"\n"+
                        "статус задачи:"+status+"\n"+"\n";



            return   stroka;

        }
    }




}


