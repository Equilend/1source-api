package com.os.events.websockets;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.os.client.model.Event;

@Controller
public class WebController {

    @MessageMapping("/events")
    @SendTo("/topic/pushmessages")
    public Event send(final Event event) throws Exception {

        return event;
    }

}
