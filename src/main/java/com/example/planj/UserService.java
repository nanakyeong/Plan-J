package com.example.planj;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password, String phone, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(username);
        siteUser.setPassword(passwordEncoder.encode(password));
        siteUser.setPhone(phone);
        siteUser.setEmail(email);
        return userRepository.save(siteUser);
    }

    public SiteUser findByUsername(String username) {
        Optional<SiteUser> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null); // 사용자 없을 경우 null 반환
    }

}

