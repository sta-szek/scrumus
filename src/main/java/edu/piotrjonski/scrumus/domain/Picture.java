package edu.piotrjonski.scrumus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture {

    private int id;
    private String name;
    private byte[] data;
}
