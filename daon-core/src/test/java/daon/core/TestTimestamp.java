package daon.core;

import java.io.IOException;
import java.sql.Timestamp;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestTimestamp {

    private Logger logger = LoggerFactory.getLogger(TestTimestamp.class);

    private boolean test;

    @Test
    public void test() throws IOException, InterruptedException {

        long t = 1481824265967l;

        Timestamp d = new Timestamp(t);
        System.out.println(d.toLocalDateTime().getSecond());


        System.out.println(test);
    }
}
