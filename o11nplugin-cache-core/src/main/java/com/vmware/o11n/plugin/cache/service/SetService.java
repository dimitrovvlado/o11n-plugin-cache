package com.vmware.o11n.plugin.cache.service;

/**
 * Service for working with distributed set instances.
 */
public interface SetService {

    /**
     * Adds the specified element to the specified set if it is not already present.
     *
     * @param setName name of the set.
     * @param value element to be added to this set.
     *
     * @return {@code true} if this set did not already contain the specified element
     */
    boolean addForSet(String setName, String value);

    /**
     * Adds the specified element to the default set if it is not already present.
     *
     * @param value element to be added to this set.
     *
     * @return {@code true} if this set did not already contain the specified element
     */
    boolean add(String value);

    /**
     * Removes the specified element from the specified set if it is present.
     *
     * @param setName name of the set.
     * @param value value to be removed from this set, if present.
     *
     * @return {@code true} if this set contained the specified element.
     */
    boolean removeForSet(String setName, String value);

    /**
     * Removes the specified element from the default set if it is present.
     *
     * @param value value to be removed from this set, if present.
     *
     * @return {@code true} if this set contained the specified element.
     */
    boolean remove(String value);

    /**
     * Removes all of the elements from the default set.
     *
     * @param setName name of the set.
     */
    void clearForSet(String setName);

    /**
     * Removes all of the elements from the specified set.
     */
    void clear();

    /**
     * Returns an array containing all of the elements in the specified set.
     *
     * @param setName name of the set.
     *
     * @return an array containing all the elements in this set.
     */
    String[] elementsForSet(String setName);

    /**
     * Returns an array containing all of the elements in the default set.
     *
     * @return an array containing all the elements in this set.
     */
    String[] elements();

    /**
     * Returns {@code true} if the specified set contains the specified element.
     *
     * @param setName name of the set.
     * @param value element whose presence in this set is to be tested.
     *
     * @return {@code true} if this set contains the specified element
     */
    boolean containsForSet(String setName, String value);

    /**
     * Returns {@code true} if the default set contains the specified element.
     *
     * @param value element whose presence in this set is to be tested.
     *
     * @return {@code true} if this set contains the specified element.
     */
    boolean contains(String value);

    /**
     * Returns the number of elements in the specified set (its cardinality).
     *
     * @param setName name of the set.
     *
     * @return the number of elements in this set (its cardinality).
     */
    int sizeForSet(String setName);

    /**
     * Returns the number of elements in the default set (its cardinality).
     *
     * @return the number of elements in this set (its cardinality).
     */
    int size();
}
