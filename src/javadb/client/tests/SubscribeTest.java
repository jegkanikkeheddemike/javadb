package javadb.client.tests;

import javadb.client.ClientSubscriber;
import javadb.client.ClientTask;
import tables.User;
import java.util.ArrayList;

public class SubscribeTest {
    public static void main(String[] args) throws InterruptedException {
        ClientSubscriber<ArrayList<User>> usersSubscriber = new ClientSubscriber<>("localhost", tables -> {
            ArrayList<User> foundUsers = new ArrayList<>();
            for (User user : tables.users) {
                foundUsers.add(user);
            }
            return foundUsers;

        }, users -> {
            System.out.println("Found: " + users);
        },error -> {
            System.out.println("Failed to receive users: " + error.getMessage());
        });

        ClientTask.SubmitTask("localhost", tables -> {
            tables.users.insert(new User("Jens", "Karsten"));
        }, error -> {
            System.out.println("Failed to execute task: " + error.getMessage());
        });

        Thread.sleep(1000);

        usersSubscriber.kill();
    }
}
