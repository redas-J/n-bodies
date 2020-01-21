import GravityField.*;

public class Main {

    public static void main(String[] args){
        Gravity.visualize = true;
        Gravity.print = false;

        GravityThread.minDelay = 0;
        GravityThread.maxDelay = 0;

        if(args.length < 3){
            System.err.println("invalid arguments");
            return;
        }
        int iterationLimit = Integer.parseInt(args[0]);
        int nBalls = Integer.parseInt(args[1]);
        int nThreads = Integer.parseInt(args[2]);
        Gravity.visualize = args.length == 4 && args[3].equals("visualize");

        Ball[] balls = Ball.generateBalls(nBalls);

        GravityTest gravityTest = new GravityTest(balls, iterationLimit);

        Thread t = new Thread(new GravityParentThread(nThreads, iterationLimit, balls));

        t.run();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean result = gravityTest.runTest(balls);
        System.out.println("Gravity Test result: " + result);
    }

}
