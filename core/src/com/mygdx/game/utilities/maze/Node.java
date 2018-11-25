package com.mygdx.game.utilities.maze;

import com.badlogic.gdx.utils.IntIntMap;

/**
 * A generic representation of a single cell inside a maze.
 */
public class Node {
   public boolean visited;
   public IntIntMap walls;
   public int rowIndex;
   public int columnIndex;

   public Node(int rowIndex, int columnIndex) {
      this.columnIndex = columnIndex;
      this.rowIndex = rowIndex;
      visited = false;
      walls = new IntIntMap();
      walls.put(LEFT, 1);
      walls.put(RIGHT, 1);
      walls.put(UP, 1);
      walls.put(DOWN, 1);

   }

   @Override
   public String toString() {
      return
              "(" + rowIndex + ", " + columnIndex + ")" +
                      "\n LeftWall: " + walls.get(LEFT, 0) +
                      "\n RightWall:" + walls.get(RIGHT, 0) +
                      "\n UpWall:" + walls.get(UP, 0) +
                      "\n DownWall:" + walls.get(DOWN, 0);
   }

   public static final int LEFT = 1;
   public static final int RIGHT = 2;
   public static final int UP = 3;
   public static final int DOWN = 4;
}
