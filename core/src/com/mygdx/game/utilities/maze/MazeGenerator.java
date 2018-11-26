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
    *
    * @param row    number of row.
    * @param column number of column.
    * @param cellWidth  the number of tile equals to the width the cell. A minimum width of cell is 3 tiles.
    * @param cellHeight the number of tile equals to the height of the cell. A minimum height of cell is 3 tiles.
    * @return a complete maze data with 0 represents a empty space and 1 represents a wall.
    */
   public int[][] generateMaze(int row, int column, int cellWidth, int cellHeight) {
      this.row = row;
      this.column = column;
      cellWidth= cellWidth<3?3:cellWidth;
      cellHeight= cellHeight<3?3:cellHeight;


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
      currentCell.visited = true;

      while (unvisitedNode.size > 0) {
         if (hasUnvisitedNeighbours(currentCell)) {
            Node nextCell = getAnUnvisitedNeighbour(currentCell);
            stack.add(currentCell);
            if (nextCell.columnIndex < currentCell.columnIndex) {
               nextCell.walls.put(Node.RIGHT, 0);
               currentCell.walls.put(Node.LEFT, 0);
            }

            if (nextCell.columnIndex > currentCell.columnIndex) {
               nextCell.walls.put(Node.LEFT, 0);
               currentCell.walls.put(Node.RIGHT, 0);
            }

            if (nextCell.rowIndex < currentCell.rowIndex) {
               nextCell.walls.put(Node.DOWN, 0);
               currentCell.walls.put(Node.UP, 0);
            }

            if (nextCell.rowIndex > currentCell.rowIndex) {
               nextCell.walls.put(Node.UP, 0);
               currentCell.walls.put(Node.DOWN, 0);
            }
            currentCell = nextCell;
            currentCell.visited = true;
            unvisitedNode.removeValue(currentCell, true);
         } else if (stack.size > 0) {
            currentCell = stack.pop();
         }
      }

      removeDeadEnd();

      int[][] temp = new int[this.row * cellHeight][this.column * cellWidth];

      for (int i = 0; i < this.row; i++) {
         for (int j = 0; j < this.column; j++) {

            int[][] t = nodeToArray(getNode(i, j),cellWidth,cellHeight);

            for (int y = 0; y < t.length; y++) {
               for (int x = 0; x < t[0].length; x++) {
                  temp[i * cellHeight + y][j * cellWidth + x] = t[y][x];
               }
            }

         }
      }
      return temp;


   }

   /**
    * Allows the use of 2D coordination to access nodes contain in 1D array.
    *
    * @param rowIndex    index of the row.
    * @param columnIndex index of the column.
    * @return node at a given coordination.
    */
   private Node getNode(int rowIndex, int columnIndex) {
      return maze.get((rowIndex * column) + columnIndex);
   }


   /**
    * Check if a node has an unvisited neighbour.
    *
    * @param node a representation of a single cell inside a maze.
    * @return true if there is an unvisited neighbour else false.
    */
   private boolean hasUnvisitedNeighbours(Node node) {
      boolean left = false, right = false, up = false, down = false;

      if (node.rowIndex - 1 >= 0) {
         up = !getNode(node.rowIndex - 1, node.columnIndex).visited;
      }
      if (node.rowIndex + 1 < row) {
         down = !getNode(node.rowIndex + 1, node.columnIndex).visited;
      }
      if (node.columnIndex - 1 >= 0) {
         left = !getNode(node.rowIndex, node.columnIndex - 1).visited;
      }
      if (node.columnIndex + 1 < column) {
         right = !getNode(node.rowIndex, node.columnIndex + 1).visited;
      }
      return left || right || up || down;
   }

   /**
    * Return a random unvisited neighbour of a given node.
    *
    * @param node a representation of a single cell inside a maze.
    * @return an unvisited node.
    */
   private Node getAnUnvisitedNeighbour(Node node) {
      Node result = null;
      while (result == null) {
         int choice = MathUtils.random(3);
         switch (choice) {
            case 0:
               if (node.rowIndex - 1 >= 0 && getNode(node.rowIndex - 1, node.columnIndex).visited == false) {
                  result = getNode(node.rowIndex - 1, node.columnIndex);
               }
               break;
            case 1:
               if (node.rowIndex + 1 < row && getNode(node.rowIndex + 1, node.columnIndex).visited == false) {
                  result = getNode(node.rowIndex + 1, node.columnIndex);
               }
               break;
            case 2:
               if (node.columnIndex - 1 >= 0 && getNode(node.rowIndex, node.columnIndex - 1).visited == false) {
                  result = getNode(node.rowIndex, node.columnIndex - 1);
               }
               break;
            default:
               if (node.columnIndex + 1 < column && getNode(node.rowIndex, node.columnIndex + 1).visited == false) {
                  result = getNode(node.rowIndex, node.columnIndex + 1);
               }
               break;
         }
      }
      return result;
   }

   /**
    * Take a maze data and print it out.
    *
    * @param mazeData 2D array contains maze information
    * @param cellWidth  the number of tile equals to the width the cell. A minimum width of cell is 3 tiles.
    * @param cellHeight the number of tile equals to the height of the cell. A minimum height of cell is 3 tiles.
    */
   public void printMaze(int[][] mazeData, int cellWidth, int cellHeight) {
      System.out.println("---------------------");
      for (int a = 0; a < this.row * cellHeight; a++) {
         for (int b = 0; b < this.column * cellWidth; b++) {
            if (mazeData[a][b] == 1) {
               System.out.print('o');
            } else {
               System.out.print(' ');
            }
         }
         System.out.print("\n");
      }
      System.out.println("---------------------");


   }

   /**
    * Convert a node to maze data.
    *
    * @param node  a representation of a single cell inside a maze.
    * @param cellWidth  the number of tile equals to the width the cell. A minimum width of cell is 3 tiles.
    * @param cellHeight the number of tile equals to the height of the cell. A minimum height of cell is 3 tiles.
    * @return maze data represents a single node.
    */
   private int[][] nodeToArray(Node node, int cellWidth, int cellHeight) {
      int[][] temp = new int[cellHeight][cellWidth];
      for (int y = 0; y < cellHeight; y++) {
         for (int x = 0; x < cellWidth; x++) {
            temp[y][x] = 0;
            if (node.walls.get(Node.UP, -1) == 1 && y == 0) {
               temp[y][x] = 1;
            }

            if (node.walls.get(Node.DOWN, -1) == 1 && y == cellHeight - 1) {
               temp[y][x] = 1;
            }

            if (node.walls.get(Node.LEFT, -1) == 1 && x == 0) {
               temp[y][x] = 1;
            }

            if (node.walls.get(Node.RIGHT, -1) == 1 && x == cellWidth - 1) {
               temp[y][x] = 1;
            }
         }
      }


      return temp;
   }
   public void removeDeadEnd(){
      for(Node node:maze){
         if(node.columnIndex>0 && node.columnIndex<column-1 &&node.rowIndex>0 && node.rowIndex<row-1){
            int count=0;
            for(int i =1; i<=4 ;i++){
               if(node.walls.get(i,-1)==0){
                  count++;
               }
            }
            if(count<2){
               boolean done=false;
               do{
                  int choice =MathUtils.random(1,4);
                  if(node.walls.get(choice,0)==1){
                     done=true;
                     node.walls.put(choice,0);
                     switch (choice){
                        case 1:
                           getNode(node.rowIndex,node.columnIndex-1).walls.put(2,0);
                           break;
                        case 2:
                           getNode(node.rowIndex,node.columnIndex+1).walls.put(1,0);
                           break;
                        case 3:
                           getNode(node.rowIndex-1,node.columnIndex).walls.put(4,0);
                           break;
                        default:
                           getNode(node.rowIndex+1,node.columnIndex).walls.put(3,0);
                           break;
                     }
                  }
               }while (!done);
            }
         }
      }
   }


}
