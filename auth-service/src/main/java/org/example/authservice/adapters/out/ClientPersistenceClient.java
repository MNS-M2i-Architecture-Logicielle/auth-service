package org.example.authservice.adapters.out;

import lombok.Getter;
import lombok.Setter;
import org.example.authservice.adapters.in.dto.ClientCreationRequest;
import org.example.authservice.domain.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "client-persistence-service", url = "http://localhost:8090/api/persistence")
public interface ClientPersistenceClient {

    @GetMapping("/clients/mail/{mail}")
    Optional<Client> getClientByMail(@PathVariable("mail") String mail);

    @PostMapping("/clients")
    Client createClient(@RequestBody Client client);
}

