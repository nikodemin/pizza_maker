package com.t_systems.webstore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * set userDetailsService and passwordEncoder
     * @param auth AuthenticationManagerBuilder
     * @throws Exception ex
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/admin/**","/external/**")
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/payment","/orders","/customProduct")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/cart")
                .access("!hasRole('ROLE_ADMIN')")
                .antMatchers("/settings")
                .access("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
                .antMatchers("/external/**","/", "/**").permitAll()

                .and()
                .formLogin().loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/",true)

                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true);
    }
}
