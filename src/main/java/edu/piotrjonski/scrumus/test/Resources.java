package edu.piotrjonski.scrumus.test;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Piotr Joñski on 2015-11-08.
 */
@Singleton
public class Resources {

    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager entityManager;

    @Produces
    public EntityManager entityManager() {
        return entityManager;
    }


}
