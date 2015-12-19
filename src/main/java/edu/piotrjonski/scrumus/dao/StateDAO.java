package edu.piotrjonski.scrumus.dao;

import edu.piotrjonski.scrumus.dao.model.project.StateEntity;
import edu.piotrjonski.scrumus.domain.State;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class StateDAO extends AbstractDAO<StateEntity, State> {

    public StateDAO() {
        this(StateEntity.class);
    }

    private StateDAO(final Class entityClass) {
        super(entityClass);
    }

    @Override
    protected StateEntity mapToDatabaseModel(final State domainModel) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setId(domainModel.getId());
        stateEntity.setName(domainModel.getName());
        return stateEntity;
    }

    @Override
    protected State mapToDomainModel(final StateEntity dbModel) {
        State state = new State();
        state.setId(dbModel.getId());
        state.setName(dbModel.getName());
        return state;
    }

    @Override
    protected Query getFindAllQuery() {
        return entityManager.createNamedQuery(StateEntity.FIND_ALL, StateEntity.class);
    }

}
