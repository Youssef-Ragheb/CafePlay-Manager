package com.playstation.manager.controller;

import com.playstation.manager.dto.ClientDebtDTO;
import com.playstation.manager.dto.DeviceSessionDTO;
import com.playstation.manager.entity.CafeOrder;
import com.playstation.manager.entity.ClientDebt;
import com.playstation.manager.service.ClientDebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/client-debts")
@RequiredArgsConstructor
public class ClientDebtController {

    private final ClientDebtService clientDebtService;

    @PostMapping
    public ResponseEntity<ClientDebt> create(@RequestBody ClientDebtDTO dto) {
        return ResponseEntity.ok(clientDebtService.create(dto));
    }

    @PostMapping("/order/{orderId}/client/{clientId}")
    public ResponseEntity<CafeOrder> changeOrderToDebt(@PathVariable Long clientId,
                                                       @PathVariable Long orderId) {
        CafeOrder result = clientDebtService.changeOrderToDebt(clientId, orderId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/session/{sessionId}/client/{clientId}")
    public ResponseEntity<DeviceSessionDTO> changeSessionToDebt(@PathVariable Long clientId,
                                                                @PathVariable Long sessionId) {
        DeviceSessionDTO result = clientDebtService.changeSessionToDebt(clientId, sessionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientDebt>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientDebtService.getByClientId(clientId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientDebtService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public List<ClientDebt> getAll() {
        return clientDebtService.getAll();
    }
}