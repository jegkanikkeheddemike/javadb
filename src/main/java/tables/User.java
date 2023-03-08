package tables;

import data.TableData;
import data.TableID;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class User extends TableData<User> {
    public String username;
    public String realName;

    public User(String username, String realName) {
        this.username = username;
        this.realName = realName;
    }
}
