package com.example.planj.user;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserCreateForm {
    @Size(min = 2, max = 20)
    @NotEmpty(message = "아이디는 필수 항목입니다.")
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

    public @Size(min = 2, max = 20) @NotEmpty(message = "아이디는 필수 항목입니다.") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 2, max = 20) @NotEmpty(message = "아이디는 필수 항목입니다.") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "비밀번호는 필수 항목입니다.") String getPassword1() {
        return password1;
    }

    public void setPassword1(@NotEmpty(message = "비밀번호는 필수 항목입니다.") String password1) {
        this.password1 = password1;
    }

    public @NotEmpty(message = "이메일은 필수 항목입니다.") @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "이메일은 필수 항목입니다.") @Email String email) {
        this.email = email;
    }

    public @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.") String getPassword2() {
        return password2;
    }

    public void setPassword2(@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.") String password2) {
        this.password2 = password2;
    }

    public @NotEmpty(message = "전화번호는 필수 항목입니다.") String getPhone() {
        return phone;
    }

    public void setPhone(@NotEmpty(message = "전화번호는 필수 항목입니다.") String phone) {
        this.phone = phone;
    }
}
