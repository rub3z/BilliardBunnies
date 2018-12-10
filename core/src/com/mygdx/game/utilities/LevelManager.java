package com.mygdx.game.utilities;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.utilities.maze.MazeGenerator;

/**
 * Manage level information.
 */
public class LevelManager {
    private static LevelManager single_instance; //Instance of this class.
    private int[][] mazeData; //Data generated from the maze generator.
    private MazeGenerator mazeGenerator; //Maze generator.
    private float tileScale; //How big is the tile size is.
    private Tile [][] tiles; //Tiles container.
    private boolean isCenter;
    private Array<Tile>quad1,quad2,quad3,quad4;

    /**
     * Private constructor.
     */
    private LevelManager() {
        mazeGenerator = new MazeGenerator();
        quad1=new Array<Tile>();
        quad2=new Array<Tile>();
        quad3=new Array<Tile>();
        quad4=new Array<Tile>();
    }

    /**
     * Get a single instance of this class.
     *
     * @return LevelManager.
     */
    public static LevelManager getManager() {
        if (single_instance == null) {
            single_instance = new LevelManager();
        }
        return single_instance;
    }

    /**
     * Generate a new level.
     *
     * @param columnNumber number of column of cells
     * @param rowNumber    number of row of cells
     * @param cellWidth    the number of tile equals to the width the cell. A minimum width of cell is 3 tiles.
     * @param cellHeight   the number of tile equals to the height of the cell. A minimum height of cell is 3 tiles.
     * @param tileScale    equivalence of 1 integer index to float value
     * @param isCenter     should the maze be center on the screen.
     * @param loadPrebuild load in a prebuild map
     */
    public void generateLevel(int columnNumber, int rowNumber, int cellWidth, int cellHeight, float tileScale, boolean isCenter, boolean loadPrebuild) {
        mazeData = mazeGenerator.generateMaze(rowNumber, columnNumber, cellWidth, cellHeight, loadPrebuild);


        this.tileScale = tileScale;
        this.isCenter = isCenter;
        tiles=new Tile[mazeData.length][mazeData[0].length];
        for (int y = 0; y < mazeData.length; y++) {
            for (int x = 0; x < mazeData[0].length; x++) {
                Type type;
                switch (mazeData[y][x]) {
                    case 1:
                        type = Type.WALL;
                        break;
                    case 2:
                        type = Type.PLAYER_WALL;
                        break;
                    default:

                        type = Type.EMPTY_SPACE;
                        break;
                }
                tiles[y][x]=new Tile((x * tileScale) + getHorizontalShift(), (y * tileScale) + getVerticalShift(), type);
            }

            //generate

        }

       for (int y = 0; y < tiles.length; y++) {
          for (int x = 0; x < tiles[0].length; x++) {
//               tiles[y][x].neighbors.put(Tile.UP,getTile(x,y+1));
//               tiles[y][x].neighbors.put(Tile.DOWN,getTile(x,y-1));
//               tiles[y][x].neighbors.put(Tile.RIGHT,getTile(x+1,y));
//               tiles[y][x].neighbors.put(Tile.LEFT,getTile(x-1,y));
             tiles[y][x].addNeighbor(1, getTile(x,y+1));
             tiles[y][x].addNeighbor(2, getTile(x,y-1));
             tiles[y][x].addNeighbor(3, getTile(x+1, y));
             tiles[y][x].addNeighbor(4, getTile(x-1, y));
          }
       }


       for (int y = tiles.length/2; y < tiles.length; y++) {
          for (int x = tiles[0].length/2; x < tiles[0].length; x++) {
             if(tiles[y][x].getType()==Type.EMPTY_SPACE){
                quad1.add(tiles[y][x]);
             }

          }
       }

       for (int y = tiles.length/2; y < tiles.length; y++) {
          for (int x = 0; x < tiles[0].length/2; x++) {
             if(tiles[y][x].getType()==Type.EMPTY_SPACE){
                quad2.add(tiles[y][x]);
             }
          }
       }
       for (int y = 0; y < tiles.length/2; y++) {
          for (int x = 0; x < tiles[0].length/2; x++) {
             if(tiles[y][x].getType()==Type.EMPTY_SPACE){
                quad3.add(tiles[y][x]);
             }
          }
       }
       for (int y = 0; y < tiles.length/2; y++) {
          for (int x = tiles[0].length/2; x < tiles[0].length; x++) {
             if(tiles[y][x].getType()==Type.EMPTY_SPACE){
                quad4.add(tiles[y][x]);
             }
          }
       }
    }

    /**
     * Generate a new level with a cell size of 5x5 tiles and try to center around the screen.
     *
     * @param columnNumber number of column of cells
     * @param rowNumber    number of row of cells
     * @param tileScale    equivalence of 1 integer index to float value
     */
    public void generateLevel(int columnNumber, int rowNumber, float tileScale) {
        generateLevel(columnNumber, rowNumber, 7, 7, tileScale, true, false);
    }

