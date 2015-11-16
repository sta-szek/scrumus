package edu.piotrjonski.scrumus.model.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents issue type object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class IssueType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;
}
