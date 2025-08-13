package com.ninehub.authentication.repository;

import com.ninehub.authentication.entity.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByValue(String token);

    @Query("FROM Jwt j WHERE j.user.email = :email AND j.deactivate = :deactivate AND j.expired = :expired")
    Optional<Jwt> findByUserAndDeactivateAndExpired(String email, boolean deactivate, boolean expired);

    @Query("FROM Jwt j WHERE j.user.email = :email")
    Stream<Jwt> findByEmail(String email);

    @Query("FROM Jwt j WHERE j.refreshToken.refreshTokenValue = :refreshTokenValue")
    Optional<Jwt> findByRefreshToken(String refreshTokenValue);

    void deleteAllByExpiredAndDeactivate(boolean expired, boolean deactivate);
}
