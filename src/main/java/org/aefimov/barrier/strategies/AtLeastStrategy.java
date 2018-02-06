package org.aefimov.barrier.strategies;

import org.aefimov.barrier.Barrier;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * The implementation of the {@link IBarrierOvercomingStrategy} which provides
 * the behavior in which the all operation should be come some configured times.
 */
public class AtLeastStrategy implements IBarrierOvercomingStrategy {

    private final long attempts;

    public AtLeastStrategy(Long attempts) {
        this.attempts = attempts <= 0 ? 1 : attempts;
    }

    @Override
    public Boolean check(Barrier barrier) {
        ConcurrentMap<String, Barrier.BarrierItem> items = barrier.getItems();
        for (Map.Entry<String, Barrier.BarrierItem> item : items.entrySet()){
            if (item.getValue().getCount() < attempts)
                return false;
        }
        return true;
    }
}
