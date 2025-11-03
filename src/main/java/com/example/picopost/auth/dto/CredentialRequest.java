package com.example.picopost.auth.dto;

import lombok.Data;

@Data
public class CredentialRequest {
    private String username;
    private String password;
}
