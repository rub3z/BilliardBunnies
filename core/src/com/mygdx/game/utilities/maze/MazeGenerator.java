package com.mygdx.game.utilities.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Maze generator uses to generate a customizable maze.
 */
public class MazeGenerator {

   private Array<Node> unvisitedNode;
   private Array<Node> maze;
   private int row;
   private int column;
   private Node currentCell;
   private Array<Node> stack;

   public MazeGenerator() {
      unvisitedNode = new Array<Node>();
      maze = new Array<Node>();
      row = 0;
      column = 0;
      currentCell = null;
      stack = new Array<Node>();

   }

   /**
    * Use recursive backtracking algorithm to generate a maze.
    * @param row number of row.
    * @param column number of column.
    * @param scale how big should each cell be. A minimum of 3x3.
    * @return a complete maze data with 0 represents a empty space and 1 represents a wall.
    */
   public int[][] generateMaze(int row, int column, int scale) {
      this.row = row;
      this.column = column;
      if(scale<3){
         scale=3;
      }


      unvisitedNode.clear();
      maze.clear();
      for (int y = 0; y < row; y++) {
         for (int x = 0; x < column; x++) {
            unvisitedNode.add(new Node(y, x));
         }
      }
      maze.addAll(unvisitedNode);

      currentCell = getNode(MathUtils.random(row - 1), MathUtils.random(column - 1));
      unvisitedNode.removeValue(currentCell, true);
      currentCell.visited=true;

      while (unvisitedNode.size>0){
         if(hasUnvisitedNeighbours(currentCell)){
            Node nextCell=getAnUnvisitedNeighbour(currentCell);
            stack.add(currentCell);
            if(nextCell.columnIndex<currentCell.columnIndex){
               nextCell.walls.put(Node.RIGHT,0);
               currentCell.walls.put(Node.LEFT,0);
            }

            if(nextCell.columnIndex>currentCell.columnIndex){
               nextCell.walls.put(Node.LEFT,0);
               currentCell.walls.put(Node.RIGHT,0);
            }

            if(nextCell.rowIndex<currentCell.rowIndex){
               nextCell.walls.put(Node.DOWN,0);
               currentCell.walls.put(Node.UP,0);
            }

            if(nextCell.rowIndex>currentCell.rowIndex){
               nextCell.walls.put(Node.UP,0);
               currentCell.walls.put(Node.DOWN,0);
            }
            currentCell=nextCell;
            currentCell.visited=true;
            unvisitedNode.removeValue(currentCell,true);
         }else if(stack.size>0){
            currentCell=stack.pop();
         }
      }

      int [][] temp = new int[this.row*scale][this.column*scale];

      for(int i =0; i< this.row;i++){
         for(int j =0; j< this.column;j++){

            int[][] t = nodeToArray(getNode(i,j),scale);

            for(int y=0; y<t.length;y++){
               for(int x=0; x<t.length;x++){
                  temp[i*scale+y][j*scale+x]=t[y][x];
               }
            }

         }
      }
      return temp;


   }

   /**
    * Allows the use of 2D coordination to access nodes contain in 1D array.
    * @param rowIndex index of the row.
    * @param columnIndex index of the column.
    * @return node at a given coordination.
    */
   private Node getNode(int rowIndex, int columnIndex) {
      return maze.get((rowIndex * column) + columnIndex);
   }


   /**
    * Check if a node has an unvisited neighbour.
    * @param node a representation of a single cell inside a maze.
    * @return true if there is an unvisited neighbour else false.
    */
   private boolean hasUnvisitedNeighbours(Node node) {
      boolean left = false, right = false, up = false, down = false;

      if (node.rowIndex - 1 >= 0) {
         up = !getNode(node.rowIndex-1, node.columnIndex).visited;
      }
      if (node.rowIndex + 1 < row) {
         down = !getNode(node.rowIndex+1, node.columnIndex).visited;
      }
      if (node.columnIndex - 1 >= 0) {
         left = !getNode(node.rowIndex, node.columnIndex-1).visited;
      }
      if (node.columnIndex + 1 < column) {
         right = !getNode(node.rowIndex, node.columnIndex+1).visited;
      }
      return left || right || up || down;
   }

   /**
    * Return a random unvisited neighbour of a given node.
    * @param node a representation of a single cell inside a maze.
    * @return an unvisited node.
    */
   private Node getAnUnvisitedNeighbour(Node node) {
      Node result = null;
      while (result == null) {
         int choice = MathUtils.random(3);
         switch (choice) {
            case 0:
               if (node.rowIndex - 1 >= 0 &&getNode(node.rowIndex-1, node.columnIndex).visited == false) {
                  result = getNode(node.rowIndex-1, node.columnIndex);
               }
               break;
            case 1:
               if (node.rowIndex + 1 <row &&getNode(node.rowIndex+1, node.columnIndex).visited == false) {
                  result = getNode(node.rowIndex+1, node.columnIndex);
               }
               break;
            case 2:
               if (node.columnIndex - 1 >= 0 && getNode(node.rowIndex, node.columnIndex-1).visited==false) {
                  result = getNode(node.rowIndex, node.columnIndex-1);
               }
               break;
            default:
               if (node.columnIndex + 1 <column && getNode(node.rowIndex, node.columnIndex+1).visited==false) {
                  result = getNode(node.rowIndex , node.columnIndex+1);
               }
               break;
         }
      }
      return result;
   }

   /**
    * Take a maze data and print it out.
    * @param mazeData 2D array contains maze information
    * @param scale what was the scale used to build the maze data
    */
   public void printMaze(int [][] mazeData, int scale){
      System.out.println("---------------------");
      for(int a =0; a< this.row*scale;a++){
         for(int b =0; b< this.column*scale;b++){
            if(mazeData[a][b]==1){
               System.out.print('o');
            }else{
               System.out.print(' ');
            }
         }
         System.out.print("\n");
      }
      System.out.println("---------------------");


   }

   /**
    * Convert a node to maze data.
    * @param node a representation of a single cell inside a maze.
    * @param scale what was the scale used to build the mazeData
    * @return maze data represents a single node.
    */
   private int[][] nodeToArray(Node node, int scale){
      int [][] temp = new int[scale][scale];
      for(int y =0; y<scale; y++){
         for (int x =0; x<scale;x++){
            temp[y][x]=0;
            if(node.walls.get(Node.UP,-1)==1 && y==0){
               temp[y][x]=1;
            }

            if(node.walls.get(Node.DOWN,-1)==1 && y==scale-1){
               temp[y][x]=1;
            }

            if(node.walls.get(Node.LEFT,-1)==1 && x==0){
               temp[y][x]=1;
            }

            if(node.walls.get(Node.RIGHT,-1)==1 && x==scale-1){
               temp[y][x]=1;
            }
         }
      }

      return temp;
   }


}
