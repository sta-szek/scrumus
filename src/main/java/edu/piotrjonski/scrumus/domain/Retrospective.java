package edu.piotrjonski.scrumus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents retrospective object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Retrospective {

    private int id;
    private String description;
    private List<RetrospectiveItem> retrospectiveItems = new ArrayList<>();
    private List<Comment> commentEntities = new ArrayList<>();
}
