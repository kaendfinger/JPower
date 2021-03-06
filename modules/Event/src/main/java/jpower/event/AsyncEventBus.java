package jpower.event;

import jpower.core.WorkerPool;

/**
 * Provides an Asynchronous edition of the generic EventBus.
 */
public class AsyncEventBus extends CustomEventBus {
    private final WorkerPool workerPool;

    /**
     * Create an AsyncEventBus using an existing WorkerPool
     *
     * @param workerPool Worker Pool
     */
    private AsyncEventBus(WorkerPool workerPool) {
        this.workerPool = workerPool;
    }

    /**
     * Create an AsyncEventBus
     */
    public AsyncEventBus() {
        this(new WorkerPool());
    }

    /**
     * Creates an AsyncEventBus with a limited amount of workers
     *
     * @param workers number of workers
     */
    public AsyncEventBus(int workers) {
        this(new WorkerPool(workers));
    }

    /**
     * Post an Event to the Bus asynchronously
     *
     * @param event Event to Post
     */
    @Override
    public void post(Object event) {
        workerPool.submit(() -> handlers.forEach(handler -> {
            handler.executeEvent(event);
        }));
    }
}
