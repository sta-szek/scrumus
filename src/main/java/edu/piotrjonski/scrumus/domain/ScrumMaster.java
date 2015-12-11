package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents scrum master object.
 */
@Data
@NoArgsConstructor
public class ScrumMaster {
    private int id;
    private Developer developer;
    private List<Team> teams = new ArrayList<>();
}
