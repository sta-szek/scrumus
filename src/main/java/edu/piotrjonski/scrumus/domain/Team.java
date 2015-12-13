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

    public void addDeveloper(Developer developer) {
        developers.add(developer);
    }

    public void removeDeveloper(Developer developer) {
        developers.remove(developer);
    }

    public void addProject(Project project) {projects.add(project);}

    public void removeProject(Project project) {projects.remove(project);}
}
