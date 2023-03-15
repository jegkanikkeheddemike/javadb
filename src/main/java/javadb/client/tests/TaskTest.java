package javadb.client.tests;

import javadb.client.ClientTask;
import tables.User;

public class TaskTest {
    public static void main(String[] args) {
        User myUser = new User("Køb","Stoffer");
        ClientTask.SubmitTask("koebstoffer.info",tables -> {
            tables.users.insert(myUser);
        },Throwable::printStackTrace);
    }
}
