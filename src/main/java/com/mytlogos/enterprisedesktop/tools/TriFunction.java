package com.mytlogos.enterprisedesktop.tools;

/**
 *
 */
public interface TriFunction<T1, T2, T3, R> {
    R apply(T1 value, T2 value1, T3 value2);
}
