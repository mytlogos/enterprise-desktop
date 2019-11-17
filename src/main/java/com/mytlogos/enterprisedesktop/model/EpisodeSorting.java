package com.mytlogos.enterprisedesktop.model;

import com.mytlogos.enterprisedesktop.controller.MainController;
import com.mytlogos.enterprisedesktop.tools.AllSortings;
import com.mytlogos.enterprisedesktop.tools.Sorting;

/**
 *
 */
public enum EpisodeSorting implements Sorting, MainController.DisplayValue {
    INDEX_ASC("Index", AllSortings.INDEX_ASC),
    INDEX_DESC("Index Desc", AllSortings.INDEX_DESC),
    DATE_ASC("Date Asc", AllSortings.DATE_ASC),
    DATE_DESC("Date Desc", AllSortings.DATE_DESC),
    LAST_UPDATE_DESC("Last Update Desc", AllSortings.LAST_UPDATE_DESC),
    LAST_UPDATE_ASC("Last Update Asc", AllSortings.LAST_UPDATE_ASC),
    ;

    private final int value;
    private final String displayValue;

    EpisodeSorting(String displayValue, Sorting sortings) {
        this.displayValue = displayValue;
        this.value = sortings.getSortValue();
    }

    @Override
    public int getSortValue() {
        return this.value;
    }

    @Override
    public String getDisplayValue() {
        return this.displayValue;
    }
}
