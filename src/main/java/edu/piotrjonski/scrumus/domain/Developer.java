package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Developer {
    private int id;
    private String firstName;
    private String surname;
    private String username;
    private String email;
}
