package com.example.planj.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    //user는 테이블이 안 만들어짐 (mysql만..?)
    private String password;

    private String phone;

    @Column(unique = true)
    private String email;

}
