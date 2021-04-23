package com.senjie.search.listener;
import com.alibaba.fastjson.JSON;
import com.senjie.pojo.TbItem;
import com.senjie.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * @Author SenJie
 * @Data 2021/4/20 19:47
 */
@Component
public class SolrMessageListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage (Message message) {
        System.out.println("接受到消息导入数据");
        if (message instanceof TextMessage) {

            try {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                List<TbItem> itemList = JSON.parseArray(text, TbItem.class);
                for (TbItem item : itemList) {
                    System.out.println(item.getTitle());
                    Map map = JSON.parseObject(item.getSpec(), Map.class);
                    item.setSpecMap(map);
                }
                itemSearchService.importData(itemList);
                System.out.println("end of the import ! ! !");
            } catch (JMSException e) {
                e.printStackTrace();
            }


        }
    }
}
