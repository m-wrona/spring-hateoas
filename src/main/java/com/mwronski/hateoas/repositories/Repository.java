package com.mwronski.hateoas.repositories;

import java.util.List;

/**
 * Generic repository that provides access to data. <br/>
 * Note: use standard Repository or CrudRepository from Spring when it grows bigger
 *
 * @param <T> type of element managed by repository
 * @author Michal Wronski
 * @date 27-05-2014
 */

public interface Repository<T> {

    /**
     * Create new element
     *
     * @param entity element to be created
     * @return the same instance with filled ID of newly created entity
     */
    T create(T entity);

    /**
     * Get elements
     *
     * @param start index of first elements to be returned
     * @param rowCount number of elements to be returned
     * @param columns optional columns that should be returned in results. If not given all will be taken.
     * @return not-nullable list with found elements
     */
    List<T> get(int start, int rowCount, String... columns);

    /**
     * Get count of elements
     *
     * @return number of elements
     */
    int size();

    /**
     * Find element by ID
     *
     * @param id
     * @param columns optional columns that should be returned in results. If not given all will be taken.
     * @return instance of found element, null otherwise
     */
    T find(String id, String... columns);

}
