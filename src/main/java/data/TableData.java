package data;

import java.io.Serializable;

public abstract class TableData<T extends Serializable> implements Serializable {
    public TableID<T> id;
}
