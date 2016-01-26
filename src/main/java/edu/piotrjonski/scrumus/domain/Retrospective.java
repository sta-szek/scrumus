package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
}
