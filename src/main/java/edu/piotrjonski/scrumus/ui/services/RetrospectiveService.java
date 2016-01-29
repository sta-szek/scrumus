package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.RetrospectiveManager;
import edu.piotrjonski.scrumus.domain.Retrospective;
import edu.piotrjonski.scrumus.domain.RetrospectiveItem;
import edu.piotrjonski.scrumus.domain.RetrospectiveItemType;
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

    private String itemDescription;

    @PostConstruct
    public void prepareForView() {
        int currentlyViewedRetrospectiveId = Integer.parseInt(FacesContext.getCurrentInstance()
                                                                          .getExternalContext()
                                                                          .getRequestParameterMap()
                                                                          .get("retrospectiveId"));
        currentlyViewedRetrospective = findById(currentlyViewedRetrospectiveId);
    }

    public void addPlus() {
        createItemAndAddToRetrospective(RetrospectiveItemType.PLUS);
    }

    public void addMinus() {
        createItemAndAddToRetrospective(RetrospectiveItemType.MINUS);
    }

    public void refreshRetrospective() {
        currentlyViewedRetrospective = retrospectiveManager.findRetrospective(currentlyViewedRetrospective.getId())
                                                           .get();
    }

    private void createItemAndAddToRetrospective(final RetrospectiveItemType itemType) {
        if (itemDescription == null || itemDescription.isEmpty()) {
            return;
        }
        refreshRetrospective();
        RetrospectiveItem retrospectiveItem = new RetrospectiveItem();
        retrospectiveItem.setDescription(itemDescription);
        retrospectiveItem.setRetrospectiveItemType(itemType);
        currentlyViewedRetrospective.addRetrospectiveItem(retrospectiveItem);
        itemDescription = null;
        retrospectiveManager.update(currentlyViewedRetrospective);
    }

    private Retrospective findById(int retrospectiveId) {
        return retrospectiveManager.findRetrospective(retrospectiveId)
                                   .orElse(null);
    }

}
