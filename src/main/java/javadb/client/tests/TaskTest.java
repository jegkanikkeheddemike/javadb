package javadb.client.tests;

import javadb.client.ClientTask;
import tables.User;

public class TaskTest {
    public static void main(String[] args) {
        ClientTask.SubmitTask("koebstoffer.info",tables -> {
            tables.users.insert(new User("Obama","Barack"));
        },Throwable::printStackTrace);
    }
}
