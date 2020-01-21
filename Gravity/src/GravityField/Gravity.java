package GravityField;
import Vizualization.XYViewer;

/**
 *  Gravity fieldsimulation class
 * @author Redas Jatkauskas, Informatika 3 kursas, 4 grupe
 */

public class Gravity {

    public static boolean visualize = false;
    public static boolean print = false;

    public static double G = 20.0;
    public static double timeStep = 0.00001; /// smaller timeStep => slower ball movement

    private XYViewer viewer;
    private Ball[] balls;

    private final int iterationLimit;

    private int iteration = 0;
    private int nCompleted = 0;
    private int currentIndex = 0;

    private final int nStates = 2;
    private int state = 0; /** 0 - counting state, 1 - updating state */

    private boolean workComplete = false;

    private void repaint(){
        if(visualize) {
            viewer.repaint();
            System.gc();
        }
    }

    Gravity(Ball[] balls, int iterationLimit){

        this.iterationLimit = iterationLimit;
        this.balls = balls;

        if(visualize) {
            viewer = new XYViewer();
            viewer.setBalls(balls);
            viewer.show();
        }

        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].setBalls(this.balls);
        }
    }

    private synchronized int getNextIndex(){

        if(currentIndex == balls.length) return -1;
        currentIndex++;
        return currentIndex-1;
    }

    private synchronized void nextState(){

        state = (state + 1) % nStates;
        currentIndex = 0;
        nCompleted = 0;

        if(state == 0){
            iteration++;
            repaint();
            if(iteration == iterationLimit) workComplete = true;
        }
    }

    private void work(Ball ball){

        switch(state){
            case 0:
                ball.countForce();
                break;
            case 1:
                ball.updatePos();
                break;
        }
    }


    private synchronized void markAsComplete(){

        nCompleted++;
        if (nCompleted == balls.length) nextState();
    }


    boolean nextJob(int threadID){
        int thisState = state; /// used only for output

        int localIndex = getNextIndex();

        if(localIndex == -1) {
            return !workComplete;
        }

        if(workComplete) return false;

        work(balls[localIndex]);

        printProgress("Thread " + threadID + ": work(ball " + localIndex + ", state " + thisState + ", iter " + iteration + "): success");

        markAsComplete();

        return true;
    }

    private void printProgress(String msg){
        if(!Gravity.print) return;
        System.out.println(msg);
    }

}