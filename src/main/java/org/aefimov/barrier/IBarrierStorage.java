package org.aefimov.barrier;

/**
 * Base interface which describe the barrier storage.
 */
public interface IBarrierStorage {

    /**
     * Adds barrier into storage
     * @param key barrier key.
     * @param barrier the instance of the barrier.
     */
    void add(String key, Barrier barrier);

    /**
     * Retrieves the barrier form storage.
     *
     * @param key the barrier key
     * @return the barrier or null if barrier is not exist.
     */
    Barrier get(String key);

    /**
     * Removes the barrier from storage.
     *
     * @param key the barrier key.
     * @return operation result.
     */
    Boolean remove(String key);

}
