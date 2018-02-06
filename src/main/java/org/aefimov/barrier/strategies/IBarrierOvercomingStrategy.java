package org.aefimov.barrier.strategies;

import org.aefimov.barrier.Barrier;

/**
 * Base interface which describe the barrier overcoming strategy.
 */
public interface IBarrierOvercomingStrategy {

    /**
     * Checks the barrier for ability to broken.
     *
     *
     * @param barrier the barrier for check.
     * @return <code>true</code> if the barrier can be broken otherwise <code>false</code>.
     *
     * The result of check is depend on a concrete implementation.
     */
    Boolean check(Barrier barrier);

}
