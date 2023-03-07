package javadb.client.tests;

import javadb.client.ClientSubscriber;
import javadb.client.ClientTask;
import tables.User;
import java.util.ArrayList;
import java.util.Arrays;

public class SubscribeTest {
    public static void main(String[] args) throws InterruptedException {
        ClientSubscriber<User[]> usersSubscriber = new ClientSubscriber<>("koebstoffer.info", tables -> {
            return tables.users.stream().toList().toArray(new User[0]);
        }, users -> {
            System.out.println("Updated users:");
            for (User user : users) {
                System.out.println(user);
            }
        },error -> {
            System.out.println("Failed to receive users: " + error.getMessage());
        });
    }
}
