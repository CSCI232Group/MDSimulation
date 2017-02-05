/*
 * 
 * 
 * 
 */
package mdsimulation;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
/**
 *
 * @author Ian Hecker
 */
public class MDSimulation
{
    private final double TIME_LIMIT = 100.0;
    
    private MinPQ<Event> PQ;
    private double t = 0.0;
    private Particle[] particleArray;
    
    public MDSimulation(Particle[] particles)
    {
        this.particleArray = particles;        
    }        
    public void findCollisions(Particle p1)
    {     
        if(p1 == null) return;
        
        for(int iter = 0; iter < particleArray.length; iter++)
        {
            double dt = p1.timeToParticleCollision(particleArray[iter]);
            if(t + dt <= TIME_LIMIT)              
                PQ.insert(new Event(t, p1, particleArray[iter]));
        }
        double dtx = p1.timeToVertWall();
        double dty = p1.timeToHorizWall();
        if(t + dtx < TIME_LIMIT){PQ.insert(new Event(t + dtx, p1, null));}
        if(t + dty < TIME_LIMIT){PQ.insert(new Event(t + dty, null, p1));}        
    }
    private void reDraw()
    {
        StdDraw.clear();
        for(Particle p : particleArray)
            {p.drawParticle();}
        StdDraw.show();
        StdDraw.pause(20);
            if(t < TIME_LIMIT)
                {PQ.insert(new Event(t + 1.0 / 0.5, null, null));}
    }
    public void runSimulation()
    { 
        PQ = new MinPQ<Event>();
        for(Particle p : particleArray)
            {findCollisions(p);}
        
        PQ.insert(new Event(0, null, null));
        
        while(!PQ.isEmpty())
        {               
            Event ev = PQ.delMin();
            if(!ev.isValid()) continue;//If valid, continue
            Particle a = ev.a;
            Particle b = ev.b;
        
            //for(double t = 0.00; true; t += 0.01){}                
                for(Particle p : particleArray)                
                    {p.updatePosition(ev.time - t);}                
                t = ev.time;
                    
                if(a != null && b != null)      {a.bounceOffOther(b);}               
                else if(a != null && b == null) {a.bounceOffVert();}
                else if(a == null && b != null) {b.bounceOffHoriz();}
                else if(a == null && b == null) {reDraw();}
                    
                findCollisions(a);
                findCollisions(b);
        }
        
    }
    private static class Event implements Comparable<Event> {
        private final double time;         // time that event is scheduled to occur
        private final Particle a, b;       // particles involved in event, possibly null
        private final int countA, countB;  // collision counts at event creation                
        
        // create a new event to occur at time t involving a and b
        public Event(double t, Particle a, Particle b) {
            this.time = t;
            this.a    = a;
            this.b    = b;
            if (a != null) countA = a.getCollisionCount();
            else           countA = -1;
            if (b != null) countB = b.getCollisionCount();
            else           countB = -1;
        }

        // compare times when two events will occur
        public int compareTo(Event that) {
            return Double.compare(this.time, that.time);
        }
                
        // has any collision occurred between when event was created and now?
        public boolean isValid() {
            if (a != null && a.getCollisionCount() != countA) return false;
            if (b != null && b.getCollisionCount() != countB) return false;
            return true;
        }
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
}//End of MDSimulation Class