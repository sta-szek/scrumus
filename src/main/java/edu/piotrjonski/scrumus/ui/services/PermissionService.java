package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.PermissionManager;
import edu.piotrjonski.scrumus.domain.Developer;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Data
@RequestScoped
@Named
public class PermissionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
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
