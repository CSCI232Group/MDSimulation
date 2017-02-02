/*
 * 
 * 
 * 
 */
package mdsimulation;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.math.BigDecimal;
import mdsimulation.PriorityQue.QueItem;

/**
 *
 * @author Ian Hecker
 */
public class MDSimulation
{
   
    public static void main(String[] args)
    {   
        /* Set up scale */
        StdDraw.setScale(-2, +2);
        StdDraw.enableDoubleBuffering();
        
        /* Initialize particle(s) */
        PriorityQue PQ = new PriorityQue();
        
        Particle parA = new Particle(0, 1, 0, 0, 0, 0.05, Color.RED);       
        Particle parB = new Particle(-1, 0, 1, .25, 5, 0.05, Color.BLUE); 
        
        Particle[] particleArray = {parA, parB};
        
        for (double t = 0.00; true; t += 0.01)
        {            
            StdDraw.clear();                                                
            
            for(Particle p : particleArray)
            {
                p.drawParticle();
            }                                                
            
            StdDraw.show();
            StdDraw.pause(20);
        }        
    }    
}