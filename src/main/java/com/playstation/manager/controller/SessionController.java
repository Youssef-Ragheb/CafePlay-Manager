package com.playstation.manager.controller;

import com.playstation.manager.dto.DeviceSessionDTO;
import com.playstation.manager.dto.EndSessionRequest;
import com.playstation.manager.dto.OrderDTO;
import com.playstation.manager.dto.SessionDTO;
import com.playstation.manager.entity.Session;
import com.playstation.manager.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/start")
    public ResponseEntity<DeviceSessionDTO> startSession(@RequestBody SessionDTO dto) {
        return ResponseEntity.ok(sessionService.startSession(dto));
    }

    @PostMapping("/add-order/{sessionId}")
    public ResponseEntity<DeviceSessionDTO> addOrder(@PathVariable Long sessionId, @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(sessionService.addOrder(sessionId, orderDTO));
    }
    @PostMapping("/remove-order/{sessionId}/{orderId}")
    public ResponseEntity<DeviceSessionDTO> removeOrder(@PathVariable Long sessionId, @PathVariable Long orderId) {
        return ResponseEntity.ok(sessionService.removeOrder(sessionId, orderId));
    }

    @PostMapping("/end")
    public ResponseEntity<DeviceSessionDTO> endSession(@RequestBody EndSessionRequest request) {
        return ResponseEntity.ok(sessionService.endSession(request.getSessionId()));
    }
    @GetMapping("/current/{deviceId}")
    public ResponseEntity<DeviceSessionDTO> getCurrentSession(@PathVariable Long deviceId) {
        return ResponseEntity.ok(sessionService.getCurrentSessionForDevice(deviceId));
    }
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSessions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Session> sessionsPage = sessionService.getSessionsBetweenDates(start, end, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("sessions", sessionsPage.getContent());
        response.put("totalItems", sessionsPage.getTotalElements());
        response.put("totalPages", sessionsPage.getTotalPages());
        response.put("currentPage", sessionsPage.getNumber());
        return ResponseEntity.ok(response);
    }

}