package edu.piotrjonski.scrumus.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents password object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1024, unique = false)
    private String password;

    @OneToOne
    private Developer developer;
}
