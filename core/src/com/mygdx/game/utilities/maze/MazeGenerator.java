package com.mygdx.game.utilities.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

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

   public void generateMaze(int column, int row) {
      this.row = row;
      this.column = column;
      unvisitedNode.clear();
      maze.clear();
      for (int i = 0; i < row; i++) {
         for (int j = 0; j < column; j++) {
            unvisitedNode.add(new Node(j, i));
         }
      }
      maze.addAll(unvisitedNode);

      currentCell = getNode(MathUtils.random(column - 1), MathUtils.random(row - 1));
      unvisitedNode.removeValue(currentCell, true);



   }

   private Node getNode(int columnIndex, int rowIndex) {
      return maze.get((rowIndex * column) + columnIndex);
   }


   private boolean hasUnvisitedNeighbours(Node node) {
      boolean left = false, right = false, up = false, down = false;

      if (node.rowIndex - 1 >= 0) {
         left = getNode(node.columnIndex, node.rowIndex - 1).visited;
      }
      if (node.rowIndex + 1 < row) {
         right = getNode(node.columnIndex, node.rowIndex + 1).visited;
      }
      if (node.columnIndex - 1 >= 0) {
         up = getNode(node.columnIndex - 1, node.rowIndex).visited;
      }
      if (node.columnIndex + 1 < column) {
         right = getNode(node.columnIndex + 1, node.rowIndex).visited;
      }
      return left || right || up || down;
   }

   private Node getAnUnvisitedNeighbour(Node node) {
      Node result = null;
      while (result == null) {
         int choice = MathUtils.random(3);
         switch (choice) {
            case 0:
               if (node.rowIndex - 1 >= 0 &&getNode(node.columnIndex, node.rowIndex - 1).visited == false) {
                  result = getNode(node.columnIndex, node.rowIndex - 1);
               }
               break;
            case 1:
               if (node.rowIndex + 1 <row &&getNode(node.columnIndex, node.rowIndex + 1).visited == false) {
                  result = getNode(node.columnIndex, node.rowIndex + 1);
               }
               break;
            case 2:
               if (node.columnIndex - 1 >= 0 && getNode(node.columnIndex - 1, node.rowIndex).visited==false) {
                  result = getNode(node.columnIndex - 1, node.rowIndex);
               }
               break;
            default:
               if (node.columnIndex + 1 <column && getNode(node.columnIndex + 1, node.rowIndex).visited==false) {
                  result = getNode(node.columnIndex + 1, node.rowIndex);
               }
               break;
         }
      }
      return result;
   }


}
