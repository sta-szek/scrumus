package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents retrospective object.
 */
@Data
@NoArgsConstructor
public class Retrospective implements Serializable {

    private int id;
    private String description;
    private List<RetrospectiveItem> retrospectiveItems = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public void addComment(final Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public void addRetrospectiveItem(RetrospectiveItem retrospectiveItem) {
        retrospectiveItems.add(retrospectiveItem);
    }

    public void removeRetrospectiveItem(RetrospectiveItem retrospectiveItem) {
        retrospectiveItems.remove(retrospectiveItem);
    }

    public List<RetrospectiveItem> getPluses() {
        return retrospectiveItems.stream()
                                 .filter(isPlus())
                                 .collect(Collectors.toList());
    }

    public List<RetrospectiveItem> getMinuses() {
        return retrospectiveItems.stream()
                                 .filter(isPlus())
                                 .collect(Collectors.toList());
    }

    private Predicate<RetrospectiveItem> isPlus() {
        return retrospectiveItem -> retrospectiveItem.getRetrospectiveItemType()
                                                     .equals(RetrospectiveItemType.PLUS);
    }

    private Predicate<RetrospectiveItem> isMinus() {
        return retrospectiveItem -> retrospectiveItem.getRetrospectiveItemType()
                                                     .equals(RetrospectiveItemType.MINUS);
    }
}
