package me.whiteship.demorestapi.configs;

import me.whiteship.demorestapi.accounts.Account;
import me.whiteship.demorestapi.accounts.AccountRepository;
import me.whiteship.demorestapi.accounts.AccountRole;
import me.whiteship.demorestapi.accounts.AccountService;
import me.whiteship.demorestapi.common.AppProperties;
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

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account admin = Account.builder()
                            .email(appProperties.getAdminUsername())
                            .password(appProperties.getAdminPassword())
                            .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                accountService.saveAccount(admin);

                Account user = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(Set.of(AccountRole.USER))
                        .build();

                accountService.saveAccount(user);

            }
        };
    }
}
