package me.whiteship.demorestapi.configs;

import me.whiteship.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

    /*
    AutherizationServer랑 ResourceServer에서 해당 AuthenticationManager를 참조할 수 있도록 bean으로 노출시킨다
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    //시큐리티 필터 적용 x
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");
        //정적 리소스들에 대한 기본 위치 적용 x
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().and()
                .formLogin()
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated()
                .anyRequest().authenticated();
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers("/docs/index.html")
//                .anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//                .anonymous();
//    }
}
