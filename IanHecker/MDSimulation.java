/*
 * 
 * 
 * 
 */
package mdsimulation;

import edu.princeton.cs.algs4.StdDraw;
import java.math.BigDecimal;
import mdsimulation.PriorityQue.QueItem;

/**
 *
 * @author Ian Hecker
 */
public class MDSimulation
{
    static double collisionTime;
    
    public static void main(String[] args)
    {   
        /* Set up scale */
        StdDraw.setScale(-2, +2);
        StdDraw.enableDoubleBuffering();
        
        /* Initialize partticle(s) */
        PriorityQue PQ = new PriorityQue();
        
        Particle parA = new Particle(0, 1, 0, 0, 0, 0.05);       
        Particle parB = new Particle(-1, 0, 1, .25, 5, 0.05);
        
        Particle[] pArray = {parA, parB};
        PQ.findCollisions(pArray, 0.0);//Initialize PQ with collision events                
        
        QueItem nextCollision = findNextCollision(PQ);                        
        
        for (double t = 0.00; true; t += 0.01)
        {            
            StdDraw.clear();
            
            System.out.printf("%f, %f\n", t, collisionTime);            
            
            BigDecimal ct = new BigDecimal(collisionTime);
            BigDecimal time = new BigDecimal(t);
            ct = ct.setScale(2, BigDecimal.ROUND_DOWN);//Double.compare(collisionTime, t) == 0
            time = time.setScale(2, BigDecimal.ROUND_DOWN);            
            
            if(ct.equals(time))
            {            
                parB.bounceX();                                
                
                PQ.delCollision();//Delete collision from que that just occurred
                                  //Updates velocities of particles involved                
                PQ.findCollisions(pArray, t);//Find new collisions from updated velocities
                
                findNextCollision(PQ);//Find next impending collision
            }                        
            
            /* Draw Particles */
            StdDraw.filledCircle(parA.getRX(), parA.getRY(), parA.getRadius());
            StdDraw.filledCircle(parB.getRX(), parB.getRY(), parB.getRadius());
            
            for(Particle p : pArray)
            {
                p.updatePosition(0.01);
            }
            
            StdDraw.show();
            StdDraw.pause(20);
        }        
    }
    public static QueItem findNextCollision(PriorityQue PQ)
    {
        PriorityQue.QueItem nextCollision = PQ.prioritize();            
        collisionTime = nextCollision.getTime();
        return nextCollision;
    }    
}