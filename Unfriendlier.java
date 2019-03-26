import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList; 

/**
 * Self-Stabilizing Algorithm
 * Based on the paper "Self-Stabilizing Algorithms for Unfriendly Partitions"
 * by Sandra Hedetniemi, Stephen Hedetniemi, K.E. Kennedy, and Alice McRae.
 *
 * This code is an implementation of the first algorithm from the paper
 * titled "Unfriendlier".
 *
 * @author Brooke Tibbett
 * @version 3/26/19
 *
 */
public class Unfriendlier
{

    public static void main(String [] args)
    {
        if (args.length == 0) {
           System.out.println("Usage: java Unfriendlier <filename>"); 
           System.exit(0);
        }

        // Read the graph 
        // Uses AdjacencyList class by Dr. Alice McRae
        AdjacencyLists graph = new AdjacencyLists(args[0]);
        int n = graph.order();
       
        // Color array to match the color of each vertex.
        // The colors are 1 & 0. 
        // 1 is equivalent to Blue (from the paper)
        // 0 is equivalent to Red (from the paper)
        int [] color = new int[n]; 
        // Array to hold random permutation of nodes.
        ArrayList<Integer> ordering = new ArrayList<Integer>();
        // Flag for loop to determine whether the network is stable;
        boolean unstable = true;
        
        LinkedList<Integer> queue = new LinkedList<Integer>();

        Random rand = new Random();

        // Initialize to random coloring and starting order.
        for (int i = 0; i < n; i++)
        {
            color[i] = rand.nextInt(2);
            ordering.add(i);
        }

        System.out.println("Starting network: " + Arrays.toString(color));

        Collections.shuffle(ordering);        

        int blue = 0;
        int red = 0;
        int blue2 = 0;
        int red2 = 0;
        Iterator<Integer> it;
        Iterator<Integer> it2;
        Iterator<Integer> it3;
        boolean neighborsCond;

        while(unstable)
        {
            //reset unstable flag
            unstable = false;
            //reshuffle ordering of permutation
            Collections.shuffle(ordering);

            for (int i = 0; i < n; i++)
            {
                //current vertex
                int v = ordering.get(i);
                //reset neighbor color count
                blue = 0;
                red = 0;

                //loop through neighbors and count how many of each color
                it = graph.neighbors(v);
                while(it.hasNext())
                {
                    int neighbor = it.next();
                    if(color[neighbor] == 1) blue++;
                    else red++;
                }
                
                if(color[v] == 1 && blue > red) 
                {
                    it2 = graph.neighbors(v);
                    neighborsCond = true;
                    while(it2.hasNext())
                    {
                        int neighbor2 = it2.next();
                        it3 = graph.neighbors(neighbor2);
                        while(it3.hasNext())
                        {
                            blue2 = 0;
                            red2 = 0;
                            int neighbor3 = it3.next();
                            if(color[neighbor3] == 1) blue2++;
                            else red2++;
                        }
                        if(blue2 - red2 > blue - red)
                        {
                            neighborsCond = false;
                        }
                    }
                    if(neighborsCond)
                    {
                        color[v] = 0;
                        unstable = true;
                    }
                }
                else if(color[v] == 0 && red > blue) 
                {
                    it2 = graph.neighbors(v);
                    neighborsCond = true;
                    while(it2.hasNext())
                    {
                        int neighbor2 = it2.next();
                        it3 = graph.neighbors(neighbor2);
                        while(it3.hasNext())
                        {
                            blue2 = 0;
                            red2 = 0;
                            int neighbor3 = it3.next();
                            if(color[neighbor3] == 1) blue2++;
                            else red2++;
                        }
                        if(red2 - blue2 > red - blue) neighborsCond = false;
                    }
                    if(neighborsCond)
                    {
                        color[v] = 1;
                        unstable = true;
                    }
                }
            }
        }
        System.out.println("Ending network: " + Arrays.toString(color));
    }
}
















