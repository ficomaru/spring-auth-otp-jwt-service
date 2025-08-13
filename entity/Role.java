package com.ninehub.authentication.entity;

import com.ninehub.authentication.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public RoleType getRoleType() {
        return roleType;
    }
}
