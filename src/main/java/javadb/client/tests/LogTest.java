package javadb.client.tests;

import javadb.client.ClientSubscriber;
import tables.Log;

public class LogTest {
    public static void main(String[] args) throws InterruptedException {
        ClientSubscriber<Log[]> logSubscriber = new ClientSubscriber<>("koebstoffer.info", tables -> {
            return tables.logs.stream().toList().toArray(new Log[0]);
        }, logs -> {
            for (Log log : logs) {
                System.out.println(log);
            }
        }, Throwable::printStackTrace);

        Thread.sleep(1000);

        logSubscriber.kill();
    }
}
