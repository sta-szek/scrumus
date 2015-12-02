package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents retrospective minus object.
 */
@Data
@NoArgsConstructor
public class RetrospectiveItem {
    private int rate;
    private String description;
    private RetrospectiveItemType retrospectiveItemType;
}
