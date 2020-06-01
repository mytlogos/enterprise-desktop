package com.mytlogos.enterprisedesktop.controller;

import com.mytlogos.enterprisedesktop.tools.AllSortings;
import com.mytlogos.enterprisedesktop.tools.Sorting;

/**
 *
 */
public enum MediumSorting implements Sorting, MainController.DisplayValue {
    TITLE_ASC("Title Asc", AllSortings.TITLE_AZ),
    TITLE_DESC("Title Desc", AllSortings.TITLE_ZA),
    MEDIUM_ASC("Medium Asc", AllSortings.MEDIUM),
    MEDIUM_DESC("Medium Desc", AllSortings.MEDIUM_REVERSE),
    LAST_UPDATE_ASC("Last Update Asc", AllSortings.LAST_UPDATE_ASC),
    LAST_UPDATE_DESC("Last Update Desc", AllSortings.LAST_UPDATE_DESC),
    EPISODES_ASC("Episodes Asc", AllSortings.NUMBER_EPISODE_ASC),
    EPISODES_DESC("Episodes Desc", AllSortings.NUMBER_EPISODE_DESC),
    NUMBER_EPISODE_READ_ASC("Episodes Read Asc", AllSortings.NUMBER_EPISODE_READ_ASC),
    NUMBER_EPISODE_READ_DESC("Episodes Read Desc", AllSortings.NUMBER_EPISODE_READ_DESC),
    NUMBER_EPISODE_UNREAD_ASC("Episodes UnRead Asc", AllSortings.NUMBER_EPISODE_UNREAD_ASC),
    NUMBER_EPISODE_UNREAD_DESC("Episodes UnRead Desc", AllSortings.NUMBER_EPISODE_UNREAD_DESC);;
    private final int value;
    private final String displayValue;

    MediumSorting(String displayValue, Sorting sortings) {
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
