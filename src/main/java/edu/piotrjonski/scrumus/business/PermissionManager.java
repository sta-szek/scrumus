package edu.piotrjonski.scrumus.business;


import edu.piotrjonski.scrumus.dao.*;
import edu.piotrjonski.scrumus.domain.Developer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class PermissionManager {

    @Inject
    private DeveloperDAO developerDAO;

    @Inject
    private ScrumMasterDAO scrumMasterDAO;

    @Inject
    private ProductOwnerDAO productOwnerDAO;

    @Inject
    private AdminDAO adminDAO;

    public boolean hasPermission(Developer developer, PermissionType permissionType) {
        try {
            AbstractDAO abstractDAO = getDAO(permissionType);
            //TODO zmienc na UserDAO i uzyc metody existy(int developer Id)
            // bo teraz szuka nam po id developera a nie id encji
            Optional user = abstractDAO.findByKey(developer.getId());
            return user.isPresent();
        } catch (UnknownUserTypeException e) {
            //TODO zaloguj blad
            return false;
        }
    }

    public void grantPermission(Developer user, PermissionType permissionType) {
        //TODO
        //1. pobrac DAO
        //2. zrobic metoda, ktora zwroci nam odpowiedni obiekt np. developer, admin, scrummaster, productowner
        //3. sprobowac dodac go za pomoca DAO
        //4. jak poleci cannot cast exception to jestesmy w lesie i trzeba zrobic ogromengo switcha
    }

    private AbstractDAO getDAO(PermissionType permissionType) throws UnknownUserTypeException {
        switch (permissionType) {
            case DEVELOPER:
                return developerDAO;
            case SCRUM_MASTER:
                return scrumMasterDAO;
            case PRODUCT_OWNER:
                return productOwnerDAO;
            case ADMIN:
                return adminDAO;
            default:
                throw new UnknownUserTypeException("Nieznany rodzaj użytkownika.");
        }
    }


}
