import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;
 
/**
 * Self-Stabilizing Algorithm
 * Based on the paper "Self-Stabilizing Algorithms for Unfriendly Partitions"
 * by Sandra Hedetniemi, Stephen Hedetniemi, K.E. Kennedy, and Alice McRae.
 *
 * This code is an implementation of the first algorithm from the paper
 * titled "Unfriendly".
 *
 * @author Brooke Tibbett
 * @version 3/25/19
 *
 */
public class UnfriendlyPartition
{

    public static void main(String [] args)
    {
        if (args.length == 0) {
           System.out.println("Usage: java UnfriendlyPartition <filename>"); 
           System.exit(0);
        }

        // Read the graph 
        // Uses AdjacencyList class by Dr. Alice McRae
        AdjacencyLists graph = new AdjacencyLists(args[0]);
        int n = graph.order();
       
        // Color array to match the color of each vertex.
        // The colors are 1 & 0. 
        boolean [] color = new int[n]; 

        LinkedList<Integer> queue = new LinkedList<Integer>();

        Random rand = new Random();

        // Initialize to random coloring.
        for (int i = 0; i < n; i++)
        {
            color[i] = rand.nextInt(1);
        }

          
        for (int i = 0; i < n; i++)
        {
            if (!marked[i])
            {
                numComponents++;
                queue.add(i);
                marked[i] = true;
                numMarked++;
                
                while (!queue.isEmpty() && numMarked < n)
                {
                    int v = queue.remove();
                    Iterator<Integer> it = graph.neighbors(v);
                    while (it.hasNext())
                    {
                        int vertex = it.next();
                        if (!marked[vertex])
                        {
                            numMarked++;
                            marked[vertex] = true;
                            queue.add(vertex);
                        }
                    }
                }
            }
            
        }
        System.out.println("There are " + numComponents + " components.");
    }
}
