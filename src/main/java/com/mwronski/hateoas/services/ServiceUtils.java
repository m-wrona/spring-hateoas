package com.mwronski.hateoas.services;

/**
 * Generic functionality related with services
 *
 * @author Michal Wronski
 * @date 27-05-2014
 */
public final class ServiceUtils {

    /**
     * Normalize given string and convert it into array
     *
     * @param string to be normalized
     * @param separator by which string should be split into array
     * @return non-null instance of array
     */
    public static String[] normalize(String string, String separator) {
        return string.replace('\'', ' ').trim().split(separator);
    }

    /**
     * Check whether previous page is available
     *
     * @param pageNumber number of currently displayed page
     * @return true if previous page is available, false otherwise
     */
    public static boolean hasPreviousPage(int pageNumber) {
        return pageNumber > 1;
    }

    /**
     * Check whether next page is available
     *
     * @param pageNumber number of currently displayed page
     * @param pageSize number of elements displayed on page
     * @param elementsCount total number of available elements
     * @return true if next page is available, false otherwise
     */
    public static boolean hasNextPage(int pageNumber, int pageSize, int elementsCount) {
        return pageToIndex(pageNumber, pageSize) + pageSize < elementsCount;
    }

    /**
     * Convert page into start index of the element that should be searched in repositories
     *
     * @param pageNumber page to be displayed
     * @param pageSize number of elements displayed on page
     * @return start index of element
     */
    public static int pageToIndex(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }

    private ServiceUtils() {
        //no instances
    }
}
