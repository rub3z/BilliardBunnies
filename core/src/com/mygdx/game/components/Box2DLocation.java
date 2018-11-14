package com.mygdx.game.components;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.utilities.Utilities;

/**
 * Location related to Box2D
 */

public class Box2DLocation implements Location<Vector2> {

  private Vector2 postion;
   float orientation;

   public Box2DLocation(){
      this.postion=new Vector2();
      this.orientation=0;
   }
   public Box2DLocation(float x, float y, float orientation){
      this.postion=new Vector2(x,y);
      this.orientation=orientation;
   }

   /**
    * Returns the vector indicating the position of this location.
    */
   @Override
   public Vector2 getPosition() {
      return postion;
   }

   /**
    * Returns the float value indicating the orientation of this location. The orientation is the angle in radians representing
    * the direction that this location is facing.
    */
   @Override
   public float getOrientation() {
      return orientation;
   }

   /**
    * Sets the orientation of this location, i.e. the angle in radians representing the direction that this location is facing.
    *
    * @param orientation the orientation in radians
    */
   @Override
   public void setOrientation(float orientation) {
      this.orientation=orientation;
   }

   /**
    * Returns the angle in radians pointing along the specified vector.
    *
    * @param vector the vector
    */
   @Override
   public float vectorToAngle(Vector2 vector) {
      return Utilities.vectorToAngle(vector);
   }

   /**
    * Returns the unit vector in the direction of the specified angle expressed in radians.
    *
    * @param outVector the output vector.
    * @param angle     the angle in radians.
    * @return the output vector for chaining.
    */
   @Override
   public Vector2 angleToVector(Vector2 outVector, float angle) {
      return Utilities.angleToVector(outVector,angle);
   }

   /**
    * Creates a new location.
    * <p>
    * This method is used internally to instantiate locations of the correct type parameter {@code T}. This technique keeps the API
    * simple and makes the API easier to use with the GWT backend because avoids the use of reflection.
    *
    * @return the newly created location.
    */
   @Override
   public Location<Vector2> newLocation() {
      return new Box2DLocation();
   }
}
