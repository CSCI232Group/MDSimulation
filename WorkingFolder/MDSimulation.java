/*
 * 
 * 
 * 
 */
package mdsimulation;


import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Ian Hecker
 */
public class MDSimulation
{              
    private MinPQ<QueueItem> PQ;
    private double t = 0.0;
    private Particle[] particleArray;
    
    public MDSimulation(Particle[] particles)
    {
        this.particleArray = particles.clone();        
    }        
    public void findCollisions(Particle p1, double limit)
    {     
        if(p1 == null) return;
        
        for(int iter = 0; iter < particleArray.length; iter++)
        {
            double dt = p1.timeToParticleCollision(particleArray[iter]);
            if(t + dt <= limit)              
                PQ.insert(new QueueItem(t + dt, p1, particleArray[iter]));
        }
        double dtx = p1.timeToVertWall();
        double dty = p1.timeToHorizWall();
        if(t + dtx <= limit){PQ.insert(new QueueItem(t + dtx, p1, null));}
        if(t + dty <= limit){PQ.insert(new QueueItem(t + dty, null, p1));}        
    }
    private void reDraw(double limit)
    {
        StdDraw.clear();
        for(Particle p : particleArray)
            {p.drawParticle();}
        StdDraw.show();
        StdDraw.pause(20);
            if(t <= limit)
                {PQ.insert(new QueueItem(t + 1.0 / 0.5, null, null));}
    }
    public void runSimulation(double limit)
    { 
        PQ = new MinPQ<QueueItem>();
        for(Particle p : particleArray)
            {findCollisions(p, limit);}
        
        PQ.insert(new QueueItem(0, null, null));
        
        while(!PQ.isEmpty())
        {               
            QueueItem ev = PQ.delMin();
            if(!ev.isValidated()) continue;//If valid, continue
            Particle p1 = ev.p1;
            Particle p2 = ev.p2;
        
            //for(double t = 0.00; true; t += 0.01){}                
                for(Particle p : particleArray)                
                    {p.updatePosition(ev.time - t);}                
                t = ev.time;
                    
                if(p1 != null && p2 != null)      {p1.bounceOffOther(p2);}               
                else if(p1 != null && p2 == null) {p1.bounceOffVert();}
                else if(p1 == null && p2 != null) {p2.bounceOffHoriz();}
                else if(p1 == null && p2 == null) {reDraw(limit);}
                    
                findCollisions(p1, limit);
                findCollisions(p2, limit);
        }        
    }
    private static class QueueItem implements Comparable<QueueItem> {
        private final double time;
        private final Particle p1, p2;
        private final int countOne, countTwo;
        
        
        public QueueItem(double t, Particle p1, Particle p2) {
            this.time = t;
            this.p1 = p1;
            this.p2 = p2;
            if (p1 != null) countOne = p1.getCollisionCount();
            else            countOne = -1;
            if (p2 != null) countTwo = p2.getCollisionCount();
            else            countTwo = -1;
        }
        
        public int compareTo(QueueItem that) {
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
        //Setting scale of window DO NOT CHANGE
        StdDraw.setCanvasSize(800, 800);                        
        StdDraw.enableDoubleBuffering();
        
        
        /* Initialize particle(s) *//*
        Particle parA = new Particle(-2, 0, 0.1, 0.01, 0.05, 0.05, Color.RED);       
        Particle parB = new Particle(-2, -2, 0, 0, 0.05, 0.05, Color.BLUE);
        Particle parC = new Particle(2, 0, -0.01, 0, 0.5, 0.5, Color.ORANGE);
        Particle[] particles = {parA, parB, parC};*/
        
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
            double rx     = scan.nextDouble();
            double ry     = scan.nextDouble();
            double vx     = scan.nextDouble();
            double vy     = scan.nextDouble();
            double radius = scan.nextDouble();
            double mass   = scan.nextDouble();
            int r         = scan.nextInt();
            int g         = scan.nextInt();
            int b         = scan.nextInt();
            Color color   = new Color(r, g, b);
            particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);                                
              
    }
        
        //Create new simulation, call simulation of particles inputted
        MDSimulation simulation = new MDSimulation(particles);                                
        simulation.runSimulation(10000);                   
              
    }//End of Main        
}//End of MDSimulation Class