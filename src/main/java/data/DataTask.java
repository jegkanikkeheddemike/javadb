package data;

import tables.Tables;

import java.io.Serializable;
import java.util.function.Consumer;

public interface DataTask extends Consumer<Tables>, Serializable {
}
