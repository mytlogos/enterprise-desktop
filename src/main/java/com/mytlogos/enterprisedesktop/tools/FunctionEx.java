package com.mytlogos.enterprisedesktop.tools;

public interface FunctionEx<T, R> {
    R apply(T t) throws Exception;
}
