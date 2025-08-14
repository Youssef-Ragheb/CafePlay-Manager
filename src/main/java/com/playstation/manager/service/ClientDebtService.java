package com.playstation.manager.service;

import com.playstation.manager.dto.ClientDebtDTO;
import com.playstation.manager.dto.DeviceSessionDTO;
import com.playstation.manager.entity.CafeOrder;
import com.playstation.manager.entity.Client;
import com.playstation.manager.entity.ClientDebt;
import com.playstation.manager.entity.Session;
import com.playstation.manager.repository.CafeOrderRepository;
import com.playstation.manager.repository.ClientDebtRepository;
import com.playstation.manager.repository.ClientRepository;
import com.playstation.manager.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientDebtService {

    private final ClientDebtRepository clientDebtRepository;
    private final ClientRepository clientRepository;
    private final ActionLogService actionLogService;
    private final CafeOrderRepository cafeOrderRepository;
    private final CafeOrderService cafeOrderService;
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    public ClientDebt create(ClientDebtDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        ClientDebt debt = new ClientDebt();
        debt.setClient(client);
        debt.setAmount(dto.getAmount());
        debt.setNote(dto.getNote());
        debt.setCreatedAt(LocalDateTime.now());

        ClientDebt saved = clientDebtRepository.save(debt);
        actionLogService.log("CREATE", "ClientDebt", saved.getId());
        return saved;
    }

    public List<ClientDebt> getByClientId(Long clientId) {
        return clientDebtRepository.findByClientId(clientId);
    }

    public CafeOrder changeOrderToDebt(Long ClientId, Long orderId) {
        Client client = clientRepository.findById(ClientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        CafeOrder order = cafeOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ClientDebt debt = new ClientDebt();
        debt.setClient(client);
        debt.setAmount(order.getTotalAmount()); // Assuming the order amount is 0 for this example
        debt.setNote("Cafe Order:  " + orderId);
        debt.setCreatedAt(LocalDateTime.now());

        ClientDebt saved = clientDebtRepository.save(debt);
        CafeOrder  cafeOrder = cafeOrderService.finalizeOrder(orderId); // Assuming you have a method to finalize the order
        actionLogService.log("CREATE", "ClientDebt", saved.getId());
        return cafeOrder;
    }

    public DeviceSessionDTO changeSessionToDebt(Long ClientId, Long sessionId) {
        Client client = clientRepository.findById(ClientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ClientDebt debt = new ClientDebt();
        debt.setClient(client);
        debt.setAmount(session.getTotalPrice()); // Assuming the order amount is 0 for this example
        debt.setNote("Session ID:  " + sessionId);
        debt.setCreatedAt(LocalDateTime.now());

        ClientDebt saved = clientDebtRepository.save(debt);
         // Assuming you have a method to end the session
        actionLogService.log("CREATE", "ClientDebt", saved.getId());
        return sessionService.endSession(sessionId);
    }

    public void delete(Long id) {
        actionLogService.log("DELETE", "ClientDebt", id);
        clientDebtRepository.deleteById(id);
    }

    public List<ClientDebt> getAll(){
        return clientDebtRepository.findAll();
    }
}