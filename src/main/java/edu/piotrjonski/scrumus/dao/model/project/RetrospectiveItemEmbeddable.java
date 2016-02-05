package edu.piotrjonski.scrumus.dao.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Represents retrospective minus object.
 */
@Data
@NoArgsConstructor
@Embeddable
public class RetrospectiveItemEmbeddable {

    @Column(length = 256, nullable = false)
    private String description;

    @Enumerated(value = EnumType.STRING)
    private RetrospectiveItemTypeEnum retrospectiveItemTypeEnum;
}
