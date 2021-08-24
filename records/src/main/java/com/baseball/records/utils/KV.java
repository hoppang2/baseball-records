package com.baseball.records.utils;

public class KV<K, V> {
    public K key;
    public V value;

    public KV(K k, V v) {
        key = k;
        value = v;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
