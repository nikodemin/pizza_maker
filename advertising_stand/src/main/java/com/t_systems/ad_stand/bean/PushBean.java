package com.t_systems.ad_stand.bean;


import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ApplicationScoped
public class PushBean implements Serializable {

    @Inject
    @Push(channel = "topProducts")
    private PushContext push;

    public void sendMessage(String text){
        push.send(text);
    }
}
