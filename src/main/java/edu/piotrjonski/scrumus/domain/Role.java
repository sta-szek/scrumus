package edu.piotrjonski.scrumus.domain;

import edu.piotrjonski.scrumus.dao.model.security.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents password object.
 */
@Data
@NoArgsConstructor
public class Role {
    private int id;
    private RoleType roleType;
    private List<Developer> developers = new ArrayList<>();

    public void addDeveloper(Developer developer) {
        developers.add(developer);
    }

    public void removeDeveloper(Developer developer) {
        developers.remove(developer);
    }
}
