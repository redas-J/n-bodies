package GravityField;

import java.awt.*;
import java.util.Random;

/**
 *  Gravitacijos objekto - rutulio - klasė
 *
 * @author Redas Jatkauskas, Informatika 3 kursas, 4 grupe
 */


public class Ball {

    int id;
    public Color color;
    private Ball[] balls;

    public double pos_x;
    public double pos_y;
    public double speed_x = 0;
    public double speed_y = 0;

    public double r;
    public double m;

    public Ball(int id, double pos_x, double pos_y, double r){
        this.id = id;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.r = r;
        this.m = r*r*3.14;
        Random rand = new Random();
        this.color = new Color(rand.nextInt(200),rand.nextInt(200),rand.nextInt(200));
    }

    double force_x = 0;
    double force_y = 0;


    public void countForce(){

        double f_x = 0;
        double f_y = 0;

        for(Ball ball : balls) {

            if(ball.id == id) {
                continue;
            }

            double dist_x = ball.pos_x - pos_x;
            double dist_y = ball.pos_y - pos_y;

            double line_dist = Math.sqrt(dist_x*dist_x + dist_y*dist_y);

            if(line_dist == 0) continue;

            double f_new;

            if(line_dist < ball.r){

                f_new = Gravity.G * m / line_dist;
            }
            else {

                f_new = Gravity.G * m * ball.m / (line_dist * line_dist);
            }

            f_x += f_new * dist_x / line_dist;
            f_y += f_new * dist_y / line_dist;
        }

        //force_x += f_x;
        //force_y += f_y;
        speed_x += Gravity.timeStep * f_x / m;
        speed_y += Gravity.timeStep * f_y / m;
    }


    public void updatePos(){


        pos_x += speed_x;
        pos_y += speed_y;

        //force_x = 0;
        //force_y = 0;
    }

    /// Set the array of all balls, to use for calculations
    public void setBalls(Ball[] balls){
        this.balls = balls;
    }
    /// Sets the speed of this ball
    public void setSpeed(double speed_x, double speed_y){
        this.speed_x = speed_x;
        this.speed_y = speed_y;
    }


    public static Ball[] generateBalls(int size){

        int positionBound = 80;
        int radiusBound = 8;

        Ball[] balls = new Ball[size];

        Random rnd = new Random(1);

        for(int i=0; i<size; i++){
            balls[i] = new Ball(i, rnd.nextInt(positionBound), rnd.nextInt(positionBound), 1 + rnd.nextInt(radiusBound) );
        }

        return balls;
    }

}