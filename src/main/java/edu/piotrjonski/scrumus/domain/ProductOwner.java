package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class ProductOwner implements Serializable {
    private int id;
    private Developer developer;
    private Project project;
}
