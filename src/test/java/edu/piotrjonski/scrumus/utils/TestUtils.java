package edu.piotrjonski.scrumus.utils;


import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class TestUtils {

    public static WebArchive createDeployment() {
        WebArchive as = ShrinkWrap.create(WebArchive.class, "scrumus-arquillian-integration-test.war")
                                  .addPackages(true, "edu.piotrjonski.scrumus")
                                  .addPackages(true, "org.assertj.core")
                                  .addPackages(true, "org.mockito")
                                  .addPackages(true, "org.objenesis")
                                  .addPackages(true, "org.apache.commons.collections4")
                                  .addAsResource("META-INF/persistence.xml")

                                  .addAsResource("configuration.properties")
//                                  .addAsResource("META-INF/configuration.properties")
//                                  .addAsWebResource("META-INF/configuration.properties")
//                                  .addAsWebResource("configuration.properties")
//                                  .addAsWebInfResource("META-INF/configuration.properties")
//                                  .addAsWebInfResource("configuration.properties")
//                                  .addAsManifestResource("configuration.properties")
//                                  .addAsManifestResource("META-INF/configuration.properties")

//                                  .addAsResource("resources/configuration.properties")
//                                  .addAsResource("WEB-INF/configuration.properties")
//                                  .addAsWebResource("WEB-INF/configuration.properties")
//                                  .addAsWebResource("resources/configuration.properties")
//                                  .addAsWebInfResource("WEB-INF/configuration.properties")
//                                  .addAsWebInfResource("resources/configuration.properties")
//                                  .addAsManifestResource("WEB-INF/configuration.properties")
//                                  .addAsManifestResource("resources/configuration.properties")
                                  .as(WebArchive.class);
        System.out.println(as.toString(true));
        return as;
    }
}
