package javadb;

import data.DataTask;
import data.Subscriber;
import tables.Log;
import tables.Tables;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class JavaDB {
    private final Tables tables;


    public JavaDB() {
        tables = new Tables();
    }

    public JavaDB(String filePath) throws IOException, ClassNotFoundException {
        byte[] bin = Files.readAllBytes(Paths.get(filePath));
        ByteArrayInputStream bis = new ByteArrayInputStream(bin);
        ObjectInputStream ois = new ObjectInputStream(bis);
        tables = (Tables) ois.readObject();
    }

    private LinkedBlockingQueue<DataTask> tasks = new LinkedBlockingQueue<>();

    private LinkedList<Subscriber> subscribers = new LinkedList<>();

    public void submitTask(DataTask task) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void submitSubscriber(Subscriber subscriber) {
        synchronized (tables) {
            try {
                subscriber.update(tables);
                synchronized (subscribers) {
                    subscribers.add(subscriber);
                }
            } catch (ClassCastException e) {
                submitLog(Log.LogLevel.ERROR, "Failed to execute filter. Likely incompatible table versions. "+ e.toString());
            }
        }
    }


    private boolean running;

    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Already running");
        }
        running = true;
        new Thread(this::run).start();
    }

    private void run() {
        while (running) {
            DataTask task;
            try {
                task = tasks.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            synchronized (tables) {
                try {
                    task.accept(tables);
                } catch (Exception e) {
                    submitLog(Log.LogLevel.ERROR, "Failed to execute task: " + e);
                }

                synchronized (subscribers) {
                    LinkedList<Subscriber> failed = new LinkedList<>();

                    for (Subscriber subscriber : subscribers) {
                        try {
                            boolean success = subscriber.update(tables);
                            if (!success) {
                                failed.add(subscriber);
                            }
                        } catch (Exception e) {
                            failed.add(subscriber);
                            submitLog(Log.LogLevel.ERROR, "Failed to execute filter on nonfirst attempt: " + e);
                        }

                    }
                    for (Subscriber failedSub : failed) {
                        subscribers.remove(failedSub);
                    }
                }
            }
            if (tasks.isEmpty()) {
                //When it is not busy, then block and save the tables to disk
                saveToDisk();
            }
        }
    }

    public static String getSaveDir() {
        return System.getProperty("user.home") + "/javaDB/";
    }
    public static String getTablesSavePath() {
        return getSaveDir() + "tables";
    }
    private void saveToDisk() {
        String filepath = getSaveDir();
        try {
            Files.createDirectories(Path.of(filepath));
        } catch (IOException e) {
            submitLog(Log.LogLevel.ERROR,"Failed to create javaDB directory. " + e);
            return;
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(tables);
            byte[] tablesBin = bos.toByteArray();

            Files.write(Path.of( getTablesSavePath()), tablesBin);
        } catch (IOException e) {
            submitLog(Log.LogLevel.ERROR, e.toString());
            return;
        }
        submitLog(Log.LogLevel.INFO,"successfully wrote table updates to disk");
    }
    public void pubSubmitLog(Log.LogLevel logLevel, String message) {
        synchronized (tables) {
            submitLog(logLevel,message);
        }
    }

    private void submitLog(Log.LogLevel logLevel, String message) {
        Log log = new Log(logLevel, message);
        tables.logs.insert(log);
        System.out.println(log);
    }
}