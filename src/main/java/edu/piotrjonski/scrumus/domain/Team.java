package edu.piotrjonski.scrumus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private int id;
    private String name;
    private List<Project> projects;
}
