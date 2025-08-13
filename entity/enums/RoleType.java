package com.ninehub.authentication.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.ninehub.authentication.entity.enums.PermissionType.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum RoleType {
    // On recupere les permission associer a chaque roles
    USER(
            Set.of(PermissionType.USER_CREATE_AVIS)
    ),

    ADMIN(
            Set.of(
                    PermissionType.ADMIN_CREATE,
                    PermissionType.ADMIN_READ,
                    PermissionType.ADMIN_UPDATE,
                    PermissionType.ADMIN_DELETE,

                    PermissionType.MODERATOR_CREATE,
                    PermissionType.MODERATOR_READ,
                    PermissionType.MODERATOR_UPDATE,
                    PermissionType.MODERATOR_DELETE_AVIS
            )
    ),

    MODERATOR(
            Set.of(
                    PermissionType.MODERATOR_CREATE,
                    PermissionType.MODERATOR_READ,
                    PermissionType.MODERATOR_UPDATE,
                    PermissionType.MODERATOR_DELETE_AVIS
            )
    );

//    CHIEF;

    @Getter
    Set<PermissionType> permissionType;


//    <E> RoleType(Set<E> userCreateAvis) {
//    }

    // Cette methode permet de recupérer les permissions et de lès associer aux roles
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> grantedAuthorities = this.getPermissionType().stream().map(
                permission -> new SimpleGrantedAuthority(permission.name())
        ).collect(Collectors.toList());

        // On ajoute les roles et les permissions pour pouvoir autoriser les actions de l'utilisateur
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
