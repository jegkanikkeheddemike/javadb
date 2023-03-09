package javadb.client.tests;

import javadb.client.ClientTask;

public class ShutdownTest {
    public static void main(String[] args) {
        ClientTask.SubmitTask("koebstoffer.info", tables -> {
            System.exit(0);
        }, Throwable::printStackTrace);
    }
}
