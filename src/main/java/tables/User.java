package tables;

import java.io.Serializable;

public record User(String username, String realName) implements Serializable {
}
