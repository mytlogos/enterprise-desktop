package com.mytlogos.enterprisedesktop.tools;

/**
 *
 */
public interface TriConsumerEx<T, T1, T2> {
    void accept(T t, T1 t1, T2 t2) throws Exception;
}
