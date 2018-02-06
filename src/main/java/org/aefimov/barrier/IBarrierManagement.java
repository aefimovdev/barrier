package org.aefimov.barrier;

import org.aefimov.barrier.exception.BarrierAlreadyExistException;
import org.aefimov.barrier.exception.BarrierNotFoundException;
import org.aefimov.barrier.strategies.IBarrierOvercomingStrategy;

import java.util.Set;

/**
 * Base interface which describe the barrier management functional.
 */
public interface IBarrierManagement {

    /**
     * The barrier will be created an added into storage.
     * And for cases where an object does not live permanently the barrier-kill-task
     * is created and added to scheduler.
     *
     * @param barrierKey the barrier unique key in storage.
     * @param innerKeys the inner keys for initialize the barrier items.
     * @param strategy can be an implementation one of the {@link IBarrierOvercomingStrategy} type.
     * @param lifetime the barrier lifetime. After this lifetime the barrier will killed.
     * @param onOvercome the task will be fired when the barrier will overcome.
     * @param onExpire the task will be fired when the barrier will expired.
     * @return made barrier
     * @throws BarrierAlreadyExistException if barrier with key is already exist in storage.
     */
    Barrier createBarrier(String barrierKey,
                          Set<String> innerKeys,
                          IBarrierOvercomingStrategy strategy,
                          long lifetime,
                          Runnable onOvercome,
                          Runnable onExpire)
            throws BarrierAlreadyExistException;

    /**
     * In places where the operation associated with the barrier is considered to be logically completed,
     * you must call this method.
     *
     * @param barrierKey the barrier key for search a barrier.
     * @param innerKey a completed operation key.
     * @throws BarrierNotFoundException if a barrier with passed key is exist in storage.
     */
    void toBarrier(String barrierKey, String innerKey) throws BarrierNotFoundException;

    boolean isExist(String barrierKey);

    /**
     * You can kill the barrier by calling this method.
     * Usually, the barrier kill by the scheduler task and
     * you don't have to worry about this.
     *
     *
     * @param barrierKey for search the barrier for kill.
     * @return <code>true</code> if barrier is successfully killed
     * and if the barrier not found then return <code>false</code>
     */
    Boolean killBarrier(String barrierKey);

}
