package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Developer implements Serializable {
    private int id;
    private String firstName;
    private String surname;
    private String username;
    private String email;

    public String getFullName() {
        return firstName + " " + surname;
    }
}
