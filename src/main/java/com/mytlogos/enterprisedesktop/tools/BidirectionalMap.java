package com.mytlogos.enterprisedesktop.tools;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class BidirectionalMap<K, V> {
    private final Map<K, V> keyValueMap = new HashMap<>();
    private final Map<V, K> valueKeyMap = new HashMap<>();

    public V getValue(K k) {
        return this.keyValueMap.get(k);
    }

    public K getKey(V v) {
        return this.valueKeyMap.get(v);
    }

    public boolean containsKey(K k) {
        return this.keyValueMap.containsKey(k);
    }

    public boolean containsValue(V v) {
        return this.valueKeyMap.containsKey(v);
    }

    public void put(K k, V v) {
        this.keyValueMap.put(k, v);
        this.valueKeyMap.put(v, k);
    }

    public void removeKey(K k) {
        final V remove = this.keyValueMap.remove(k);
        this.valueKeyMap.remove(remove);
    }

    public void removeValue(V v) {
        final K remove = this.valueKeyMap.remove(v);
        this.keyValueMap.remove(remove);
    }
}
