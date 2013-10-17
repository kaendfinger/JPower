package jpower.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a Collection of Workers
 */
public class WorkerPool {
    private List<Worker> workers = new ArrayList<Worker>();

    private int size;

    /**
     * Creates a Worker Pool of specified size
     * @param size size of pool
     */
    public WorkerPool(int size) {
        this.size = size;
    }

    /**
     * Creates a Worker Pool of the Default Size (50 Workers)
     */
    public WorkerPool() {
        this.size = 50;
    }

    /**
     * Submit a Task to the Queue
     * @param task Task
     * @return Was added to Queue
     */
    public boolean submit(Task task) {
        Worker worker = pullWorker();
        if (worker==null) {
            return false;
        }
        worker.addTask(task);
        return true;
    }

    /**
     * Retrieves an Open Worker, or creates one, if there is not one available.
     * It does however follow the Size Limit.
     * @return A Worker if Found, else Null
     */
    public Worker pullWorker() {
        if (workers.isEmpty()) {
            Worker worker = newWorker();
            workers.add(worker);
            return worker;
        }
        for (Worker worker : workers) {
            if (!worker.isWorking()) {
                return worker;
            }
        }
        return newWorker();
    }

    /**
     * Creates a New Worker
     * @return Worker
     */
    private Worker newWorker() {
        if (workers.size()==size) {
            return null;
        }
        Worker worker = new Worker();
        worker.start();
        return worker;
    }

    /**
     * Gets a list of all Workers
     * @return Worker List
     */
    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * Stops all Workers
     */
    public void stopWorkers() {
        List<Worker> temp = new ArrayList<Worker>(workers);
        for (Worker worker : temp) {
            worker.stop();
            while (worker.isWorking());
            workers.remove(worker);
        }
    }

    /**
     * Gets Current amount of Workers
     * @return Amount of Workers
     */
    public int currentWorkers() {
        return workers.size();
    }

    /**
     * Waits for all Workers to be Open
     */
    public void waitForAll() {
        for (Worker worker : workers) {
            worker.waitFor();
        }
    }
}
