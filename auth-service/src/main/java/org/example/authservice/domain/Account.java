package org.example.authservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Account {

    private Long id;
    private double balance = 0;
    private Client client;
}
