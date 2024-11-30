package com.example.planj.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDto) {
        SiteUser user = userService.findByUsername(userDto.getUsername());

        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCreateForm userCreateForm, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            return ResponseEntity.badRequest().body("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getPhone(), userCreateForm.getEmail());
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: " + e.getMessage());
        }
    }


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/username")
    public ResponseEntity<String> findUsername(@RequestParam String email) {
        logger.info("클라이언트에서 전달된 이메일: {}", email);
        try {
            String cleanedEmail = email.trim().toLowerCase();
            String username = userService.findUsernameByEmail(cleanedEmail);
            logger.info("DB에서 조회된 아이디: " + username);
            if (username != null) {
                return ResponseEntity.ok("아이디는 " + username + "입니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("등록된 이메일이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("아이디 찾기 실패: " + e.getMessage());
        }
    }

    @PostMapping("/password")
    public ResponseEntity<String> findPassword(@RequestBody UserDTO userDto) {
        System.out.println("입력된 아이디: " + userDto.getUsername());
        System.out.println("입력된 이메일: " + userDto.getEmail());
        try {
            String cleanedUsername = userDto.getUsername().trim();
            String cleanedEmail = userDto.getEmail().trim().toLowerCase();
            SiteUser user = userService.findByUsernameAndEmail(cleanedUsername, cleanedEmail);
            System.out.println("DB에서 조회된 사용자: " + user);
            if (user != null) {
                String tempPassword = userService.generateTemporaryPassword();
                userService.updatePassword(user, passwordEncoder.encode(tempPassword));
                return ResponseEntity.ok("임시 비밀번호: " + tempPassword);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디와 이메일이 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 찾기 실패: " + e.getMessage());
        }
    }

}
