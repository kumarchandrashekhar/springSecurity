package com.spring_security_30.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {

    private String usernameOrEmail;
    private String password;
}
