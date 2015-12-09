package edu.piotrjonski.scrumus.domain;

import edu.piotrjonski.scrumus.business.Permissionable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Developer implements Permissionable {
    private int id;
    private String firstName;
    private String surname;
    private String username;
    private String email;
}
