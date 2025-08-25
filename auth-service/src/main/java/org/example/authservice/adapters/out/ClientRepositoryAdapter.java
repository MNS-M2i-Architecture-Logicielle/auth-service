package org.example.authservice.adapters.out;

import org.example.authservice.adapters.in.dto.ClientCreationRequest;
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
        try {
            Client client = clientPersistenceClient.getClientByEmail(email);
            return Optional.ofNullable(client);
        } catch (feign.FeignException.NotFound nf) {
            return Optional.empty();
        } catch (feign.FeignException fe) {
            throw fe;
        }
    }

    @Override
    public Client createClient(ClientCreationRequest request) {
        return clientPersistenceClient.createClient(request);
    }
}