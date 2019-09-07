package com.t_systems.ad_stand.jms;

import com.t_systems.ad_stand.bean.PushBean;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.*;

@Startup
@Singleton
public class MyTopicSubscriber {
    private static final String WIRE_LEVEL_ENDPOINT = "tcp://localhost:61616";
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "admin";
    private static final ActiveMQConnectionFactory CONNECTION_FACTORY = new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
    private TopicConnection subscriberConnection;
    private TopicSession subscriberSession;
    private Topic subscriberTopic;
    private TopicSubscriber subscriber;

    @Inject
    private PushBean pushBean;

    @PostConstruct
    private void init() {
        try {
            CONNECTION_FACTORY.setUserName(USER_NAME);
            CONNECTION_FACTORY.setPassword(PASSWORD);
            subscriberConnection = CONNECTION_FACTORY.createTopicConnection();
            subscriberSession = subscriberConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            subscriberTopic = subscriberSession.createTopic("webstore_top_products");
            subscriber = subscriberSession.createSubscriber(subscriberTopic);
            subscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        pushBean.sendMessage(((TextMessage) message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            subscriberConnection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void close(){
        try {
            subscriber.close();
            subscriberSession.close();
            subscriberConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
