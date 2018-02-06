package org.aefimov.barrier;

import org.aefimov.barrier.bean.KillBarrierTaskObj;
import org.aefimov.barrier.exception.BarrierAlreadyExistException;
import org.aefimov.barrier.exception.BarrierNotFoundException;
import org.aefimov.barrier.strategies.IBarrierOvercomingStrategy;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main class of the barrier component.
 * If you want to use the barrier you have to use this bean.
 *
 * This bean encapsulates some method for interact with the operation barrier component.
 */
public class BarrierManagement implements IBarrierManagement {
//    private static final Logger LOG = Logger.getLogger(BarrierManagement.class);

    private final IBarrierStorage barrierStorage;

    /**
     * Default constructor.
     * Set some needed dependencies.
     *
     * @param barrierStorage inject the implementation of the barrier storage to store the barriers.
     * @param createSchedulerTask inject the scheduler task maker. Is needed to kill the barrier when lifetime is over.
     * @param optionFactory inject the scheduler's task option factory.
     */
    public BarrierManagement() {
        this.barrierStorage = new InMemoryBarrierStorage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Barrier createBarrier(String barrierKey, Set<String> innerKeys, IBarrierOvercomingStrategy strategy,
                                 long lifetime,
                                 Runnable overcomingCallback,
                                 Runnable expireCallback)
            throws BarrierAlreadyExistException {

        if (this.barrierStorage.get(barrierKey) != null)
            throw new BarrierAlreadyExistException(String
                    .format("The barrier with key '%s' is exist!", barrierKey));

        Barrier barrier = new Barrier(innerKeys, strategy, barrierStorage, barrierKey, overcomingCallback, expireCallback);
        this.barrierStorage.add(barrierKey, barrier);
        scheduleKillBarrierTask(new KillBarrierTaskObj(barrierKey), lifetime);
        return barrier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toBarrier(String barrierKey, String innerKey) throws BarrierNotFoundException {
        Barrier barrier = barrierStorage.get(barrierKey);
        if (null == barrier)
            throw new BarrierNotFoundException(String
                    .format("Can't find the barrier with key '%s' when 'toBarrier' operation executing!", barrierKey));
        barrier.toBarrier(innerKey);
    }

    @Override
    public boolean isExist(String barrierKey) {
        return barrierStorage.get(barrierKey) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean killBarrier(String barrierKey) {
        Barrier barrier = barrierStorage.get(barrierKey);
        return null != barrier && barrier.expireBarrier();
    }


    private void scheduleKillBarrierTask(final KillBarrierTaskObj task, long lifetime) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                killBarrier(task.getBarrierKey());
            }
        }, lifetime);

    }

}
