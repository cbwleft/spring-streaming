package com.cbwleft.streaming.sse.one2one;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Async
    public void execute(String id) {
        for(int progress = 0; progress <= 100; progress++) {
            try {
                Thread.sleep(1000);
                publisher.publishEvent(new UserEvent(this, id, progress));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
