package com.cbwleft.streaming.sse.one2one;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@Controller
@Slf4j
public class UserSseController {

    private Map<String, SseEmitter> one2one = Collections.synchronizedMap(new WeakHashMap<>());

    @Autowired
    private AsyncService asyncService;

    @RequestMapping(value = "/user/{id}")
    public String user(@PathVariable String id, Map<String, Object> model) {
        model.put("id", id);
        return "user";
    }

    @RequestMapping(value = "/trigger/{id}")
    public void trigger(@PathVariable String id) {
        asyncService.execute(id);
    }

    @ResponseBody
    @RequestMapping(value = "/sse/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse(@PathVariable String id) {
        final SseEmitter emitter = new SseEmitter();//default 30 seconds
        emitter.onCompletion(() -> one2one.remove(id));
        one2one.put(id, emitter);
        return emitter;
    }

    @EventListener
    public void onUserEvent(UserEvent event) {
        try {
            SseEmitter sseEmitter = one2one.get(event.getId());
            sseEmitter.send(event().reconnectTime(1000).data(event, MediaType.APPLICATION_JSON));//set retry
        } catch (Exception ex) {
            log.info("Error sending event to client");
        }
    }
}
