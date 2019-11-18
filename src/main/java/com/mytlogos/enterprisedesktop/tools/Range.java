package com.mytlogos.enterprisedesktop.tools;

/**
 *
 */
public class Range {
    private final int from;
    private final int to;

    public Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public boolean inRange(int i) {
        return this.from >= i && this.to <= i;
    }

    public boolean isAfter(int i) {
        return this.to < i;
    }

    public boolean isBefore(int i) {
        return this.from > i;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        if (from != range.from) return false;
        return to == range.to;
    }

    @Override
    public String toString() {
        return "Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
