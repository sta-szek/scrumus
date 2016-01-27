package edu.piotrjonski.scrumus.ui.services;

import lombok.Data;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

@Data
public class AgileService implements Serializable {

    @Inject
    private transient Logger logger;


}
