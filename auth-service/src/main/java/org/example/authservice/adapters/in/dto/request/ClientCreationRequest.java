package org.example.authservice.adapters.in.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreationRequest {
    private String name;
    private String mail;
    private String password;
}
