package com.mytlogos.enterprisedesktop.background.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 */
public class PagedList<E> extends ArrayList<E> {

    /**
     *Alibi Uid.
     */
    private static final long serialVersionUID = 2377959485581940912L;

    public PagedList() {
    }

    public PagedList(Collection<? extends E> c) {
        super(c == null ? Collections.emptyList() : c);
    }
}
