package me.whiteship.demorestapi.configs;

import me.whiteship.demorestapi.accounts.Account;
import me.whiteship.demorestapi.accounts.AccountRole;
import me.whiteship.demorestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
    //스프링 시큐리티 최신버전 인코딩된 패스워드 앞에 프리픽스가 붙어 어떠한 방식으로 암호화 되었는지 알 수 있다
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){

        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account jongwoo = Account.builder()
                            .email("whddn528@email.com")
                            .password("jongwoo")
                            .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                accountService.saveAccount(jongwoo);

            }
        };
    }
}
