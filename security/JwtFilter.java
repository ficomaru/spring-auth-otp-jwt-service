package com.ninehub.authentication.security;

import com.ninehub.authentication.entity.Jwt;
import com.ninehub.authentication.service.JwtService;
import com.ninehub.authentication.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        Jwt tokenInDb = null;
        String username = null;
        boolean isTokenExpired = true;

        // On recupere notre token
        final String requestAuthorization = request.getHeader("Authorization");

        try {
            if (requestAuthorization != null && requestAuthorization.startsWith("Bearer ")) {
                token = requestAuthorization.substring(7);

                // On verifie le token dans la base de donnée
                tokenInDb = this.jwtService.loadTokenByValue(token);

                //On verifie si le token n'as pas expirer
                isTokenExpired = jwtService.isTokenExpired(token);
                username = jwtService.extractUsername(token);
            }

            // Si personne n'est authentifier pour le moment,
            if (!isTokenExpired
                    && tokenInDb.getUser().getEmail().equals(username)
                    && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = userService.loadUserByUsername(username);
                // On crèe un nouveau token
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Je recupere le jwt, je verifie qu'il est valide, et je connais le username correspondant a ce token
                // Je creer une donnée d'aithentification dans le contexte et je passe a spring security
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (UsernameNotFoundException e) {
            logger.warn("Erreur lors du traitement du token JWT : {}", e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // La chaine de filtre: Continue a filtree notre requete et notre reponse
        filterChain.doFilter(request, response);
    }
}
