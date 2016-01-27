package edu.piotrjonski.scrumus.dao.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents time range between two dates.
 */
@Data
@NoArgsConstructor
@Embeddable
public class TimeRange implements Serializable {

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

}
