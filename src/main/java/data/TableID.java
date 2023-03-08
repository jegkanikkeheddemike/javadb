package data;

import java.io.Serializable;

public record TableID<T>(int index, int generation) implements Serializable {
}
