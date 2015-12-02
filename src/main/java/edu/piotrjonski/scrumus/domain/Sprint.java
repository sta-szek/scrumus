package edu.piotrjonski.scrumus.domain;

import edu.piotrjonski.scrumus.dao.model.project.TimeRange;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents sprint datastore object.
 */
@Data
@NoArgsConstructor
public class Sprint {
    private int id;
    private String name;
    private String definitionOfDone;
    private TimeRange timeRange;
    private int retrospectiveId;

}
