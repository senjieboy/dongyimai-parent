package com.senjie.sms.listener;


import com.senjie.sms.service.ShortMassageSend;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @Author SenJie
 * @Data 2021/4/22 15:54
 */
@Component
public class ShortMessageSendListener implements MessageListener {

    @Autowired
    private ShortMassageSend shortMessageSend;

    @Override
    public void onMessage (Message message) {
        if (message instanceof MapMessage) {
            try {
                MapMessage mapMessage = (MapMessage) message;
                System.out.println("Request message received"+mapMessage.getString("mobile"));
                HttpResponse response = shortMessageSend.shortMassageSend(mapMessage.getString("mobile"), mapMessage.getString("param"));
                System.out.println("End of the request"+response.getEntity());
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
