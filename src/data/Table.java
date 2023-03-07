package data;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Table<T extends Serializable> implements Iterable<T>,Serializable {
    int generation = 0;
    Stack<Integer> freeIndexes = new Stack<>();
    ArrayList<T> content = new ArrayList<>();

    public TableID<T> insert(T value) {
        if (freeIndexes.empty()) {
            TableID<T> id = new TableID<T>(content.size(), generation);
            content.add(value);
            return id;
        }

        int index = freeIndexes.pop();
        generation += 1;
        TableID<T> id = new TableID<>(index, generation);
        content.set(index, value);
        return id;
    }

    public Stream<T> stream() {
        return content.stream().filter(Objects::nonNull);
    }

    @Override
    public String toString() {
        return content.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int iteratorIndex = 0;
            @Override
            public boolean hasNext() {
                while (iteratorIndex < content.size() && content.get(iteratorIndex) == null) {
                    iteratorIndex +=1;
                }
                return iteratorIndex < content.size();
            }

            @Override
            public T next() {
                T next =  content.get(iteratorIndex);
                iteratorIndex+=1;
                return next;
            }
        };
    }
}
