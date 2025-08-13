package com.ninehub.authentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableMethodSecurity // Une partie de la gestion des permission est controller par les methodes
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(JwtFilter jwtFilter, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtFilter = jwtFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return
                httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                authorize ->
                                        authorize
                                                .requestMatchers(POST,"/register").permitAll()
                                                .requestMatchers(POST, "/activate").permitAll()
                                                .requestMatchers(POST, "/login").permitAll()
                                                .requestMatchers(POST, "/password/update").permitAll()
                                                .requestMatchers(POST, "/password/new").permitAll()
                                                .requestMatchers(POST, "/refresh/token").permitAll()
                                                // Pour mieux gerer les permissions, on peut utiliser hasAnyAuthority au lieu de hasRole ou hasAnyRole
                                                .requestMatchers(GET, "/avis/all").hasAnyAuthority("ROLE_MODERATOR","ROLE_ADMIN")
                                                .anyRequest().authenticated()
                                // On ajoute une session pour l'authentification de l'utilisateur
                        )
                        .sessionManagement(httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // STATELESS veut dire aucun etat. chaque fois on verifie le jwt du user

                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }


    // S'appuie  sur l'authentication (gere l'authentification)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Verifier que les mots de passes et identifaint que l'utilisateur soumit sont bien ceux qu'on attend
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new UserService();
//    }

    // Acceder a la bdd. C'est lui qui s'appui sur la bdd pour gerer les informations a cause du authentication manager
    // C'est a partir de ca qu'il va choisir d'authenifier notre utilisateur
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}
