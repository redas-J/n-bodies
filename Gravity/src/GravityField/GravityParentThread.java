package GravityField;

/**
 *  Parent thread, used to run a desired number of threads to simulate gravitational field.
 * @author Redas Jatkauskas
 */


public class GravityParentThread implements Runnable {

    private int nThreads;
    private Gravity gravity;

    public GravityParentThread(int nThreads, int iterationLimit, Ball[] balls) {
        this.nThreads = nThreads;
        this.gravity = new Gravity(balls, iterationLimit);
    }

    public void run() {

        Thread[] threads = new Thread[nThreads];

        for(int i=0; i<nThreads; i++) {
            threads[i] = new Thread(new GravityThread(gravity, i));
        }

        for(int i=0; i<nThreads; i++) {
            threads[i].start();
        }

        try {
            for(int i=0; i<nThreads; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
