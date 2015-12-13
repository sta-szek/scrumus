package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Team {
    private int id;
    private String name;
    private List<Project> projects = new ArrayList<>();
    private List<Developer> developers = new ArrayList<>();

    public void addUserToTeam(Developer developer) {
        developers.add(developer);
    }

    public void removeUserFromTeam(Developer developer) {
        developers.remove(developer);
    }
}
