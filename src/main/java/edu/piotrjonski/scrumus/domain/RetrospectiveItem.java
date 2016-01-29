package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents retrospective minus object.
 */
@Data
@NoArgsConstructor
public class RetrospectiveItem implements Serializable {
    private String description;
    private RetrospectiveItemType retrospectiveItemType;
}
