package edu.tenchnopolis.homework.messenger.net;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

/**
 * Date: 04.06.17
 *
 * @author olerom
 */
public class NonBlockingServerTest {
    @Test
    public void connectionTest() throws IOException, InterruptedException {
        InputStream input = new FileInputStream("./src/main/resources/portConfiguration.properties");

        Properties properties = new Properties();
        properties.load(input);

        int port = Integer.valueOf(properties.getProperty("port"));

        Socket[] sockets = new Socket[3000];
        for (int i = 0; i < sockets.length; i++) {
            sockets[i] = new Socket("localhost", port);
            System.out.println(i + " " + sockets[i]);
        }
    }

}
