package com.ninehub.authentication.controller;

import com.ninehub.authentication.dto.AuthentificationDto;
import com.ninehub.authentication.entity.User;
import com.ninehub.authentication.service.JwtService;
import com.ninehub.authentication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name="Authentication", description = "Endpoint for authentication and token management")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager; // Qui est charger d'authentifier un user
    private final UserService userService;
    private final JwtService jwtService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    @Operation(summary = "User register", description = "Permit to user to create an account")
    public ResponseEntity<User> register(@RequestBody User user){
        User savedUser = this.userService.register(user);
        log.info("Account created successful");
        return ResponseEntity
                .created(URI.create("/users/" + savedUser.getId()))
                .body(savedUser);
    }

    @PostMapping("/activate")
    @Operation(summary = "Activate user account", description = "Otp code entered by user to activate his/her account")
    public void activateAccount(@RequestBody Map<String, String> activation){
        this.userService.activateAccount(activation);
        log.info("Account activated successful");
    }

    @PostMapping("/password/update")
    @Operation(summary = "Update password", description = "Update user password")
    public void updatePassword(@RequestBody Map<String, String> password){
        this.userService.modifyPassword(password);
        log.info("Password modify successful");
    }

    @PostMapping("/password/new")
    @Operation(summary = "Create password", description = "Create a new password")
    public void newPassword(@RequestBody Map<String, String> password){
        this.userService.newPassword(password);
        log.info("New Password created successful");
    }

    @PostMapping("/refresh/token")
    @Operation(summary = "Refresh token")
    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> refreshTokenRequest){

        return this.jwtService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/login") // le Map contient le token et la valeur du token
    @Operation(summary = "User login", description = "Permit to user to get conneted on his/her account")
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
    @Operation(summary="User sign out")
    public void signOut(){
        this.jwtService.signOut();
        log.info("You have been logged out successfully");
    }


}