    /**
     * Return immutable array of tiles.
     *
     * @return
     */
    public ImmutableArray<Tile> getTiles() {
       Array<Tile> array= new Array<Tile>();
       for (int y=0; y<tiles.length;y++){
          for(int x =0; x<tiles[0].length;y++){
             array.add(tiles[y][x]);
          }
       }
       return new ImmutableArray<Tile>(array);
    }

    /**
     * Return a specific tiles by indexes.
     *
     * @param x int index.
     * @param y int index.
     * @return a tile located at the above index.
     */
    public Tile getTile(int x, int y) {
       try {
          return tiles[y][x];
       }catch (ArrayIndexOutOfBoundsException e){
          return  null;
       }
    }

    /**
     * Return a specific tiles by floating point coordinate.
     *
     * @param x float x-coordinate.
     * @param y float y-coordinate.
     * @return a tile located at the above location.
     */
    public Tile getTile(float x, float y) {
      try {
         x = (x - getHorizontalShift())/tileScale+(tileScale/2f);
         y = (y - getVerticalShift())/tileScale +(tileScale/2f);

         if(x>=0 &&y>=0 ){
            return tiles[(int)y][(int)x];
         }
         else
            return null;
      }catch (ArrayIndexOutOfBoundsException e){
         return  null;
      }

   }



    /**
     * Convert float to index
     *
     * @param x int index.
     * @param y int index.
     * @return Vector2 contains float coordinates.
     */
    public Vector2 floatToIndex(float x, float y) {
          x = (x - getHorizontalShift())/tileScale+(tileScale/2f);
          y = (y - getVerticalShift())/tileScale +(tileScale/2f);

          return new Vector2((int)x,(int)y);
    }

    /**
     * Get all tiles that is classified as a wall.
     *
     * @return all wall tiles.
     */
    public ImmutableArray<Tile> getWallTitles() {
       Array<Tile> array= new Array<Tile>();
       for (int y=0; y<tiles.length;y++){
          for(int x =0; x<tiles[0].length;x++){
             if(tiles[y][x].getType()==Type.WALL)
             array.add(tiles[y][x]);
          }
       }

        return new ImmutableArray<Tile>(array);
    }

    /**
     * Get all tiles that is classified as an invisible walls to limit players path.
     *
     * @return all wall tiles.
     */
    public ImmutableArray<Tile> getPlayerWallTiles() {
       Array<Tile> array= new Array<Tile>();
       for (int y=0; y<tiles.length;y++){
          for(int x =0; x<tiles[0].length;x++){
             if(tiles[y][x].getType()==Type.PLAYER_WALL)
                array.add(tiles[y][x]);
          }
       }
        return new ImmutableArray<Tile>(array);
    }

    /**
     * Get all tiles that is classified as empty space.
     *
     * @return all empty space tiles.
     */
    public ImmutableArray<Tile> getEmptyTiles() {
       Array<Tile> array= new Array<Tile>();
       for (int y=0; y<tiles.length;y++){
          for(int x =0; x<tiles[0].length;x++){
             if(tiles[y][x].getType()==Type.EMPTY_SPACE)
                array.add(tiles[y][x]);
          }
       }

        return new ImmutableArray<Tile>(array);
    }

    /**
     * Get a random tile that is classified as a wall.
     *
     * @return a random wall tile.
     */
    public Tile getARandomWallTile() {
        Tile t;
        do {
            t = getTile(MathUtils.random(mazeData[0].length-1), MathUtils.random(mazeData.length-1));
        } while (t.getType() != Type.WALL);
        return t;
    }

    /**
     * Get a random tile that is classified as an empty space.
     *
     * @return a random empty space tile.
     */
    public Tile getARandomEmptySpaceTile() {
        Tile t;
        do {
            t = getTile(MathUtils.random(mazeData[0].length-1), MathUtils.random(mazeData.length-1));
        } while (t.getType() != Type.EMPTY_SPACE);
        return t;
    }

    /**
     * Type of tiles.
     */
    public enum Type {
        WALL, EMPTY_SPACE, PLAYER_WALL
    }

    public Tile getARandomEmptyTileInQuadrantOne(){
       return quad1.get(MathUtils.random(quad1.size-1));
    }

   public Tile getARandomEmptyTileInQuadrantTwo(){
      return quad2.get(MathUtils.random(quad2.size-1));
   }

   public Tile getARandomEmptyTileInQuadrantThree(){
      return quad3.get(MathUtils.random(quad3.size-1));
   }
   public Tile getARandomEmptyTileInQuadrantFour(){
      return quad4.get(MathUtils.random(quad4.size-1));
   }
   public Tile getCenter(){
       return quad1.first();
   }

    public float getHorizontalShift() {
        return isCenter ? ((Utilities.FRUSTUM_WIDTH / 2) - ((mazeData[0].length * tileScale) / 2)) + 0.9501f : 0f;
    }

    public float getVerticalShift() {
        return isCenter ? ((Utilities.FRUSTUM_HEIGHT / 2) - ((mazeData.length * tileScale) / 2)) + 1f : 0f;
    }

}
