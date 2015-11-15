package edu.piotrjonski.scrumus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents issue type datastore object.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;
}
