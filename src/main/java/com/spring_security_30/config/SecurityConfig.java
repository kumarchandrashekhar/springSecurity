package com.spring_security_30.config;


import com.spring_security_30.security.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
@Autowired
    private CustomUserDetail userDetailsService;

@Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

// this is InMemoryUserDetails Services
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        // first user
//        UserDetails normalUser = User.withUsername("shekhar")
//                .password(passwordEncoder().encode("password"))
//                .roles("NORMAL")
//                .build();
//
//        // second user
//
//        UserDetails adminUser = User.withUsername("durgesh")
//                .password(passwordEncoder().encode("password"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser,adminUser);
//    }


    //this method give userDetails from database

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
@Bean
    public AuthenticationProvider AuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
    http
            .csrf(csrf->csrf.disable())
            .authorizeHttpRequests((authz)->authz
                    //role base authentication
//                    .requestMatchers("/home/normal")
//                    .hasRole("NORMAL")
//                    .requestMatchers("/home/admin")
//                    .hasRole("ADMIN")
//                    .requestMatchers("/home/public")
//                    .permitAll()
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())

            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
