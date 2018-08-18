package com.assistanz.gatekeeper.util.web;

import org.springframework.util.Assert;

import com.assistanz.gatekeeper.constants.GenericConstants;

/**
 * Range object to handle pagination values.
 *
 */
public class Range {

    /** First result attribute. */
    private Integer firstResult = 0;

    /** End result attribute. */
    private Integer endResult = 0;

    /** Max results attribute. */
    private Integer maxResults = 0;

    /** Page element count attribute. */
    private Integer pageElementCount = 0;

    /**
     * Parameterized constructor.
     *
     * @param range to set
     * @param limit to number of rows
     */
    public Range(String range, Integer limit) {
        String[] parsed = range.split("-");
        Assert.isTrue(parsed.length == 2, "Range header in an unexpected format.");
        this.firstResult = new Integer(parsed[0]);
        this.endResult = new Integer(parsed[1]);
        this.pageElementCount = (endResult - firstResult) + 1;
        if (limit == null) {
            pageElementCount = GenericConstants.DEFAULTLIMIT;
        }
        this.maxResults = (endResult - firstResult) + 1;
    }

    /**
     * Get the first result.
     *
     * @return first result.
     */
    public Integer getFirstResult() {
        return this.firstResult;
    }

    /**
     * Get the max results.
     *
     * @return max results.
     */
    public Integer getMaxResults() {
        return this.maxResults;
    }

    /**
     * Get the page number.
     *
     * @return page number.
     */
    public Integer getPageNumber() {
        return (firstResult / pageElementCount) + 1;
    }

    /**
     * Get the content range value.
     *
     * @param resultCount to set
     * @param totalCount to set
     * @return the content range
     */
    public String getContentRangeValue(Integer resultCount, Long totalCount) {
        StringBuilder value = new StringBuilder("items " + firstResult + "-");

        if (resultCount == 0) {
            value.append("0");
        } else if (resultCount >= totalCount.intValue()) {
            if (totalCount.intValue() > 0) {
                value.append(totalCount - 1);
            } else {
                value.append("0");
            }
        } else {
            Integer tempEndResult = (firstResult + resultCount - 1);
            if (tempEndResult >= totalCount) {
                value.append(totalCount - 1);
            } else {
                value.append(firstResult + resultCount - 1);
            }
        }
        value.append("/" + totalCount);
        return value.toString();
    }

    /**
     * Get the content range value.
     *
     * @param resultCount to set
     * @param totalCount to set
     * @param offSet to set
     * @return the content range
     */
    public String getContentRangeValue(Integer resultCount, Long totalCount, Integer offSet) {
        StringBuilder value = new StringBuilder("items " + offSet + "-");

        if (resultCount == 0) {
            value.append("0");
        } else if (resultCount >= totalCount.intValue()) {
            if (totalCount.intValue() > 0) {
                value.append(totalCount - 1);
            } else {
                value.append("0");
            }
        } else {
            Integer tempEndResult = (offSet + resultCount);
            if (tempEndResult >= totalCount) {
                value.append(totalCount - 1);
            } else {
                value.append(tempEndResult);
            }
        }
        value.append("/" + totalCount);
        return value.toString();
    }
}
