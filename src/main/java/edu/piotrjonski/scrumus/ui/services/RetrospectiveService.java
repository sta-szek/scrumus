package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.RetrospectiveManager;
import edu.piotrjonski.scrumus.domain.Retrospective;
import lombok.Data;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

@Data
public class RetrospectiveService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private RetrospectiveManager retrospectiveManager;

    private Retrospective currentlyViewedRetrospective;

    @PostConstruct
    public void prepareForView() {
        int currentlyViewedRetrospectiveId = Integer.parseInt(FacesContext.getCurrentInstance()
                                                                          .getExternalContext()
                                                                          .getRequestParameterMap()
                                                                          .get("retrospectiveId"));
        currentlyViewedRetrospective = findById(currentlyViewedRetrospectiveId);
    }

    private Retrospective findById(int retrospectiveId) {
        return retrospectiveManager.findRetrospective(retrospectiveId)
                                   .orElse(null);
    }
}
