package org.example.authservice.application.port.out;


import org.example.authservice.adapters.in.dto.ClientCreationRequest;
import org.example.authservice.domain.Client;

import java.util.Optional;

public interface ClientRepository {
    Optional<Client> findByEmail(String email);
    Client createClient(ClientCreationRequest request);
}
