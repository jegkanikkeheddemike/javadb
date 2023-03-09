package data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class TableID<T> implements Serializable {
    public final int index;
    public final int generation;

    String tableName;

    public TableID(int index, int generation, String tableName) {
        this.index = index;
        this.generation = generation;
        this.tableName = tableName;
    }
}
