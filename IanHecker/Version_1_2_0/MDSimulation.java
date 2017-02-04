/*
 * 
 * 
 * 
 */
package mdsimulation;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import mdsimulation.PriorityQue.QueItem;

/**
 *
 * @author Ian Hecker
 */
public class MDSimulation
{
    private final double TIME_LIMIT = 100.0;
    private PriorityQue PQ;
    private double t = 0.0;
    private Particle[] particleArray;
    
    public MDSimulation(Particle[] particles)
    {
        this.particleArray = particles;        
    }
    
    public static void main(String[] args)
    {   
        /* Set up scale */
        StdDraw.setScale(-2, +2);
        StdDraw.enableDoubleBuffering();
        
        /* Initialize particle(s) */
        Particle parA = new Particle(0, 1, 0, 0, 5, 0.05, Color.RED);       
        Particle parB = new Particle(-1, 0, 1, .25, 5, 0.05, Color.BLUE);
        Particle parC = new Particle(1, 0, 0, 0, 5, 0.05, Color.ORANGE);
        Particle[] particles = {parA, parB, parC};
        
        MDSimulation simulation = new MDSimulation(particles);                                
        simulation.runSimulation();                    
              
    }//End of Main
    public void runSimulation()
    {             
        PQ = new PriorityQue();
        findCollisions(particleArray, t);
        
        PQ.insertCollision(null, null, 0);
        
        while(!PQ.isEmpty())
        {                        
            QueItem qi = PQ.delCollision();
            if(qi.isInvalidated()) continue;//If valid, continue
            Particle a = qi.getParOne();
            Particle b = qi.getParTwo();
        
            //for(double t = 0.00; true; t += 0.01){}                
                for(Particle p : particleArray)                
                {p.updatePosition(qi.getTime() - t);}                
                t = qi.getTime();
                    
                if(a != null && b != null)
                    {a.bounceOffOther(b);}
                
                else if(a != null && b == null)
                    {
                        if(a.whichWall() == "both")
                            {a.bounceOffVert();
                             a.bounceOffHoriz();}
                        else if(a.whichWall() == "vert")
                            {a.bounceOffVert();}
                        else if(a.whichWall() == "horiz")
                            {a.bounceOffHoriz();}
                        else
                            {StdDraw.clear();
                            for(Particle p : particleArray)
                            {p.drawParticle();}
                            StdDraw.show();
                            StdDraw.pause(20);}
                    }
                findCollisions(particleArray, t);
        }
    }
    public void findCollisions(Particle[] allParticles, double time)
    {
        for(Particle p : allParticles)
        {
            for(int iter = 0; iter < allParticles.length; iter++)
            {                
                double deltaT = p.timeToParticleCollision(allParticles[iter]);
                double deltaTX = p.timeToVertWall();
                double deltaTY = p.timeToHorizWall();
                
                if(deltaT + time < TIME_LIMIT){
                    Particle p2 = allParticles[iter];
                    PQ.insertCollision(p, p2, deltaT + time);}
                                    
                if(deltaTX + time < TIME_LIMIT){                    
                    PQ.insertCollision(p, null, deltaTX + time);}
                
                if(deltaTY + time < TIME_LIMIT){                    
                    PQ.insertCollision(p, null, deltaTX + time);}
            }
        }
    }
    
}//End of MDSimulation Class