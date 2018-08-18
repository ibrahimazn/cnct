/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.util.sorting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.assistanz.slmlp.container.constants.GenericConstants;
import com.assistanz.slmlp.container.util.Range;
import com.assistanz.slmlp.container.util.SortingUtil;

/**
 * Value object for paging and sorting, used in business api's and controllers.
 *
 */
public class PagingAndSorting {

    /** The actual page number. */
    private Integer pageNumber;

    /** The item size of the page. */
    private Integer pageSize;

    /** The sort field. */
    private String sortField;

    /** The sort direction. */
    private String sortDirection;

    /** Range attribute for paging header reading and response content formatting. */
    private Range parsedRange;

    /**
     * Parameterized constructor.
     *
     * @param range from header
     * @param sortBy from param
     * @param limit from param
     * @param clazz of the entity
     */
    public PagingAndSorting(String range, String sortBy, Integer limit, Class<? extends Object> clazz) {
        this.sortField = SortingUtil.checkSortingParameter(clazz, sortBy);
        this.sortDirection = SortingUtil.checkSortStyle(clazz, sortBy).toString();
        parsedRange = new Range(range.replaceAll(GenericConstants.RANGE_PREFIX, ""), limit);

        this.pageNumber = parsedRange.getPageNumber();
        this.pageSize = parsedRange.getMaxResults();
    }

    /**
     * Get the pageNumber.
     *
     * @return pageNumber
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Get the pageSize.
     *
     * @return pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Get the sortField.
     *
     * @return sortField
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * Get the sortDirection.
     *
     * @return sortDirection
     */
    public String getSortDirection() {
        return sortDirection;
    }

    /**
     * Converts to Spring PageRequest object.
     *
     * @return PageRequest
     */
    public PageRequest toPageRequest() {
        Direction direction = Direction.ASC;

        if (this.sortDirection == "DESC") {
            direction = Direction.DESC;
        }
        Sort sort = new Sort(direction, sortField);
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }

    /**
     * Get the pagination header value.
     *
     * @param page the page object
     * @return header value
     */
    public String getPageHeaderValue(Page<? extends Object> page) {
        return parsedRange.getContentRangeValue(page.getNumberOfElements(), Long.valueOf(page.getTotalElements()));
    }

}
