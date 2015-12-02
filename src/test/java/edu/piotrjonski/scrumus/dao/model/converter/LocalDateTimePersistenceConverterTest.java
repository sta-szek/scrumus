package edu.piotrjonski.scrumus.dao.model.converter;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimePersistenceConverterTest {

    private LocalDateTimePersistenceConverter localDateTimePersistenceConverter = new LocalDateTimePersistenceConverter();

    @Test
    public void shouldReturnNullWhenLocalDateTimeIsNull() {
        // given
        LocalDateTime localDateTime = null;

        // when
        Timestamp result = localDateTimePersistenceConverter.convertToDatabaseColumn(localDateTime);

        // then
        assertThat(result).isNull();
    }

    @Test
    public void shouldReturnNullWhenTimestampIsNull() {
        // given
        Timestamp timestamp = null;

        // when
        LocalDateTime result = localDateTimePersistenceConverter.convertToEntityAttribute(timestamp);

        // then
        assertThat(result).isNull();
    }

    @Test
    public void shouldConvertToTimestamp() {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        long expectedSeconds = localDateTime.atZone(zoneId)
                                            .toInstant()
                                            .toEpochMilli();

        // when
        Timestamp result = localDateTimePersistenceConverter.convertToDatabaseColumn(localDateTime);

        // then
        assertThat(result.getTime()).isEqualTo(expectedSeconds);
    }

    @Test
    public void shouldConvertToLocalDateTime() {
        // given
        long millis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(millis);
        ZoneId zoneId = ZoneId.systemDefault();


        // when
        LocalDateTime localDateTime = localDateTimePersistenceConverter.convertToEntityAttribute(timestamp);
        long result = localDateTime.atZone(zoneId)
                                   .toInstant()
                                   .toEpochMilli();

        // then
        assertThat(result).isEqualTo(millis);
    }
}