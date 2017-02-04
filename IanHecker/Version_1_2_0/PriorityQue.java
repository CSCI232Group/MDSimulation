/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mdsimulation;

/**
 *
 * @author Ian
 */
public class PriorityQue
{
    public static double NO_COLLISION = -12345;
    private QueItem[] CollisionQue;
    private Node first;
    private Node last;
    private int count;
    
    public PriorityQue(){}
    
    public boolean isEmpty()    {return first == null;}
    public int count()          {return count;}        
    
    public void insertCollision(Particle p, Particle p2, double time)
    {
        if(time != NO_COLLISION)
        {
            QueItem qi = new QueItem(p, p2, time);
            
            Node oldLast = last;
            last = new Node();
            last.qi = qi;
            last.next = null;
            if(isEmpty())   {first = last;}
            else            {oldLast.next = last;}
            count++;
        }        
    }
    public QueItem delCollision()
    {
        QueItem qi = first.qi;                        
        first = first.next;
        count--;
        if(isEmpty())   {last = null;}
        return qi;
    }
    /* Will find item with soonest time; delete other items in way*/
    public QueItem prioritize()
    {
        Node impendingCol = first;
        
        for(int i = count; i < count+1; i++)
        {
            if(impendingCol.next != null)
            {
                if(impendingCol.getTime() > first.next.getTime())
                {
                    impendingCol = impendingCol.next;
                    delCollision();
                }
                else if(impendingCol.getTime() < first.next.getTime())
                {
                    Node newNext;//Temp holder
                    newNext = impendingCol.next.next;//impendingCol's next item is next item's next
                    impendingCol.next.next = null;//Remove impendingCol's next item's pointer for deletion
                    impendingCol.next = newNext;//move next, next of impendingCol back to next
                    count--;
                }
            }
        }
        return impendingCol.qi;
    }
    private class Node extends QueItem
    {
        QueItem qi;
        Node next;
    }
    /* Inner class that holds data for each collision in PQ */
    public class QueItem
    {
        private Particle p; //Particle involved in collision
        private Particle p2;
        private boolean invalidated; //Will event occur or has previous collison rendered invalid
        private double time; //When event will occur        
        
        private QueItem(){}
        
        private QueItem(Particle p, Particle p2, double time)
        {
            this.p = p; this. p2 = p2;            
            this.time = time;
            this.invalidated = false;
        }
        
        public Particle getParOne()
            {return p;}
        
        public Particle getParTwo()
            {return p2;}
        
        public double getTime()
            {return time;}
        
        public boolean isInvalidated()
            {return invalidated;}        
    }    
}
