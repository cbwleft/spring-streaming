package com.cbwleft.streaming.sse.one2one;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {

    private String id;

    private int progress;

    public UserEvent(Object source, String id, int progress) {
        super(source);
        this.id = id;
        this.progress = progress;
    }
}
