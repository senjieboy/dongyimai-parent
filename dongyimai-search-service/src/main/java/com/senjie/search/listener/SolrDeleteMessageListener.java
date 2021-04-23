package com.senjie.search.listener;

import com.senjie.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * @Author SenJie
 * @Data 2021/4/20 20:36
 */
@Component
public class SolrDeleteMessageListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage (Message message) {
        System.out.println("接收到删除消息");
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                Long[] ids = (Long[]) objectMessage.getObject();
                itemSearchService.deleteByGoodsId(Arrays.asList(ids));
                System.out.println("删除结束");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
