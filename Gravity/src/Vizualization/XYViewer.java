package Vizualization;

import GravityField.Ball;
import GravityField.Gravity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

/**
 *  2d visualisation class
 * @author R.Vaicekauskas
 */

public class XYViewer extends JFrame {

    double windowLocation_x = 0;
    double windowLocation_y = 0;
    
    Ball[] balls;

    double viewStart_x = 0;
    double viewStart_y = 0;

    double viewEnd_x = 80;
    double viewEnd_y = 80;

    private PaintCanvas canvas;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;

    private int iteration = 0;

    public XYViewer() {

        refreshTitle();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        canvas = new PaintCanvas();
        contentPane.add(canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();

        this.setLocation((int)windowLocation_x, (int)windowLocation_y);

        canvas.addMouseListener(new MouseListener(){

                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}

                // Center clicked screen pos
                public void mouseClicked(MouseEvent e)
                {
                    //System.out.println("Click");
                    int cc = e.getClickCount();

                    double dx = canvas.getWidth();
                    double dy = canvas.getHeight();

                    int xc = e.getX();
                    int yc = e.getY();

                    viewStart_x += viewEnd_x * xc/dx - viewEnd_x/2;
                    viewStart_y += viewEnd_y * yc/dy - viewEnd_y/2;
                    refreshTitle();
                    canvas.repaint();
                }
            }
        );

        // zoom out
        addButton(buttonPanel, "-", new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                System.out.println("-");
                viewStart_x -= viewEnd_x /2;
                viewStart_y -= viewEnd_y /2;
                viewEnd_x *=2;
                viewEnd_y *=2;
                refreshTitle();
                canvas.repaint();
            }
        });

        // Zoom in
        addButton(buttonPanel, "+", new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                System.out.println("+");
                viewStart_x += viewEnd_x/4;
                viewStart_y += viewEnd_y/4;
                viewEnd_x *= 0.5; viewEnd_y *= 0.5;
                refreshTitle();
                canvas.repaint();
            }
        });

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        setSize(WIDTH, HEIGHT);
    }

    /*public static void sortBalls(Ball[] balls) {
        // surikiuoti nuo didziausio iki maziausio, kad mazesnius piestu ANT didesniu
        if (balls.length < 2) return;
        for (int j = 0; j < balls.length; j++) {
            for (int i = 1; i < balls.length; i++) {
                if (balls[i].r > balls[i - 1].r) {
                    Ball temp = balls[i - 1];
                    balls[i - 1] = balls[i];
                    balls[i] = temp;
                }
            }
        }
    }//*/

    public synchronized void setBalls(Ball[] balls) {
        this.balls = balls;
        canvas.repaint();
    }

    public void repaint(){
        iteration++;
        refreshTitle();
        canvas.repaint();
        System.gc();
    }

    void refreshTitle(){
        setTitle("Iteration: " + iteration + " Area: ["+viewStart_x+ ";" +(viewStart_x + viewEnd_x)+ "] x [" + viewStart_y + ";" + (viewStart_y + viewEnd_y) + "]" );
    }


    public void addButton(Container c, String title, ActionListener listener) {
        JButton button = new JButton(title);
        c.add(button);
        button.addActionListener(listener);
    }

    class PaintCanvas extends JPanel {
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            double dx = canvas.getWidth();
            double dy = canvas.getHeight();
            if (dx < 0 || dy < 0) return;

            Ball[] ballsUse;

            synchronized(this) {
                ballsUse = balls;
            }

            for (int i = 0; i < ballsUse.length; i++) {

                double xEllipse = ballsUse[i].pos_x;
                double yEllipse = ballsUse[i].pos_y;

                xEllipse = (xEllipse - viewStart_x) * dx/viewEnd_x - ballsUse[i].r;
                yEllipse = (yEllipse - viewStart_y) * dy/viewEnd_y - ballsUse[i].r;

                double rElipse = (ballsUse[i].r*2) * dy/viewEnd_x;

                g2.setColor(ballsUse[i].color);
                g2.fill(new Ellipse2D.Double(xEllipse, yEllipse, rElipse*dx/dy, rElipse));

            }

        }
    }
}