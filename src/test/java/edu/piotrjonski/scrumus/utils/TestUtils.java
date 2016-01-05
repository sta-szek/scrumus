package edu.piotrjonski.scrumus.utils;


import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {
    private static Logger logger = LoggerFactory.getLogger(TestUtils.class);

    public static WebArchive createDeployment() {
        WebArchive as = ShrinkWrap.create(WebArchive.class, "scrumus-arquillian-integration-test.war")
                                  .addPackages(true, "edu.piotrjonski.scrumus")
                                  .addPackages(true, "org.assertj.core")
                                  .addPackages(true, "org.mockito")
                                  .addPackages(true, "org.objenesis")
                                  .addPackages(true, "org.apache.commons.collections4")
                                  .addAsResource("META-INF/persistence.xml")
                                  .addAsResource("configuration.properties")
                                  .as(WebArchive.class);
        logger.info(as.toString());
        return as;
    }
}
