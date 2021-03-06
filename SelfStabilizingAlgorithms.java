import java.util.LinkedList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Self-Stabilizing Algorithms
 * First three algorithms based on the paper 
 * "Self-Stablizing Algorithms for Unfriendly Partitions"
 * by Sandra Hedetniemi, Stephen Hedetniemi, K.E. Kennedy, and Alice McRae.
 * 
 * Last algorithm based on "Maximal matching stabilizes in time O(m)" by 
 * Stephen Hedetniemi, David Jacobs, Pradip Srimani.
 *
 * This code is supposed to work like a wrapper for a self-stabilizing
 * algorithm. This code will build the graph based on a text file and 
 * do the setup to run an algorithms which are then implemented as methods.
 * 
 * @author Brooke Tibbett
 * @version 3/25/19
 */

public class SelfStabilizingAlgorithms
{
    private AdjacencyLists graph;
    private int order;
    private int [] color;
    private int [] matches;
    private ArrayList<Integer> ordering;
    boolean unstable;
    Random rand;
    long startTime;
    long runTime;

    public SelfStabilizingAlgorithms(String filename, boolean maximal)
    {
        // Read the graph
        graph = new AdjacencyLists(filename);
        // Get the order of the graph
        order = graph.order();
        // Color array will hold the colors of each vertex;
        color = new int[order];
        matches = new int[order];
        // Array to hold random permutation of nodes
        ordering = new ArrayList<Integer>();
        // Flag to know when the network is stable
        unstable = true;
        // Random number generator for random coloring
        rand = new Random();
        // Randomly color graph
        initializeGraph();
        initializeOrdering();
        initializeMatches();
        System.out.println("-------------------------------------------");
        System.out.println("       Unfriendly Partition Algorithms     ");
        System.out.println("-------------------------------------------");
        System.out.println("Starting network: " + Arrays.toString(color));
        startTime = System.nanoTime();
        runUnfriendly();
        runTime = System.nanoTime() - startTime;
        System.out.println("Unfriendly graph: " + Arrays.toString(color));
        System.out.println("Ran in " + runTime + " ns.\n");
        initializeGraph();
        unstable = true;
        System.out.println("Starting network: " + Arrays.toString(color));
        startTime = System.nanoTime();
        runUnfriendlier();
        runTime = System.nanoTime() - startTime;
        System.out.println("Unfriendlier graph: " + Arrays.toString(color));
        System.out.println("Ran in " + runTime + " ns.\n");
        initializeGraph();
        unstable = true;
        System.out.println("Starting network: " + Arrays.toString(color));
        startTime = System.nanoTime();
        runMoreUnfriendly();
        runTime = System.nanoTime() - startTime;
        System.out.println("More Unfriendly graph: " + Arrays.toString(color));
        System.out.println("Ran in " + runTime + " ns.");
        if(maximal)
        {
            initializeGraph();
            System.out.println("-------------------------------------------");
            System.out.println("         Maximal Matching Algorithm        ");
            System.out.println("-------------------------------------------");
            unstable = true;
            System.out.println("Starting network: " + Arrays.toString(matches));
            startTime = System.nanoTime();
            runMaximalMatching();
            runTime = System.nanoTime() - startTime;
            System.out.println("Maximal matching: " + Arrays.toString(matches));
            System.out.println("Ran in " + runTime + " ns.");
        }
    }

    /**
    * Initialize the graph to a random coloring
    */
    public void initializeGraph()
    {
        for(int i = 0; i < order; i++)
        {
            color[i] = rand.nextInt(2);
        }
    }

    /**
    * Initialize the matches to -1
    */
    public void initializeMatches()
    {
        for(int i = 0; i < order; i++)
        {
            matches[i] = -1;
        }
    }

    /**
     * Initialize ordering arraylist
     */
    public void initializeOrdering()
    {
        for(int i = 0; i < order; i++)
        {
            ordering.add(i);
        }
    }

    /**
    * Create random permutation of graph
    */
    public void shuffleGraph()
    {
        Collections.shuffle(ordering);
    }

    /**
    * This code is an implementation of the first algorithm from the paper
    * titled "Unfriendly"
    */
    public void runUnfriendly()
    {
        int blue;
        int red;

        while(unstable)
        {
            //reset unstable flag
            unstable = false;
            //reshuffle ordering of permutation
            Collections.shuffle(ordering);

            for (int i = 0; i < order; i++)
            {
                //current vertex
                int v = ordering.get(i);
                //reset neighbor color count
                blue = 0;
                red = 0;

                //loop through neighbors and count how many of each color
                Iterator<Integer> it = graph.neighbors(v);
                while(it.hasNext())
                {
                    int neighbor = it.next();
                    if(color[neighbor] == 1) blue++;
                    else red++;
                }

                if(color[v] == 1 && blue > red)
                {
                    color[v] = 0;
                    unstable = true;
                }
                else if(color[v] == 0 && red > blue)
                {
                    color[v] = 1;
                    unstable = true;
                }
            }
        } 
    }

