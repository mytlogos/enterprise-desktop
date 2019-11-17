package com.mytlogos.enterprisedesktop.tools;

/**
 *
 */
public interface BiConsumerEx<T, V> {
    void accept(T t, V v) throws Exception;
}
