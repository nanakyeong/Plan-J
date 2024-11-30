package com.example.planj.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    // 이메일로 아이디 찾기
    public String findUsernameByEmail(String email) {
        Optional<SiteUser> optionalUser = userRepository.findByEmail(email);
        return optionalUser.map(SiteUser::getUsername).orElse(null);
    }

    // 아이디와 이메일로 사용자 찾기
    public SiteUser findByUsernameAndEmail(String username, String email) {
        Optional<SiteUser> optionalUser = userRepository.findByUsernameAndEmail(username, email);
        return optionalUser.orElse(null);
    }

    // 임시 비밀번호 생성
    public String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 임시 비밀번호 생성
    }

    // 비밀번호 업데이트
    public void updatePassword(SiteUser user, String encodedPassword) {
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

}

