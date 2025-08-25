package org.example.authservice.adapters.out;

import lombok.Getter;
import lombok.Setter;
import org.example.authservice.adapters.in.dto.ClientCreationRequest;
import org.example.authservice.domain.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "client-persistence-service", url = "http://localhost:8082")
public interface ClientPersistenceClient {

    @GetMapping("/api/clients/email/{email}")
    Client getClientByEmail(@PathVariable("email") String email);

    @PostMapping("/api/clients")
    Client createClient(@RequestBody ClientCreationRequest client);
}

