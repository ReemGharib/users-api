package com.users.api.support;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Reem Gharib
 */
public class CustomPageable implements Pageable {

    private final int limit;
    private final int offset;
    private final Sort sort;

    /**
     * Constructor custom pageable
     *
     * @param limit  the limit
     * @param offset the offset
     * @param sort   the sort
     */
    public CustomPageable(int limit, int offset, Sort sort) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new CustomPageable(getPageSize(), (int) (getOffset() + getPageSize()), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    public Pageable previous() {
        // The integers are positive. Subtracting does not let them become bigger than integer.
        return hasPrevious() ?
                new CustomPageable(getPageSize(), (int) (getOffset() - getPageSize()), getSort()) : this;
    }


    @Override
    public Pageable first() {
        return new CustomPageable(getPageSize(), 0, getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
