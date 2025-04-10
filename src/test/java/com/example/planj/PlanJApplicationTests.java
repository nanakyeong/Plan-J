package com.example.planj;

import com.example.planj.frame.JoinFrame;
import com.example.planj.frame.LoginFrame;
import com.example.planj.user.SiteUser;
import com.example.planj.user.UserRepository;
import com.example.planj.user.UserSecurityService;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlanJApplicationTests {

    @MockBean
    private UploadpageFrame uploadpageFrame;

    @MockBean
    private MainpageFrame mainpageFrame;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PlanwritepageFrame planwritepageFrame;

    @MockBean
    private LoginFrame loginFrame;

    @MockBean
    private JoinFrame joinFrame;

    @Autowired
    private UserSecurityService userSecurityService;

    @Test
    @DisplayName("로그인 테스트 성공")
    void loginSuccess() {
        // given
        String username = "admin";
        String password = "admin";

        SiteUser siteUser = new SiteUser();
        siteUser.setId(1L);
        siteUser.setUsername(username);
        siteUser.setPassword(password);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(siteUser));

        // when
        UserDetails result = userSecurityService.loadUserByUsername(username);

        // then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
    }

    @Test
    @DisplayName("로그인 테스트 실패")
    void loginFail() {
        // given
        String username = "ttest";

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(UsernameNotFoundException.class, () -> {
            userSecurityService.loadUserByUsername(username);
        });
    }
}