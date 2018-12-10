package com.mygdx.game.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * A utility class stores useful functions and constants.
 */
public class Utilities {
    //Use in RendeingSystem
    public static final float PPM=16f;
    public static final float FRUSTUM_WIDTH= 1920/PPM;
    public static final float FRUSTUM_HEIGHT = 1080/PPM;

    public static final float PIXELS_TO_METRES = 1.0f / PPM;


    public static float PixelsToMeters(float pixelValue){
        return pixelValue*PIXELS_TO_METRES;
    }
    public static float MetersToPixels(float meterValue){

       return meterValue*PPM;
    }


    public static final float MAX_STEP_TIME=1/300f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;

    //Collision Filter
    //Collision Filter
    //CategoryBits of entity
    public static final short CATEGORY_PLAYER_ONE = 0x00001; //1
   public static final short CATEGORY_PLAYER_TWO = 0x0002; //10
   public static final short CATEGORY_PLAYER_THREE=0x0004; //100
   public static final short CATEGORY_PLAYER_FOUR=0x008; //1000
   public static final short CATEGORY_BULLET_ONE=0x0010; //10000
   public static final short CATEGORY_BULLET_TWO= 0x0020; //100000
   public static final short CATEGORY_BULLET_THREE= 0x0040; //1000000
   public static final short CATEGORY_BULLET_FOUR=0x0080;
   public static final short CATEGORY_WALL=0x0100;
   public static final short CATEGORY_PLAYER_BOUNDARY=0x0200;
   public static final short CATEGORY_BULLET_BOUNDARY=0x0400;
   public static final short CATEGORY_SEED=0x0800;

   //MaskingBits of that entity

   public static final short MASK_PLAYER_ONE = CATEGORY_BULLET_TWO | CATEGORY_BULLET_THREE |CATEGORY_BULLET_FOUR |CATEGORY_PLAYER_BOUNDARY |CATEGORY_WALL|CATEGORY_SEED |CATEGORY_PLAYER_TWO|CATEGORY_PLAYER_THREE|CATEGORY_PLAYER_FOUR;
   public static final short MASK_PLAYER_TWO = CATEGORY_BULLET_ONE  | CATEGORY_BULLET_THREE |CATEGORY_BULLET_FOUR |CATEGORY_PLAYER_BOUNDARY | CATEGORY_WALL|CATEGORY_SEED |CATEGORY_PLAYER_ONE;
   public static final short MASK_PLAYER_THREE=CATEGORY_BULLET_ONE |CATEGORY_PLAYER_TWO | CATEGORY_BULLET_TWO | CATEGORY_BULLET_FOUR |CATEGORY_PLAYER_BOUNDARY |CATEGORY_WALL|CATEGORY_SEED|CATEGORY_PLAYER_ONE;
   public static final short MASK_PLAYER_FOUR=CATEGORY_BULLET_ONE | CATEGORY_PLAYER_TWO | CATEGORY_BULLET_TWO | CATEGORY_BULLET_THREE |CATEGORY_PLAYER_BOUNDARY |CATEGORY_WALL|CATEGORY_SEED|CATEGORY_PLAYER_ONE;
   public static final short MASK_BULLET_ONE= CATEGORY_PLAYER_TWO |CATEGORY_PLAYER_THREE |CATEGORY_PLAYER_FOUR |CATEGORY_WALL |CATEGORY_BULLET_BOUNDARY;
   public static final short MASK_BULLET_TWO= CATEGORY_PLAYER_ONE  |CATEGORY_PLAYER_THREE |CATEGORY_PLAYER_FOUR |CATEGORY_WALL |CATEGORY_BULLET_BOUNDARY;
   public static final short MASK_BULLET_THREE= CATEGORY_PLAYER_ONE | CATEGORY_PLAYER_TWO |CATEGORY_PLAYER_FOUR |CATEGORY_WALL |CATEGORY_BULLET_BOUNDARY;
   public static final short MASK_BULLET_FOUR=CATEGORY_PLAYER_ONE | CATEGORY_PLAYER_TWO |CATEGORY_PLAYER_THREE |CATEGORY_WALL |CATEGORY_BULLET_BOUNDARY;
   public static final short MASK_WALL=CATEGORY_PLAYER_ONE |CATEGORY_PLAYER_TWO |CATEGORY_PLAYER_THREE |CATEGORY_PLAYER_FOUR | CATEGORY_BULLET_ONE |CATEGORY_BULLET_TWO |CATEGORY_BULLET_THREE |CATEGORY_BULLET_FOUR;
   public static final short MASK_PLAYER_BOUNDARY= CATEGORY_PLAYER_ONE |CATEGORY_PLAYER_TWO |CATEGORY_PLAYER_THREE |CATEGORY_PLAYER_FOUR ;
   public static final short MASK_BULLET_BOUNDARY=CATEGORY_BULLET_ONE |CATEGORY_BULLET_TWO |CATEGORY_BULLET_THREE |CATEGORY_PLAYER_FOUR;
   public static short MASK_SEED = 0x000;


   public static float vectorToAngle (Vector2 vector) {
      return (float)Math.atan2(vector.y, vector.x);
   }

   public static Vector2 angleToVector (Vector2 outVector, float angle) {
      outVector.x = -(float)Math.sin(angle);
      outVector.y = (float)Math.cos(angle);
      return outVector;
   }
}
