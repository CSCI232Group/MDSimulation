/* Class: CSCI 232
 * Author: Micheal Hewitt, Ian Hecker, Jacob Yakawich, Garret Hilton
 * Lab1: Collision System
 * This main class for our particle simulation is modeled after 
 * ~Robert Sedgewick~
 * ~Kevin Wayne~
 * design. Because we liked and used a similar design to their priority Que, 
 * similar steps had to be taken to use the PQ correctly
*/
package mdsimulation;//The project name we are working on

import edu.princeton.cs.algs4.StdDraw;//Libraries we are using
import java.awt.Color;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Ian Hecker
 */
public class MDSimulation// Our main where we will be running everything off of.
{              
    private MinPQ<QueueItem> PQ;//We are starting to set up the Priority Que
    private double t = 0.0;//We are setting up the clock
    private Particle[] particleArray;//This is an arrary for the particles 
    
    public MDSimulation(Particle[] particles)// We are now starting up our system wih a set of particles
    
    {
        this.particleArray = particles.clone();        
    }        
    public void findCollisions(Particle p1, double limit)// We are updating the Priority Que
    {     
        if(p1 == null) return;
        
        for(int iter = 0; iter < particleArray.length; iter++)
        {
            double dt = p1.timeToParticleCollision(particleArray[iter]);// If an object hits another object this will run
            if(t + dt <= limit)              
                PQ.insertCollision(new QueueItem(t + dt, p1, particleArray[iter]));
        }
        double dtx = p1.timeToVertWall();//If an object hits a wall this will run
        double dty = p1.timeToHorizWall();
        if(t + dtx <= limit){PQ.insertCollision(new QueueItem(t + dtx, p1, null));}
        if(t + dty <= limit){PQ.insertCollision(new QueueItem(t + dty, null, p1));}        
    }
    private void reDraw(double limit)//We are redrawing each object
    {
        StdDraw.clear();
        for(Particle p : particleArray)
            {p.drawParticle();}
        StdDraw.show();
        StdDraw.pause(20);
            if(t <= limit)
                {PQ.insertCollision(new QueueItem(t + 1.0 / 0.5, null, null));}
    }
    public void runSimulation(double limit)// We are stating that this will run for a curtain amount of time
    { 
        PQ = new MinPQ<QueueItem>();
        for(Particle p : particleArray)// We are starting the Priority Que with collision and redrawing the object
            {findCollisions(p, limit);}
        
        PQ.insertCollision(new QueueItem(0, null, null));
        
        while(!PQ.isEmpty())// This is a loop for our program
        {               
            QueueItem ev = PQ.delMinCollision();
            if(!ev.isValidated()) continue;//If valid, continue
            Particle p1 = ev.p1;
            Particle p2 = ev.p2;
                    
                for(Particle p : particleArray)//for(double t = 0.00; true; t += 0.01){}                 
                    {p.updatePosition(ev.time - t);}                
                t = ev.time;
                    
                if(p1 != null && p2 != null)      {p1.bounceOffOther(p2);}// objects collide 
                else if(p1 != null && p2 == null) {p1.bounceOffVert();}// wall collide
                else if(p1 == null && p2 != null) {p2.bounceOffHoriz();}// wall collide
                else if(p1 == null && p2 == null) {reDraw(limit);}// redraw
                    
                findCollisions(p1, limit);//restarting the priority que
                findCollisions(p2, limit);
        }        
    }
    private static class QueueItem implements Comparable<QueueItem> {
        private final double time;// timing on each Que
        private final Particle p1, p2;// objects involved in Que
        private final int countOne, countTwo;// counts the impacts
        
        
        public QueueItem(double t, Particle p1, Particle p2)// new QueueItem created
        {
            this.time = t;
            this.p1 = p1;
            this.p2 = p2;
            if (p1 != null) countOne = p1.getCollisionCount();
            else            countOne = -1;
            if (p2 != null) countTwo = p2.getCollisionCount();
            else            countTwo = -1;
        }
        
        public int compareTo(QueueItem that)//comparison int
        {
            return Double.compare(this.time, that.time);
        }
                
        //Events wil be invalidated if particles have collided more than
        //when event was predicted
        public boolean isValidated() {
            if (p1 != null && p1.getCollisionCount() != countOne)
                return false;
            if (p2 != null && p2.getCollisionCount() != countTwo)
                return false;
            else
                return true;
        }
    }    
    public static void main(String[] args) throws IOException
    {           
        StdDraw.setCanvasSize(800, 800);                        
        StdDraw.enableDoubleBuffering();
                
         // first check to see if the program was run with the command line argument
        if(args.length < 1) {
            System.out.println("Error, usage: java ClassName inputfile");
            System.exit(1);
        }                   
        Scanner scan = new Scanner(new FileInputStream(args[0]));        
        
        int n = scan.nextInt();
        Particle[] particles = new Particle[n]; 
        /* Initialize particle(s) */
        for (int i = 0; i < n; i++) {
            double rx = scan.nextDouble();
            double ry = scan.nextDouble();
            double vx = scan.nextDouble();
            double vy = scan.nextDouble();
            double radius = scan.nextDouble();
            double mass = scan.nextDouble();
            int r = scan.nextInt();
            int g = scan.nextInt();
            int b = scan.nextInt();
            Color color = new Color(r, g, b);
            particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);                                              
    }        
        //Create new simulation, call simulation of particles inputted
        MDSimulation simulation = new MDSimulation(particles);                                
        simulation.runSimulation(10000);                   
              
    }//End of Main        
}//End of MDSimulation Class