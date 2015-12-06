package edu.piotrjonski.scrumus.dao.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents picture object.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "picture")
@NamedQueries({@NamedQuery(name = PictureEntity.FIND_ALL, query = PictureEntity.FIND_ALL_QUERY)})
public class PictureEntity {

    public static final String FIND_ALL = "findAllPictures";
    protected static final String FIND_ALL_QUERY = "SELECT p FROM PictureEntity p";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255, unique = false)
    private String name;

    @Lob
    private byte[] data;
}