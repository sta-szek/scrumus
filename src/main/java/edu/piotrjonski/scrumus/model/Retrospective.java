package edu.piotrjonski.scrumus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents retrospective datastore object.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Retrospective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 4096, nullable = true)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<RetrospectiveItem> retrospectiveItems = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}
