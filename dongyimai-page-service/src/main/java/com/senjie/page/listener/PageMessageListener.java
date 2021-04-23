package com.senjie.page.listener;

import com.senjie.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * @Author SenJie
 * @Data 2021/4/20 21:09
 */
@Component
public class PageMessageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage (Message message) {
        if (message instanceof TextMessage) {
            try {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                itemPageService.genItemHtml(Long.parseLong(text));
                System.out.println("Static interface generated successfully");
            } catch (JMSException e) {
                e.printStackTrace();
            }


        }
    }
}
