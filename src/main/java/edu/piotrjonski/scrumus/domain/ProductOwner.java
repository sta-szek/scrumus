package edu.piotrjonski.scrumus.domain;

import edu.piotrjonski.scrumus.business.Permissionable;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProductOwner implements Permissionable {
    private int id;
    private Developer developer;
    private Project project;
}
