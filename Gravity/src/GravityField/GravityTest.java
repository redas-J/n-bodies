package GravityField;

import Vizualization.XYViewer;

/**
 * Class used to test parallel computing results by comparing them with the same simulation using serial computing
 * @author Redas Jatkauskas
 */

public class GravityTest {

    private final int iterationLimit;
    private int iteration = 0;
    private Ball[] balls;

    public GravityTest(Ball[] balls, int iterationLimit){

        this.balls = new Ball[balls.length];
        this.iterationLimit = iterationLimit;

        for(int i=0; i<balls.length; i++){
            this.balls[i] = new Ball(balls[i].id, balls[i].pos_x, balls[i].pos_y, balls[i].r);
            this.balls[i].speed_x = balls[i].speed_x;
            this.balls[i].speed_y = balls[i].speed_y;
        }

        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].setBalls(this.balls);
        }
    }

    public void runTest(){

        XYViewer viewer = null;

        /*if(Gravity.visualize){
            viewer = new XYViewer();
            viewer.setBalls(balls);
            viewer.show();
        }*/

        for(iteration = 0; iteration < iterationLimit; iteration++) {
            for (int i = 0; i < balls.length; i++) {
                balls[i].countForce();
            }
            for (int i = 0; i < balls.length; i++) {
                balls[i].updatePos();
            }

            //if(Gravity.visualize) viewer.repaint();
        }
    }


    public boolean runTest(Ball[] balls){

        runTest();
        return compare(balls);
    }

    /// Palygina itertuotas reiksmes su paduotom ir grazina tiesa, jeigu jos identiskos
    public boolean compare(Ball[] balls){

        /// *** Priminimas: privalo buti ir vienodai isrykiuota !

        if(this.balls.length != balls.length) return false;

        for(int i=0; i<balls.length; i++){
            /*
            System.out.println("Comparing balls1 and balls2: ");
            System.out.println("Pos: x: " + this.balls[i].pos_x + " : " + balls[i].pos_x + " y: " + this.balls[i].pos_y + " : " + balls[i].pos_y);
            System.out.println("Speed: x:" + this.balls[i].speed_x + " : " + balls[i].speed_x + " y: " + this.balls[i].speed_y + " : " + balls[i].speed_y);
            */
            if(this.balls[i].id != balls[i].id) return false;
            if(this.balls[i].pos_x != balls[i].pos_x) return false;
            if(this.balls[i].pos_y != balls[i].pos_y) return false;
            if(this.balls[i].speed_x != balls[i].speed_x) return false;
            if(this.balls[i].speed_y != balls[i].speed_y) return false;
            if(this.balls[i].r != balls[i].r) return false;
        }

        return true;
    }
}
