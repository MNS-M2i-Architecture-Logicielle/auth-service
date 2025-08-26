package org.example.authservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private Long id;
    private String name;
    private String mail;
    private String password;
    private List<Account> account;
}

