package edu.piotrjonski.scrumus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProductOwner {
    private int id;
    private Developer developer;
    private Project project;
}
