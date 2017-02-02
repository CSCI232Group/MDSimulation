/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mdsimulation;

import static mdsimulation.PriorityQue.NO_COLLISION;

/**
 *
 * @author Ian
 */
public class Particle
{
    private double rx, ry;
    private double vx, vy;
    private double mass, radius;
    private int collisionCount;
            
    public Particle(double rx, double ry, double vx, double vy, double mass, double radius)
    {
        this.rx = rx; this.ry = ry;
        this.vx = vx; this.vy = vy;
        this.mass = mass; this.radius = radius;
    }
    public double getRX()
        {return rx;}
    
    public double getRY()
        {return ry;}
    
    public double getVX()
        {return vx;}
    
    public double getVY()
        {return vy;}
    
    public double getMass()
        {return mass;}
    
    public double getRadius()
        {return radius;}
    
    public void setRX(double rx)//Testing only
        {this.rx = rx;}
    
    public void setRY(double ry)//Testing only
        {this.ry = ry;}
    
    public void setVY()//TESTING ONLY
        {vy = -1 * vy;}
    
    public double getFutureRX(double t)
        {return rx + (t * vx);}
    
    public double getFutureRY(double t)
        {return ry + (t * vy);}
    
    public void updatePosition(double increment)
        {rx += vx * increment; ry += vy * increment;}
    
    public double collidesX(double t)
    {
        double time = t;
        
        if(vx > 0)          {time += (2 - radius - rx) / vx;}
        
        else if (vx < 0)    {time += (-2 + radius - rx) / vx;}
        
        else if(vx == 0)    {time = NO_COLLISION;}        
        
            return round(time);
    }
    public double collidesY(double t)
    {
        double time = t;
        
        if(vy > 0)          {time += (2 - radius - ry) / vy;}
        
        else if (vy < 0)    {time += (-2 + radius - ry) / vy;}
        
        else if(vy == 0)    {time = NO_COLLISION;}        
        
            return round(time);
    }
    public double collides()
    {
        return time;
    }
    public void bounceX()
        {vx = -vx;}
    public void bounceY()
        {vy = -vy;}
    public void bounce(Particle b)
    {
    
    }
    public double round(double time)
    {
        long factor = (long) Math.pow(10, 2/*places to round to*/);
        time = time * factor;
        long tmp = Math.round(time);
        return (double) tmp / factor;
    }
    public void updateCollisionCount()
        {collisionCount++;}
    
    public int getCollisionCount()
        {return collisionCount;}
}//End of Particle Class
