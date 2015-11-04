package edu.piotrjonski.beans;

import edu.piotrjonski.model.Record;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Piotruœ on 2015-11-04.
 */
@Stateful(name = "SessionEJB")
public class SessionBean {


    @PersistenceContext(name = "PostgresDS")
    EntityManager entityManager;

    public SessionBean() {
        Record record = new Record();
        record.setId(10);
        entityManager.persist(record);
    }

}
