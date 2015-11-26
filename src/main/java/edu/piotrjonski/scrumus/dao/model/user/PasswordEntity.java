package edu.piotrjonski.scrumus.dao.model.user;

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
@Table(name = "password")
public class PasswordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1024, unique = false)
    private String password;

    @OneToOne
    private DeveloperEntity developerEntity;
}
