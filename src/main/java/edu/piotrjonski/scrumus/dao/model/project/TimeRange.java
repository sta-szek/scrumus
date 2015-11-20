package edu.piotrjonski.scrumus.dao.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Represents time range between two dates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TimeRange {

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

}
