package com.mygdx.game.utilities.maze;

import com.badlogic.gdx.utils.IntIntMap;

public class Node {
   public boolean visited;

   public IntIntMap walls;

   public int rowIndex;
   public int columnIndex;

   public Node(int columnIndex, int rowIndex){
      this.columnIndex=columnIndex;
      this.rowIndex=rowIndex;
      visited=false;
      walls = new IntIntMap();
      walls.put(LEFT,0);
      walls.put(RIGHT,0);
      walls.put(UP,0);
      walls.put(DOWN,0);

   }

   @Override
   public String toString() {
      return "("+columnIndex+", "+rowIndex+")";
   }

   public static final int LEFT=1;
   public static final int RIGHT=1;
   public static final int UP=1;
   public static final int DOWN=1;
}
