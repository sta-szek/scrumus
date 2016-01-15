package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents scrum master object.
 */
@Data
@NoArgsConstructor
public class ScrumMaster implements Serializable {
    private int id;
    private Developer developer;
    private List<Team> teams = new ArrayList<>();

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public boolean hasAnyTeam() {
        return !teams.isEmpty();
    }
}
