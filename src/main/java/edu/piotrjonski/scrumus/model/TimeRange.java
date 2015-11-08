package edu.piotrjonski.scrumus.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Represents time range between two dates.
 */
@Embeddable
public class TimeRange {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TimeRange() {
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("startDate", startDate)
                                        .append("endDate", endDate)
                                        .toString();
    }
}
