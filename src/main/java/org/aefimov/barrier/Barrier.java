package org.aefimov.barrier;

import org.aefimov.barrier.strategies.IBarrierOvercomingStrategy;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The base implementation of the operation barrier.
 */
public class Barrier {

    private final ConcurrentMap<String, BarrierItem> items;
    private final IBarrierOvercomingStrategy strategy;
    private final IBarrierStorage barrierStorage;
    private final String barrierGlobalKey;
    private final Runnable onOvercome;
    private final Runnable onExpire;

    /**
     * Default constructor.
     *
     * @param keys     the inner keys for initialize the barrier items.
     * @param strategy can be an implementation one of the {@link IBarrierOvercomingStrategy} type.
     * @param barrierStorage the reference to the barrier storage.
     * @param barrierGlobalKey the current barrier's global key.
     * @param onOvercome the task will be fired when the barrier will overcome.
     * @param onExpire the task will be fired when the barrier will expired.
     *
     * @see IBarrierOvercomingStrategy for more detail.
     * @see IBarrierStorage for more detail.
     */
    Barrier(Set<String> keys, IBarrierOvercomingStrategy strategy, IBarrierStorage barrierStorage,
            String barrierGlobalKey,
            Runnable onOvercome,
            Runnable onExpire) {

        this.barrierStorage = barrierStorage;
        this.barrierGlobalKey = barrierGlobalKey;
        this.onOvercome = onOvercome;
        this.onExpire = onExpire;
        this.strategy = strategy;
        this.items = new ConcurrentHashMap<>();
        for (String key : keys) {
            this.items.put(key, new BarrierItem());
        }
    }

    /**
     * Finds a passed key in inner barrier storage and
     * if it found then check the current barrier for overcoming,
     * and if barrier is broken then call the <code>onOvercome</code>
     * and remove the barrier.
     *
     *
     * @param key inner barrier key.
     */
    void toBarrier(String key) {
        BarrierItem barrierItem = this.items.get(key);
        if (barrierItem != null) {
            barrierItem.incrementCount();
            this.items.replace(key, barrierItem);
            Boolean isOvercoming = strategy.check(this);
            if (isOvercoming) {
                runCallback(onOvercome);
                stopBarrier();
            }
        }
    }

    /**
     * Calls in case when the current barrier is
     * expired by timeout and remove current barrier from the storage.
     *
     * Impotent: Before remove the barrier, calls the user <code>onExpire</code> callback.
     *
     * @return <code>true</code> if the current barrier was removed from storage otherwise <code>false</code>
     */
    boolean expireBarrier() {
        runCallback(onExpire);
        return barrierStorage.remove(this.barrierGlobalKey);
    }

    public ConcurrentMap<String, BarrierItem> getItems() {
        return items;
    }

    public class BarrierItem {
        private AtomicLong count;
        private AtomicBoolean completed;

        BarrierItem() {
            this.count = new AtomicLong(0);
            this.completed = new AtomicBoolean(false);
        }

        public Long getCount() {
            return count.get();
        }

        public Boolean isComleted() {
            return completed.get();
        }

        public void complete() {
            this.completed.set(true);
        }

        void incrementCount() {
            this.count.incrementAndGet();
        }
    }

    private void runCallback(Runnable r) {
        Thread thread = new Thread(r);
        thread.start();
    }

    private boolean stopBarrier(){
        return barrierStorage.remove(this.barrierGlobalKey);
    }
}
