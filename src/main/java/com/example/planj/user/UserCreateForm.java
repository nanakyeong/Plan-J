package com.example.planj.user;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserCreateForm {

    @Size(min = 2, max = 20)
    @NotEmpty(message = "아이디는 필수 항목입니다.") //Notnull
    private String username;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email
    private String email;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    private String phone;

    public @Size(min = 2, max = 20)
    @NotEmpty(message = "아이디는 필수 항목입니다.")
    String getUsername() {
        return username;
    }

}
