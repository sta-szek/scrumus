package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.user.Developer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DeveloperDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Developer developer) {
        entityManager.persist(developer);
    }

    public Developer findByKey(int key) {
        return entityManager.find(Developer.class, key);
    }

    public List<Developer> findAll() {
        return entityManager.createNamedQuery(Developer.FIND_ALL, Developer.class)
                            .getResultList();
    }
}
