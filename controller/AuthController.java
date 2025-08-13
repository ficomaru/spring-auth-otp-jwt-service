package com.ninehub.authentication.controller;

import com.ninehub.authentication.dto.AuthentificationDto;
import com.ninehub.authentication.entity.User;
import com.ninehub.authentication.service.JwtService;
import com.ninehub.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager; // Qui est charger d'authentifier un user
    private final UserService userService;
    private final JwtService jwtService;

//    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
//        this.authenticationManager = authenticationManager;
//        this.userService = userService;
//        this.jwtService = jwtService;
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@RequestBody User user){
        this.userService.register(user);
        log.info("Account created successful");
    }

    @PostMapping("/activate")
    public void activateAccount(@RequestBody Map<String, String> activation){
        this.userService.activateAccount(activation);
        log.info("Account activated successful");
    }

    @PostMapping("/password/update")
    public void updatePassword(@RequestBody Map<String, String> password){
        this.userService.modifyPassword(password);
        log.info("Password modify successful");
    }

    @PostMapping("/password/new")
    public void newPassword(@RequestBody Map<String, String> password){
        this.userService.newPassword(password);
        log.info("New Password created successful");
    }

    @PostMapping("/refresh/token")
    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> refreshTokenRequest){

        return this.jwtService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/login") // le Map contient le token et la valeur du token
    public Map<String, String> login(@RequestBody AuthentificationDto authentificationDto){
        // Recuperer les information de l'utilisateur
        // Authentifie moi cet utilisateur
        final Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDto.username(), authentificationDto.password())
        );

        // Si l'utilisateur est connecté, on genere le token
        if (authenticate.isAuthenticated()){
            return this.jwtService.generateJwtToken(authentificationDto.username());
        }

        return null;
    }

    @PostMapping("/signout")
    public void signOut(){
        this.jwtService.signOut();
        log.info("You have been logged out successfully");
    }


}
