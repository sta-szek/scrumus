package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Picture implements Serializable {

    private int id;
    private String name;
    private byte[] data;
}
