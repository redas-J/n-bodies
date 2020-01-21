import GravityField.Ball;
import GravityField.GravityParentThread;

/** Simple system multithreading performance test.
    @author R.Vaicekauskas
    Fitted by R. Jatkauskas
*/
public class TTest extends Thread
{
   volatile boolean finished = false;
    Ball[] balls;
   public TTest() {
       balls = Ball.generateBalls(ballsSize);
   }

   //-----------------------------------------
   // work unit for 1 thread
   void work (){

       Thread t = new Thread(new GravityParentThread(nThreads, iterations, balls));

       t.run();

       try {
           t.join();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

   //-----------------------------------------
   // Thread.run()
   public void run() {

        work();
        this.finished = true;
   }

   //-----------------------------------------
   // Global parameters
   static int iterations = 0;
   static int nThreads = 0; // Number working threads
    static int  ballsSize = 0;

   //-----------------------------------------
   // Make tests for given iterations and number threads
   // Returns working time
   static double makePerformanceTest() throws Exception
   {
       TTest aThread = new TTest();
       long time0 = System.currentTimeMillis();
       aThread.start();
       aThread.join();

       long time1 = System.currentTimeMillis();
       double dtime = (time1-time0)/1000.;
       return dtime;
   }

   int generateWorkload() throws Exception {

       int iterations = 100;
       for (;;iterations *= 2)
       {
           double dtime = makePerformanceTest();
           if (dtime > 1.) break; //>>>>>>>
       }
       return iterations;
   }

   //-----------------------------------------
   public static void main(String[] args)
   {
       try
       {
          if (args.length < 2
              || ! ( (nThreads  = Integer.parseInt(args[0])) >=1 && nThreads  <= 16
              && (iterations = Integer.parseInt(args[1])) >=1 && iterations <= 100000000))
          {
              System.err.println("Simple system multithreading performance test. Ver 1.3");
              System.err.println("Parameters: <number threads 1..16> <iterations: 1..100000000>");

              System.err.println("#Make auto test: find iterations for > 1 sec...");

              TTest.iterations = Integer.parseInt(args[0]);
              TTest.ballsSize = Integer.parseInt(args[1]);


              System.out.println("#n #iterations #size #timeS #speedup");

              double dtime1=0.; 
              for (nThreads = 1; nThreads <= 32; nThreads *= 2)
              {
                 double dtime = makePerformanceTest();
                 dtime1 = nThreads == 1 ? dtime : dtime1;
                 double speedup = dtime1 / dtime;
                 System.out.println(nThreads + " \t" + iterations  + " \t" + ballsSize + " \t" +dtime + " \t" + speedup);
                 System.gc();
              }

              System.out.println("#completed");
              System.exit(1); //>>>>>>>
          }
          else
          {
              System.err.println("#Test for: nThreads="+nThreads+" iterations="+iterations);
              double dtime = makePerformanceTest();
              System.err.println("#Completed. Running time: " + dtime + "s");
              System.out.println( nThreads + " " + iterations  + " " + dtime );
              System.exit(0); //>>>>>>>
          }
       }
       catch (Exception exc)
       {
          System.out.println(exc);
          exc.printStackTrace();
          System.exit(4);
       }
   }
}