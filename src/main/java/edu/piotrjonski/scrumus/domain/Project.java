package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Project {
    private String key;
    private String name;
    private String description;
    private String definitionOfDone;
    private LocalDateTime creationDate = LocalDateTime.now();

}
