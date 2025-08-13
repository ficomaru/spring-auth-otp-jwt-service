package com.ninehub.authentication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instructions;
    private int order;

    @ManyToOne
    @JoinColumn(name = "recipes_id")
    private Recipes recipes;
}
