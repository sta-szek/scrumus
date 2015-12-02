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
@NamedQueries({@NamedQuery(name = PictureEntity.FIND_ALL, query = PictureEntity.FIND_ALL_QUERY),
               @NamedQuery(name = PictureEntity.DELETE_BY_ID, query = PictureEntity.DELETE_BY_ID_QUERY)})
public class PictureEntity {

    public static final String FIND_ALL = "findAllPictures";
    public static final String DELETE_BY_ID = "deletePictureById";
    public static final String ID = "id";

    protected static final String FIND_ALL_QUERY = "SELECT p FROM PictureEntity p";
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM PictureEntity p WHERE p.id=:" + ID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255, unique = false)
    private String name;

    @Lob
    private byte[] data;
}