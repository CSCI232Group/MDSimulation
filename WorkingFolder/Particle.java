/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mdsimulation;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;

/*@author Ian 
 * Formatting of formulas in the methods involving
 * particle-to-particle collision follows layout of:
 * ~Robert Sedgewick~
 * ~Kevin Wayne~
 * in their design of a Particle class, for simplicity's
 * sake and to allow better understanding of physics involved
 * with my group for lab1. 
 * The rest of the class is my own original design
 */
public class Particle
{
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    
    private double rx, ry;
    private double vx, vy;
    private final double mass, radius;
    private int collisionCount;
    private final Color color;
            
    public Particle(double rx, double ry, double vx, double vy, double mass, double radius, Color color)
    {
        this.rx = rx; this.ry = ry;
        this.vx = vx; this.vy = vy;
        this.mass = mass; this.radius = 100*radius;
        this.color = color;
    }

    public void drawParticle()
    {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(rx, ry, radius);
    }
    
    public String whichWall()
    {
        if(rx == Math.abs(2) && ry == Math.abs(2))
            return "both";
        else if(rx == Math.abs(2))
            return "vert";
        else
            return "horiz";
    }
                                    
    public void updatePosition(double increment)
        {rx += vx * increment;
        ry += vy * increment;}
    
    public double timeToVertWall()
    {      
        double dt;
        if(vx > 0)          dt = (2 - radius - rx) / vx;        
        else if (vx < 0)    dt = (-2 + radius - rx) / vx;        
        else                dt = INFINITY;                        
        return dt;
    }
    public double timeToHorizWall()
    {             
        double dt;
        if(vy > 0)          dt = (2 - radius - ry) / vy;        
        else if (vy < 0)    dt = (-2 + radius - ry) / vy;       
        else                dt = INFINITY;
        return dt;
    }
    public double timeToParticleCollision(Particle other)
    {
            if(other == this) return INFINITY;
        double dx, dy, dvx, dvy, dvdr, drdr, dvdv, sigma, d;
        dx = other.rx - this.rx;
        dy = other.ry - this.ry;
        dvx = other.vx - this.vx;
        dvy = other.vy - this.vy;            
        dvdr = dx*dvx + dy*dvy;
            if(dvdr > 0) return INFINITY;
        dvdv = dvx*dvx + dvy*dvy;
        drdr = dx*dx + dy*dy;
        sigma = other.radius + this.radius;
        d = dvdr*dvdr - dvdv * (drdr - sigma*sigma);
            if(d < 0) return INFINITY;
            return -(dvdr + Math.sqrt(d)) / dvdv;
        
    }
    public void bounceOffVert()
        {vx = -vx; collisionCount++;}
    
    public void bounceOffHoriz()
        {vy = -vy; collisionCount++;}
    
    public void bounceOffOther(Particle other)
    {
        double dx, dy, dvx, dvy, dmass , dvdr, distance, magnitude, forceY, forceX;
        dx = other.rx - this.rx;
        dy = other.ry - this.ry;
        dvx = other.vx - this.vx;
        dvy = other.vy - this.vy;
        dmass = other.mass + this.mass;
        dvdr = dx*dvx + dy*dvy;
        distance = other.radius + this.radius;
        magnitude = 2 * other.mass * this.mass * dvdr / ((dmass) * distance);
        forceX = magnitude * dx / distance;
        forceY = magnitude * dy / distance;
        /*-----------------------------*/
        other.vx -= forceX / other.mass;
        other.vy -= forceY / other.mass;
        this.vx += forceX / this.mass;
        this.vy += forceY / this.mass;
        
        other.collisionCount++;
        this.collisionCount++;        
    }
    public double kineticEnergy()
        {return 0.5 * mass * (vx*vx + vy*vy);}        
    
    public int getCollisionCount()
        {return collisionCount;}
}//End of Particle Class
