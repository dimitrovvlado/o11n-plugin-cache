package com.vmware.o11n.plugin.cache.service;

import java.util.List;

/**
 * Service for working with distributed list instances.
 */
public interface ListService {

    /**
     * Appends the specified element to the end of this list.
     *
     * @param listName name of the list.
     * @param value element to be appended to this list.
     *
     * @return {@code true} if this collection changed as a result of the call
     */
    boolean addForList(String listName, String value);

    /**
     * Appends the specified element to the end of this list.
     *
     * @param value element to be appended to this list.
     *
     * @return {@code true} if this collection changed as a result of the call
     */
    boolean add(String value);

    /**
     * Returns the element at the specified position from the specified list.
     *
     * @param listName name of the list.
     * @param index index of the element to return.
     *
     * @return the element at the specified position from the default list.
     */
    String getForList(String listName, int index);

    /**
     * Returns the element at the specified position from the default list.
     *
     * @param index index of the element to return.
     *
     * @return the element at the specified position from the default list.
     */
    String get(int index);

    /**
     * Returns the number of elements in the specified list.
     *
     * @param listName name of the list.
     *
     * @return the number of elements in the specified list.
     */
    int sizeForList(String listName);

    /**
     * Returns the number of elements in the default list.
     *
     * @return the number of elements in the default list.
     */
    int size();


    /**
     * Returns the index of the last occurrence of the specified element in the specified list, or -1 if the specified
     * list does not contain the element.
     *
     * @param listName name of the list.
     * @param value element to search for.
     *
     * @return the index of the last occurrence of the specified element in the specified list, or -1 if this list does
     * not contain the element
     */
    int indexOfForList(String listName, String value);

    /**
     * Returns the index of the last occurrence of the specified element in the default list, or -1 if this list does
     * not contain the element.
     *
     * @param value element to search for.
     *
     * @return the index of the last occurrence of the specified element in the default list, or -1 if this list does
     * not contain the element
     */
    int indexOf(String value);

    /**
     * Removes all of the elements from the specified list.
     *
     * @param listName name of the list.
     */
    void clearForList(String listName);

    /**
     * Removes all of the elements from the default list.
     */
    void clear();

    /**
     * Returns a view of the portion of the specified list between the specified {@code fromIndex}, inclusive, and
     * {@code toIndex}, exclusive.
     *
     * @param listName name of the list.
     * @param fromIndex low endpoint (inclusive) of the subList.
     * @param toIndex high endpoint (exclusive) of the subList.
     *
     * @return a view of the specified range within the specified list.
     */
    List<String> subListForList(String listName, int fromIndex, int toIndex);

    /**
     * Returns a view of the portion of the default list between the specified {@code fromIndex}, inclusive, and
     * {@code toIndex}, exclusive.
     *
     * @param fromIndex low endpoint (inclusive) of the subList.
     * @param toIndex high endpoint (exclusive) of the subList.
     *
     * @return a view of the specified range within the default list.
     */
    List<String> subList(int fromIndex, int toIndex);

    /**
     * Removes the first occurrence of the specified element from the specified list, if it is present. If this list
     * does not contain the element, it is unchanged.
     *
     * @param listName name of the list.
     *
     * @param value element to be removed from the default list, if present.
     *
     * @return {@code true} if the default list contained the specified element, {@code false} otherwise.
     */
    boolean removeValueForList(String listName, String value);

    /**
     * Removes the first occurrence of the specified element from this list, if it is present. If this list does not
     * contain the element, it is unchanged.
     *
     * @param value element to be removed from the default list, if present.
     *
     * @return {@code true} if the default list contained the specified element, {@code false} otherwise.
     */
    boolean removeValue(String value);

    /**
     * Removes the element at the specified position in the specified list. Shifts any subsequent elements to the left
     * (subtracts one from their indices). Returns the element that was removed from the list.
     *
     * @param listName name of the list.
     * @param index the index of the element to be removed.
     *
     * @return the element previously at the specified position.
     */
    String removeForList(String listName, int index);

    /**
     * Removes the element at the specified position in the default list. Shifts any subsequent elements to the left
     * (subtracts one from their indices). Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed.
     *
     * @return the element previously at the specified position.
     */
    String remove(int index);
}
