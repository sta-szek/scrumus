package edu.piotrjonski.scrumus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Represents retrospective minuses datastore object.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrospectiveItem {

    @Column(nullable = false)
    private int rate;

    @Column(length = 256, nullable = false)
    private String description;

    @Enumerated(value = EnumType.STRING)
    private RetrospectiveItemType retrospectiveItemType;
}
