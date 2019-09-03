package com.t_systems.ad_stand.jms;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean(name = "jmsTest")
@SessionScoped
public class JmsTest implements Serializable {
    @Inject
    Sender sender;

    public void send(){
        sender.sendMessage("Hello World!");
    }
}
