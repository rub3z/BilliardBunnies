package com.mygdx.game.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * A utility class stores useful functions and constants.
 */
public class Utilities {
    //Use in RendeingSystem
    public static final float PPM=16f;
    public static final float FRUSTUM_WIDTH= Gdx.graphics.getWidth()/PPM;
    public static final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight()/PPM;

    public static final float PIXELS_TO_METRES = 1.0f / PPM;

    public static Vector2 getScreenSizeInMeters(){
        Vector2 meterDimension= new Vector2(Gdx.graphics.getWidth()*PIXELS_TO_METRES,
                Gdx.graphics.getHeight()*PIXELS_TO_METRES);
        return meterDimension;
    }

    public static Vector2 getScreenSizeInPixel(){
        Vector2 pixelDimension= new Vector2(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        return pixelDimension;
    }
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
   //CategoryBits of entity
   public static final short CATEGORY_PLAYER = 0x001; //1
   public static final short CATEGORY_ENEMY = 0x002; //10
   public static final short CATEGORY_PLAYER_PROJECTILE=0x004; //100
   public static final short CATEGORY_ENEMY_PROJECTILE=0x010; //1000
   public static final short CATEGORY_BULLET_BOUNDARY=0x020; //10000
   public static final short CATEGORY_ENVIRONMENT= 0x040; //100000
   public static final short CATEGORY_PLAYER_SPECIAL_PROJECTILE= 0x080; //1000000
   public static final short CATEGORY_ENEMY_BOUNDARY=0x100;

   //MaskingBits of that entity
   public static final short MASK_PLAYER=~CATEGORY_PLAYER & ~CATEGORY_PLAYER_PROJECTILE; //Player can collide with anything that isn't player or player's projectile.
   public static final short MASK_PLAYER_PROJECTILE= CATEGORY_ENEMY|CATEGORY_BULLET_BOUNDARY; //Player's projectile only collide with enemy
   public static final short MASK_ENEMY= CATEGORY_PLAYER | CATEGORY_PLAYER_PROJECTILE|CATEGORY_PLAYER_SPECIAL_PROJECTILE|CATEGORY_ENEMY_BOUNDARY; // Enemy can collide with player and player's projectile
   public static final short MASK_ENEMY_PROJECTILE = CATEGORY_PLAYER |CATEGORY_BULLET_BOUNDARY; //Enemy's projectile can collide with player
   public static final short MASK_BULLET_BOUNDARY=CATEGORY_ENEMY_PROJECTILE|CATEGORY_PLAYER_PROJECTILE|CATEGORY_ENEMY; // Wall to delete stray bullet
   public static final short MASK_ENVIRONMENT = -1; // ENVIRONMENT can collide with everything.
   public static final short MASK_PLAYER_SPECIAL_PROJECTILE = CATEGORY_ENEMY;
   public static final short MASK_ENEMY_BOUNDARY =CATEGORY_ENEMY;


   public static float vectorToAngle (Vector2 vector) {
      return (float)Math.atan2(-vector.x, vector.y);
   }

   public static Vector2 angleToVector (Vector2 outVector, float angle) {
      outVector.x = -(float)Math.sin(angle);
      outVector.y = (float)Math.cos(angle);
      return outVector;
   }
}
