package com.mytlogos.enterprisedesktop.background.api.model;

/**
 * API Model for TocContent.
 * Enterprise Web API 1.0.2.
 */
public class TocContent {
    private String title;
    private double combiIndex;
    private int totalIndex;
    private Integer partialIndex;

    public TocContent(String title, double combiIndex, int totalIndex, Integer partialIndex) {
        this.setTitle(title);
        this.setCombiIndex(combiIndex);
        this.setTotalIndex(totalIndex);
        this.setPartialIndex(partialIndex);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getCombiIndex() {
        return combiIndex;
    }

    public void setCombiIndex(double combiIndex) {
        this.combiIndex = combiIndex;
    }

    public int getTotalIndex() {
        return totalIndex;
    }

    public void setTotalIndex(int totalIndex) {
        this.totalIndex = totalIndex;
    }

    public Integer getPartialIndex() {
        return partialIndex;
    }

    public void setPartialIndex(Integer partialIndex) {
        this.partialIndex = partialIndex;
    }
}
