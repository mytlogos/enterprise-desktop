package com.mytlogos.enterprisedesktop.background;

import java.util.Collection;

public interface ClientConsumer<T> {
    Class<T> getType();

    void consume(Collection<T> data);
}
