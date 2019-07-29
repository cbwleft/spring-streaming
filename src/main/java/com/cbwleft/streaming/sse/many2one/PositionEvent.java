package com.cbwleft.streaming.sse.many2one;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PositionEvent extends ApplicationEvent {

    private final double[] position;

    public PositionEvent(Object source, double[] position) {
        super(source);
        this.position = position;
    }

}
