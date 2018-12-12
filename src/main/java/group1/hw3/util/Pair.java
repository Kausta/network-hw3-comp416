package group1.hw3.util;

import java.util.Objects;
import java.util.function.BiFunction;

public class Pair<Key, Value> {
    private final Key key;
    private final Value value;

    public Pair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) &&
                Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    public <K2, V2> Pair<K2, V2> map(BiFunction<Key, Value, Pair<K2, V2>> mapFn) {
        return mapFn.apply(key, value);
    }
}

