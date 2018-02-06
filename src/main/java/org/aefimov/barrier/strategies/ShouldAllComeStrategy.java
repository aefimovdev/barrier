package org.aefimov.barrier.strategies;

import org.aefimov.barrier.Barrier;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * The implementation of the {@link IBarrierOvercomingStrategy} which provides
 * the behavior in which the all operation should be come.
 */
public class ShouldAllComeStrategy implements IBarrierOvercomingStrategy {

    @Override
    public Boolean check(Barrier barrier) {
        ConcurrentMap<String, Barrier.BarrierItem> items = barrier.getItems();
        for (Map.Entry<String, Barrier.BarrierItem> item : items.entrySet()){
            if (item.getValue().getCount() == 0)
                return false;
        }
        return true;
    }
}
