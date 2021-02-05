package me.whiteship.demorestapi.accounts;

import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    //@Autowired
    //AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername(){
        //Given
        String password = "jongwoo";
        String username = "jongwoo@email.com";

        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

//        this.accountRepository.save(account);
        this.accountService.saveAccount(account);

        //When
        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Then
        //assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword()))
                .isTrue();

    }

    @Test
    public void findByUsernameFail(){

        final String username = "random@email.com";
        Assertions.assertThrows(UsernameNotFoundException.class, ()->{
            accountService.loadUserByUsername(username);
        });
    }

}