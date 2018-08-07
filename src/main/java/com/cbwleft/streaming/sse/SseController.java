package com.cbwleft.streaming.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@Slf4j
@Controller
public class SseController {

    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    private PositionRepository positionRepository;

    @RequestMapping(value = "/")
    public String index(Map<String, Object> model) {
        model.put("position", Arrays.toString(positionRepository.getPosition()));
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getPositionEvent() {
        final SseEmitter emitter = new SseEmitter();//default 30 seconds
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitters.add(emitter);
        return emitter;
    }

    @EventListener
    public void onPositionEvent(PositionEvent event) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(event().reconnectTime(1000).data(event, MediaType.APPLICATION_JSON));//set retry
            } catch (Exception ex) {
                log.info("Error sending event to client");
            }
        }
    }

}
