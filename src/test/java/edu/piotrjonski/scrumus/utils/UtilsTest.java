package edu.piotrjonski.scrumus.utils;


import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class UtilsTest {

    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "scrumus-arquillian-integration-test.war")
                         .addPackages(true, "edu.piotrjonski")
                         .addPackages(true, "org.assertj.core")
                         .addPackages(true, "org.apache.commons.collections4")
                         .addAsResource("META-INF/persistence.xml")
                         .as(WebArchive.class);
    }
}
