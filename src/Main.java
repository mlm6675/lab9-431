import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.jar.JarFile;

public class Main {

    public static void main(String[] args) {
        int[][] graph = getGraphFromFile();
                //{{1,2}, {2,3},{3},{}}; //
       // int[][] graph = {{1,2},{2}, {}}; //testing graphs
       // int[][]graph ={{1,3,4}, {2}, {3,4}, {4}, {}}; //testing graphs

        ArrayList<ArrayList<Boolean>> visitedEdge = initializeVisitedEdge(graph);
        //System.out.println(visitedEdge);
        ArrayList<Integer> traversed = new ArrayList<>();

        System.out.println("Cyclomatic complexity: " + calculateCyclomaticComplexity(graph));
        System.out.println("Independent paths: ");

        int currentNode = 0;
        int nextNode = 0;
        int nextNodeIndex = 0;
        int i = 0;
        boolean newTraversal = false;
        traversed.add(currentNode);

        try {
            while (!fullyTraversed(visitedEdge)) //while not all edges have been traversed
            {
                //System.out.println(i++);
                nextNodeIndex = hasNext(graph, visitedEdge, currentNode); //getting the next node index
                if (nextNodeIndex != -1) //if a next node for the given vertex exists
                {
                    nextNode = graph[currentNode][nextNodeIndex]; //set the next node
                    traversed.add(nextNode); //add it to the list
                    visitedEdge.get(currentNode).set(nextNodeIndex, true); //mark the edge as visited
                    currentNode = nextNode; //update the current node

                    newTraversal = true;
                } else //if the current node has no unvisited edges
                {
                    if (newTraversal) {
                        System.out.println(traversed);
                        newTraversal = false;
                    }
                    traversed.remove(traversed.size() - 1); //remove the most recent node from the list
                    currentNode = traversed.get(traversed.size() - 1); //update the current node to be the previous node
                }

                if (fullyTraversed(visitedEdge)) //getting the very last independent path
                {
                    System.out.println(traversed);
                }

                //System.out.println(visitedEdge);
                //System.out.println(dummy);
          /*  if(i>20)
            {
                break;
            }
           */
            }
        }
        catch (Exception e)
        {
            System.out.println("The given graph does not meet specification (one source, one sink)");
        }


        /* this was an attempt using regular depth first search. i realized after that we need to be doing dfs with the edges for this rather than with the vertices
        while(!stack.isEmpty())
        {
            int u = stack.pop();
            dummy.remove((Integer) u);
            if(!visitedNode[u])
            {
                visitedNode[u] = true;
                for(int v : graph[u])
                {
                    if(!visitedNode[v])
                    {
                        stack.push(v);
                        dummy.add(v);
                    }

                }
                //System.out.println(dummy);
            }
        }

        //System.out.println(dummy);
*/

    }


    /**
     *
     * @param graph the given graph
     * @param visitedEdge a boolean ArrayList indicating whether or not each edge has been traversed
     * @param node the node to be checked
     * @return the index of the next node to be checked, -1 if the current node has no more edges to traverse
     */
    static int hasNext(int[][] graph, ArrayList<ArrayList<Boolean>> visitedEdge, int node)
    {
        for(int i = 0; i < visitedEdge.get(node).size(); i++)
        {
            if(!visitedEdge.get(node).get(i)) //if an edge has not been traversed, return its index
            {
                return i;
            }
        }

        return -1; //if all edges have been traversed, return -1
    }

    /**
     *
     * @param graph the given graph
     * @return a boolean array containing "false" at every element, with the same size as the given graph
     */
    static boolean[] initializeVisited(int[][] graph)
    {
        boolean[] visited = new boolean[graph.length];
        for(int i = 0; i< graph.length; i++)
        {
            visited[i] = false;
        }

        return visited;
    }

    /**
     *
     * @param graph the given graph
     * @return a boolean 2d vector containing "false" at every element, with the same size as the given graph
     */
    static ArrayList<ArrayList<Boolean>> initializeVisitedEdge(int[][] graph)
    {
        ArrayList<ArrayList<Boolean>> visitedEdge = new ArrayList<>();

        for(int i = 0; i < graph.length; i++)
        {
            ArrayList<Boolean> dummy = new ArrayList<>();
            for(int j = 0; j < graph[i].length; j++)
            {
                dummy.add(false);
            }
            visitedEdge.add(dummy);
        }
        return visitedEdge;
    }

    /**
     *
     * @param array a 2d boolean vector indicating whether each edge has been traversed
     * @return true if every edge has been traversed, false otherwise
     */
    static boolean fullyTraversed(ArrayList<ArrayList<Boolean>> array)
    {
        for(int i = 0; i < array.size(); i++)
        {
            for(int j = 0; j < array.get(i).size(); j++) {
                if (!array.get(i).get(j)) {
                    return false;
                }
            }
        }

        return true;
    }

    static int calculateCyclomaticComplexity(int[][] graph)
    {
        //|E| - |V| + 2
        int complexity = 0;

        complexity-= graph.length; //adding |V|


        for(int i = 0; i < graph.length; i++) //adding |E|
        {
            complexity+= graph[i].length;
        }

        complexity +=2; //adding 2

        return complexity;
    }

    static int[][] getGraphFromFile()
    {
        System.out.println("Please select the file that contains the input: ");
        JFileChooser userInput = new JFileChooser();

        int result = userInput.showDialog(null, "Select");

        if(result == JFileChooser.APPROVE_OPTION)
        {
            return parseFile(userInput.getSelectedFile()); //parseFile(userInput.getSelectedFile());
        }
        else
        {
            System.out.println("Invalid.");
            System.exit(-1);
        }

        return null;
    }

    static int[][] parseFile(File file) {
        ArrayList<ArrayList<Integer>> vertexArray = new ArrayList<ArrayList<Integer>>();
        try(Scanner fileReader = new Scanner(file)){
            while (fileReader.hasNext()){
                String temp = fileReader.nextLine(), line = temp.substring(1, temp.length()-1);
                vertexArray.add(new ArrayList<Integer>());
                if(temp.length()>2){
                    String[] connections = line.split(",");
                    int currentIndex = vertexArray.size()-1;
                    for (String input : connections) {
                        try{
                            vertexArray.get(currentIndex).add(Integer.parseInt(input));
                        }catch (NumberFormatException e){
                            //Ignore invalid entries
                            continue;
                        }
                    }
                }
            }
        }catch (Exception e){
            System.err.println(e);
            System.exit(-1);
        }

        int[][] result = new int[vertexArray.size()][];
        for (int i = 0; i != vertexArray.size(); i++) {
            int[] arr = convertToArray(vertexArray.get(i));
            result[i] = arr;
        }
        return result;
    }

    private static int[] convertToArray(ArrayList<Integer> integers) {
        int[] result = new int[integers.size()];
        for(int i = 0; i != integers.size(); i++){
            result[i] = integers.get(i);
        }
        return result;
    }

}
