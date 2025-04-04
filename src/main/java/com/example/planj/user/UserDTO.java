package com.example.planj.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Data
public class UserDTO {

    private String username;
    private String password;
    private String phone;
    private String email;

}
