package GravityField;

import java.util.Random;

/**
 *  Darbinė gija, skirta prisidėti prie gravitacijos lauko simaliavimo skaičiavimų
 *  @author: Redas Jatkauskas
 */


public class GravityThread implements Runnable{

    Gravity gravity;
    int threadID;
    Random rand = new Random();

    /// These static values may be used to make each thread sleep after completing a job for a random amount of milliseconds - between minDelay and maxDelay
    public static int minDelay = 0;
    public static int maxDelay = 0;

    GravityThread(Gravity gravity, int threadID){
        this.gravity = gravity;
        this.threadID = threadID;
    }

    private void delay(){

        if(maxDelay > 0) {
            try {
                Thread.sleep(minDelay + rand.nextInt(maxDelay - minDelay));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {

        while (gravity.nextJob(threadID)) {
            delay();
        }
    }

}
