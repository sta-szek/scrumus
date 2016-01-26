package edu.piotrjonski.scrumus.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class State implements Serializable {
    private int id;
    private String name;
}
