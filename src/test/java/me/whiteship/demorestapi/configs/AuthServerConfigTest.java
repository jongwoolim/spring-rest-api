package me.whiteship.demorestapi.configs;

import me.whiteship.demorestapi.accounts.Account;
import me.whiteship.demorestapi.accounts.AccountRole;
import me.whiteship.demorestapi.accounts.AccountService;
import me.whiteship.demorestapi.common.AppProperties;
import me.whiteship.demorestapi.common.BaseControllerTest;
import me.whiteship.demorestapi.common.TestDescription;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {

        //Given
//        final String username = "whddn528@email.com";
//        final String password = "jongwoo";
//

//        final Account jongwoo = Account.builder()
//                .email(appProperties.getUserUsername())
//                .password(appProperties.getUserPassword())
//                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
//                .build();

        //this.accountService.saveAccount(jongwoo);

        String clientId = "myApp";
        String clientSecret = "pass";
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                    .param("username",appProperties.getUserUsername())
                    .param("password",appProperties.getUserPassword())
                    .param("grant_type", "password"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("access_token").exists());
    }
}