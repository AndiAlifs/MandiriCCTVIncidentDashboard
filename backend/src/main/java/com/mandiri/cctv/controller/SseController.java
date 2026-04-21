package com.mandiri.cctv.controller;

import com.mandiri.cctv.service.SseEmitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Events", description = "Server-Sent Events stream for real-time incident notifications")
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterService sseEmitterService;

    @Operation(summary = "Subscribe to incident events", description = "Opens a persistent SSE connection. Pushes INCIDENT_CREATED and INCIDENT_CLEARED events to the client in real time.")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return sseEmitterService.subscribe();
    }
}
