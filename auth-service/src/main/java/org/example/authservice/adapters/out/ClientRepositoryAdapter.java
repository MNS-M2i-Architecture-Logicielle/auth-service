package org.example.authservice.adapters.out;

import org.example.authservice.application.port.out.ClientRepository;
import org.example.authservice.domain.Client;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientRepositoryAdapter implements ClientRepository {

    private final ClientPersistenceClient clientPersistenceClient;

    public ClientRepositoryAdapter(ClientPersistenceClient clientPersistenceClient) {
        this.clientPersistenceClient = clientPersistenceClient;
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        return clientPersistenceClient.getClientByMail(email);
    }

    @Override
    public Client createClient(Client request) {
        return clientPersistenceClient.createClient(request);
    }
}