package com.playstation.manager.service;

import com.playstation.manager.dto.ClientDTO;
import com.playstation.manager.entity.Client;
import com.playstation.manager.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ActionLogService actionLogService;
    public Client create(ClientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setPhone(dto.getPhone());
        Client saved = clientRepository.save(client);
        actionLogService.log("CREATE", "Client", saved.getId());
        return saved;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public Client update(Long id, ClientDTO dto) {
        Client client = findById(id);
        client.setName(dto.getName());
        client.setPhone(dto.getPhone());
        Client saved = clientRepository.save(client);
        actionLogService.log("UPGRADE", "Client", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        Client client = findById(id);
        actionLogService.log("DELETE", "Client", id);
        clientRepository.delete(client);
    }
}