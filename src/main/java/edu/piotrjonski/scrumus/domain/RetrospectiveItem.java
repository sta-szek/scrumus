package edu.piotrjonski.scrumus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents retrospective minus object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrospectiveItem {
    private int rate;
    private String description;
    private RetrospectiveItemType retrospectiveItemType;
}
