package com.behrend.contestmanager.config;

import com.behrend.contestmanager.service.CookieServices;
import com.behrend.contestmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import static org.hibernate.criterion.Restrictions.and;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*@Autowired
    private UserDetails userDetailsService;*/

    @Autowired
    private CookieServices cookieServices;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }*/

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable()).authorizeRequests()//security measure for CSRF attacks and excludes username/password from HTTP requests
                .antMatchers("/CoordinatorTools","/tournament/create", "/player/create", "/ruleset/create", "/rule/create", "/TournamentRunner").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .failureUrl("redirect:/LoginFailed")
                .permitAll()
                .and()
                .logout()
                /*.addLogoutHandler(new SecurityContextLogoutHandler())
                .addLogoutHandler(new CookieClearingLogoutHandler("JSESSIONID"))
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .addFilterBefore(new SessionFilter(cookieServices), UsernamePasswordAuthenticationFilter.class)*/
                .permitAll()
                .and()
                .build();
    }

    /*@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }*/

}
