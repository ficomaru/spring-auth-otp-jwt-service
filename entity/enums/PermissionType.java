package com.ninehub.authentication.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PermissionType {
    ADMIN_CREATE,
    ADMIN_READ,
    ADMIN_UPDATE,
    ADMIN_DELETE,

    MODERATOR_CREATE,
    MODERATOR_READ,
    MODERATOR_UPDATE,
    MODERATOR_DELETE_AVIS,

    USER_CREATE_AVIS;

    // Champ pour retourner les libeles des permissions
    @Getter
    private String permission;

}
