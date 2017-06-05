package edu.technopolis.homework.messenger.teacher.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Date: 25.04.17
 *
 * @author olerom
 */
public class FastScanner implements Closeable {
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public String readLine() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
    }
}