    /**
    * This code is an implementation of the first algorithm from the paper
    * titled "Unfriendlier"
    */
    public void runUnfriendlier()
    {
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

            for (int i = 0; i < order; i++)
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
    }

    /**
    * This code is an implementation of the first algorithm from the paper
    * titled "More Unfriendly"
    */
    public void runMoreUnfriendly()
    {
        int blue;
        int red;
        int blue2 = 0;
        int red2 = 0;
        Iterator<Integer> it;
        Iterator<Integer> it2;
        Iterator<Integer> it3;
        boolean redCond;
        boolean blueCond;

        while(unstable)
        {
            //reset unstable flag
            unstable = false;
            //reshuffle ordering of permutation
            Collections.shuffle(ordering);

            for (int i = 0; i < order; i++)
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
                    color[v] = 0;
                    unstable = true;
                }
                else if(color[v] == 0 && red > blue)
                {
                    color[v] = 1;
                    unstable = true;
                }else if(color[v] == 1 && red == blue)
                {
                    it2 = graph.neighbors(v);
                    redCond = true;
                    blueCond = true;
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
                        if(color[neighbor2] == 0 && red2 != blue2) redCond = false;
                        if(color[neighbor2] == 1 && blue2 > red2) blueCond = false;
                    }
                    if(redCond && blueCond)
                    {
                        color[v] = 0;
                        unstable = true;
                    }

                }else if(color[v] == 0 && red == blue)
                {
                    it2 = graph.neighbors(v);
                    redCond = true;
                    blueCond = true;
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
                        if(color[neighbor2] == 0 && red2 > blue2) redCond = false;
                        if(color[neighbor2] == 1 && blue2 != red2) blueCond = false;
                    }
                    if(redCond && blueCond)
                    {
                        color[v] = 1;
                        unstable = true;
                    }
                }
            }
        }

    }

    /**
    * This code is an implementation of the Maximal Matching Algorithm in
    * the second paper referenced.
    */
    public void runMaximalMatching()
    {
        //boolean for rule 1
        boolean rule1 = false;
        //needed to hold a neighbor with -1 match
        int nullneighbor = -1;

        while(unstable)
        {
            //reset unstable flag
            unstable = false;
            //reshuffle ordering of permutation
            Collections.shuffle(ordering);

            for (int i = 0; i < order; i++)
            {
                //current vertex
                int v = ordering.get(i);
                //get match of current vertex
                int match = matches[v];
                //Rule 3: if v is matched to j and j is matched to antoher vertex
                //then set v's match to -1
                if(match != -1 && matches[match] != v && matches[match] != -1)
                {
                    matches[v] = -1;
                    unstable = true;
                }
                //match for v is null for Rule 1 & 2
                else if(match == -1)
                {
                    //reset rule1 flag
                    rule1 = false;
                    //reset nullneighbor
                    nullneighbor = -1;
                    //go through neighbors of v
                    Iterator<Integer> it = graph.neighbors(v);
                    while(it.hasNext())
                    {
                        int neighbor = it.next();
                        //Rule 1: if one of v's neighbors, j, has v as its match
                        //then set v's match to j
                        if(matches[neighbor] == v)
                        {
                            matches[v] = neighbor;
                            unstable = true;
                            rule1 = true;
                            break;
                        }
                        //keep a neighbor with -1 match for if/when v doesn't move on rule 1 
                        else if(matches[neighbor] == -1) nullneighbor = neighbor;
                    }
                    //if rule 1 didn't happen and rule 2
                    //Rule 2: if v has a neighbor with -1 match then make that neighbor
                    //v's match
                    if(!rule1 && nullneighbor != -1)
                    {
                        matches[v] = nullneighbor;
                        unstable = true;
                    }
                }
            }
        } 
    }

    public static void main(String[] args)
    {
        if (args.length == 0 || (args.length == 2 && !args[1].equals("-m")))
        {
            System.out.println("Usage: java SelfStabilizingAlgorithms <filename> (options: -m)");
            System.exit(0);
        }

        SelfStabilizingAlgorithms st = new SelfStabilizingAlgorithms(args[0],(args.length == 2 && args[1].equals("-m")));
    }
}
