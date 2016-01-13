package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.PermissionManager;
import edu.piotrjonski.scrumus.domain.Developer;
import lombok.Data;

import javax.inject.Inject;

@Data
public class PermissionService {

    @Inject
    private PermissionManager permissionManager;

    public boolean isAdmin(Developer user) {
        return permissionManager.isAdmin(user);
    }

    public void grantAdminPermission(Developer user) {
        permissionManager.grantAdminPermission(user);
    }

    public void removeAdminPermission(Developer user) {
        permissionManager.removeAdminPermission(user);
    }
}
