
/*  Alexander Kyprianou
 *  Constrained Topo Sort
 */

import java.io.*;
import java.util.*;

public class TopoPath {

    static List<Integer>[] adjacencyList;
    static int[] inDegree;
    static boolean[] visited;
    static List<Integer> validSort;
    static int vertices;
    static boolean flag = true;
    

    public static boolean hasTopoPath(String filename) throws FileNotFoundException
    {
     
     // Read the graph from the file into an adjacency list. The flag ensures
     // that this is not repeated for each recursive call.
     if (flag)
     {
        // Open the given file and save the total number of vertices (the first 
        // int to appear).
         Scanner scanner = new Scanner(new File(filename));
         vertices = scanner.nextInt();


         // Initialize the adjacency list.
         adjacencyList = new List [vertices + 1];
         for (int k = 1; k <= vertices ;k++) 
               adjacencyList[k] = new LinkedList<>();


         // Initialize the inDegree & visited array. Use vertices + 1
         // becuase the vertices in our graph go from 1 to n, inclusive.
         inDegree = new int[vertices + 1]; 
         visited = new boolean[vertices + 1];

         validSort =  new ArrayList<>();

         // Populate the adjacencyList and inDegree array.
         for (int i  = 1; i <= vertices; i++)
         {
             int j = scanner.nextInt(); 
             while (j > 0)
             {
                 int hold = scanner.nextInt();
                 adjacencyList[i].add(hold);
                 inDegree[hold]++;
                 j--;
             }     
         } 
         
         flag = false;
     }
     
    // HERE STARTS THE RECURSIVE BACKTRACK TO GENERATE ALL TOPOLOGICAL SORTS
    // UPDATED CODE FROM ASSIGNMENT 5 "ConstrainedTopoSort"
    
    // Flag when all possibe topological sorts have been tried.
    boolean flagCheck = false;
        
    for (int i = 1; i <=  vertices; i++)
    { 
        // Only consider a vertex who has NOT been visited and whose 
        // in-degree is 0.
        if (!visited[i] && inDegree[i] == 0)
        {
            // Mark the vertex as visited and decrement the in-degree of all
            // adjacent vertices.
            visited[i] = true;
            Iterator<Integer> it = adjacencyList[i].listIterator();
            while (it.hasNext())
                inDegree[it.next()]--;
           
            // Add the vertex to our current topological sort and preform
            // the recursive call. 
            validSort.add(i);
            if (hasTopoPath(filename))
                return true;
                
            // Backtrack to the previous state.
            visited[i] = false;
            it = adjacencyList[i].listIterator();
            while (it.hasNext())
                inDegree[it.next()]++;
            validSort.remove((Integer) i);
                
            // All possible topological sorts found.
            flagCheck = true;
        }    
    }
        
    // Check to see if x comes before y in the current valid sort. If so,
    // return true. Otherwise, return false and keep looking.
    if (!flagCheck)
    {
        if ( isPath() )
            return true;     
    }
        
    return false;
 
    }
    
    
    // This method checks to see if a given topological sort on the graph is 
    // also a path. This is done by iterating through the adjacencyList while 
    // comparing it to the given sort.
    private static boolean isPath()
    {
       int ix = 0;
       boolean myFlag;
       
       if (validSort.size() != vertices)
           return false;
       
       while (ix < vertices - 1)
       {
           
           Iterator<Integer> it = adjacencyList[validSort.get(ix)].listIterator();
           myFlag = false;
           
           while (it.hasNext())
           {
               if (it.next() == validSort.get(ix + 1))
               {
                   myFlag = true;
                   break;
               }
           }
           
           if (!myFlag)
               return false;
           
           ix++;
       }
       
       return true;
    
    }
}
