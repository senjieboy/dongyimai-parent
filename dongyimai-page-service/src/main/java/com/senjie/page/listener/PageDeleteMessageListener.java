package com.senjie.page.listener;

import com.senjie.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Author SenJie
 * @Data 2021/4/20 21:31
 */
@Component
public class PageDeleteMessageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage (Message message) {
        if(message instanceof ObjectMessage){
            try {
                ObjectMessage objectMessage= (ObjectMessage) message;
                Long [] ids= (Long[]) objectMessage.getObject();
                itemPageService.deleteItemHtml(ids);
                System.out.println("static page deleted successfully");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
