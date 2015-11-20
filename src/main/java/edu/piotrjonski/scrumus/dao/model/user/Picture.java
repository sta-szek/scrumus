package edu.piotrjonski.scrumus.dao.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents picture object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255, unique = false)
    private String name;

    @Lob
    private byte[] data;
}