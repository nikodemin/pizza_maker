package com.t_systems.ad_stand.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

    public void sendMessage(String message) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616"); // ActiveMQ-specific
        Connection con = null;
        try {
            con = factory.createConnection();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE); // non-transacted session

            Queue queue = session.createQueue("webstore"); // only specifies queue name

            MessageProducer producer = session.createProducer(queue);
            Message msg = session.createTextMessage(message); // text message
            producer.send(msg);

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close(); // free all resources
                } catch (JMSException e) { /* Ignore */ }
            }
        }
    }
}
