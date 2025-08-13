package com.ninehub.authentication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Code created (user) at...
    private Instant createdAt;
    // Code or user activated at...
    private Instant activatedAt;
    // The code expired at...
    private Instant expiredAt;
    private String code;

    // One code is for only one user
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
