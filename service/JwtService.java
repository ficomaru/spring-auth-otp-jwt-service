package com.ninehub.authentication.service;

import com.ninehub.authentication.entity.Jwt;
import com.ninehub.authentication.entity.RefreshToken;
import com.ninehub.authentication.entity.User;
import com.ninehub.authentication.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class JwtService {
    public static final String BEARER = "bearer";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String INVALID_TOKEN = "Invalid token";
    private final String ENCRYPTION_KEY = "37493f167d76e8e542b5950ab5053cfec23c092c3a7bbf3a910664317088c4fb";
    private final UserService userService;
    private final JwtRepository jwtRepository;

    public Jwt loadTokenByValue(String value) {
        return this.jwtRepository.findByValue(value)
                .orElseThrow(()-> new UsernameNotFoundException("Unknown token"));
    }

    // Le type du token est une chain de caractère(type de retour)
    public Map<String, String> generateJwtToken(String username){
        // On génère le token a partir du username
        // On cherche le user dans la db
        User user = (User) this.userService.loadUserByUsername(username);
        this.disableToken(user);
        Map<String, String> jwtMap = new java.util.HashMap<>(this.generateJwt(user));

        // Géneration de mon refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshTokenValue(UUID.randomUUID().toString())
                .isExpired(false)
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusMillis(20 * 60 * 1000))
                .build();

        // On stocke dans la bd
        Jwt jwt = Jwt.builder()
                .value(jwtMap.get(BEARER))
                .deactivate(false)
                .expired(false)
                .user(user)
                .refreshToken(refreshToken)
                .build();
        this.jwtRepository.save(jwt);
        jwtMap.put(REFRESH_TOKEN, refreshToken.getRefreshTokenValue());
        return jwtMap;
    }
    
    private void disableToken(User user){
        List<Jwt> jwtList = this.jwtRepository.findByEmail(user.getEmail()).peek(
                jwt -> {
                    jwt.setDeactivate(true);
                    jwt.setExpired(true);
                }
        ).collect(Collectors.toList());

        this.jwtRepository.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        // Pour verifier que le token n'a pas expirer, on verifie la date d'expiration et la date actuel
        // Je cherche les information en fonction des claims qui ont servir a la creation du token
        Date expirationDate = this.getClaim(token, Claims::getExpiration);;
        return expirationDate.before(new Date());
    }

    // Cette fonction permet de tout recuperer(le token, username, password, etc)
    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // Retourne le jwt qui a été générer
    private Map<String, String> generateJwt(User user) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 15 * 60 * 1000; // le Jwt dure une 15 minutes

        // Information a passer dans les claims
        Map<String, Object> claims = Map.of(
                "firstName", user.getFirstName(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getEmail()
        );

        String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(user.getEmail())// Personne pour laquelle on genere le token
                .setClaims(claims)// Information sur l'utilisateur qu'on veut envoyer(transmettre)
                .signWith(getKey(), SignatureAlgorithm.HS256) // On definie l'algorithme de signature
                .compact();// compact permet d'avoir tout ca en string
        return Map.of(BEARER, bearer);
    }

    private Key getKey(){
        // On genere des bytes pour pouvoir avoir notre clé
        // decode moi une chaine de caractere qui vas me servir de clé secrete
        byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder); // clé generer
    }

    public void signOut() {
        // On recupere l'utilisateur connecter
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findByUserAndDeactivateAndExpired(user.getEmail(), false, false)
                .orElseThrow(()-> new UsernameNotFoundException("Any user match with this token"));
        jwt.setExpired(true);
        jwt.setDeactivate(true);
        jwtRepository.save(jwt);
    }

    // Cette methode permet de supprimer les tokens de la bd a une periode donnée. Parce que un utilisateur peut avoir plusieur token
    @Scheduled(cron = "* */30 * * * *")
    public void removeUselessJwt(){
        log.info("Suppression des tokens a {}", Instant.now());
        this.jwtRepository.deleteAllByExpiredAndDeactivate(true, true);
    }

    public Map<String, String> refreshToken(Map<String, String> refreshTokenRequest) {
        Jwt jwtRefresh = this.jwtRepository.findByRefreshToken(refreshTokenRequest.get(REFRESH_TOKEN))
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_TOKEN));

        if (jwtRefresh.getRefreshToken().isExpired()
                || jwtRefresh.getRefreshToken().getExpiredAt().isBefore(Instant.now())){
            throw new RuntimeException(INVALID_TOKEN);
        }
        Map<String, String> tokens = this.generateJwtToken(jwtRefresh.getUser().getUsername());
        this.disableToken(jwtRefresh.getUser());
        return tokens;
    }
}
