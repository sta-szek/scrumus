package edu.piotrjonski.scrumus.utils;


import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class TestUtils {

    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "scrumus-arquillian-integration-test.war")
                         .addPackages(true, "edu.piotrjonski.scrumus")
                         .addPackages(true, "org.assertj.core")
                         .addPackages(true, "org.mockito")
                         .addPackages(true, "org.objenesis")
                         .addPackages(true, "org.apache.commons.collections4")
                         .addAsResource("META-INF/persistence.xml")
                         .addAsResource("configuration.properties")
                         .as(WebArchive.class);
    }
}
