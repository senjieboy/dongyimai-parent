package com.senjie.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;

/**
 * @Author SenJie
 * @Data 2021/4/22 16:21
 */
@RestController
public class ShortMessage {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination queueSMSDestination;

    @RequestMapping("/sendMessage")
    public String sendMsg (String mobile, String param) {
        jmsTemplate.send(queueSMSDestination, new MessageCreator() {
            @Override
            public Message createMessage (Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("mobile", mobile);
                mapMessage.setString("param", param);
                return mapMessage;
            }
        });
        return "SendOk";
    }
}
