package com.mytlogos.enterprisedesktop.background.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 */
public class PagedList<E> extends ArrayList<E> {

    public PagedList() {
    }

    public PagedList(Collection<? extends E> c) {
        super(c == null ? Collections.emptyList() : c);
    }
}
