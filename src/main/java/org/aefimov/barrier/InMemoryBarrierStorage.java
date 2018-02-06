package org.aefimov.barrier;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The in memory implementation of {@link IBarrierStorage}.
 */
public class InMemoryBarrierStorage implements IBarrierStorage {

    private final ConcurrentMap<String, Barrier> barrierStorage;

    /**
     * Default constructor.
     */
    public InMemoryBarrierStorage() {
        this.barrierStorage = new ConcurrentHashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(String key, Barrier barrier) {
        this.barrierStorage.put(key, barrier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Barrier get(String key) {
        return this.barrierStorage.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean remove(String key) {
        Barrier remove = this.barrierStorage.remove(key);
        return null != remove;
    }
}
