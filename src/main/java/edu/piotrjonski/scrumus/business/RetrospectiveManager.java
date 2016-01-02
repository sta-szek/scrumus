package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.RetrospectiveDAO;
import edu.piotrjonski.scrumus.dao.SprintDAO;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.RetrospectiveItem;
import edu.piotrjonski.scrumus.domain.Sprint;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class RetrospectiveManager {

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    @Inject
    private SprintDAO sprintDAO;

    public Optional<Retrospective> createRetrospectiveForSprint(Retrospective retrospective, Sprint sprint) throws AlreadyExistException {
        if (retrospectiveExist(retrospective)) {
            throw new AlreadyExistException("Retrospektywa już istnieje");
        } else if (sprintHasRetrospective(sprint)) {
            throw new AlreadyExistException("Sprint ma już swoją retrospektywę");
        }
        return createRetrospectiveAndUpdateSprint(retrospective, sprint);
    }

    public Optional<Retrospective> findRetrospective(int retrospectiveId) {
        return retrospectiveDAO.findById(retrospectiveId);
    }

    public void addRetrospectiveItemToRetrospective(RetrospectiveItem retrospectiveItem, Retrospective retrospective)
            throws NotExistException {
        if (!retrospectiveExist(retrospective)) {
            throw new NotExistException("Retrospektywa nie istnieje");
        }
        retrospective.addRetrospectiveItem(retrospectiveItem);
        retrospectiveDAO.saveOrUpdate(retrospective);
    }

    public void removeRetrospectiveItemFromRetrospective(RetrospectiveItem retrospectiveItem, Retrospective retrospective)
            throws NotExistException {
        if (!retrospectiveExist(retrospective)) {
            throw new NotExistException("Retrospektywa nie istnieje");
        }
        retrospective.removeRetrospectiveItem(retrospectiveItem);
        retrospectiveDAO.saveOrUpdate(retrospective);
    }

    private Optional<Retrospective> createRetrospectiveAndUpdateSprint(final Retrospective retrospective, final Sprint sprint) {
        Optional<Retrospective> savedRetrospective = retrospectiveDAO.saveOrUpdate(retrospective);
        if (savedRetrospective.isPresent()) {
            sprint.setRetrospectiveId(savedRetrospective.get()
                                                        .getId());
            sprintDAO.saveOrUpdate(sprint);
        }
        return savedRetrospective;
    }

    private boolean sprintHasRetrospective(final Sprint sprint) {
        return sprintDAO.hasRetrospective(sprint.getId());
    }

    private boolean retrospectiveExist(Retrospective retrospective) {
        return retrospectiveDAO.exist(retrospective.getId());
    }
}
