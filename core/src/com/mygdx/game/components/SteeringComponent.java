package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.utilities.Utilities;

/**
 * This component stores variables related AI behavior
 */
public class SteeringComponent implements Steerable<Vector2>, Component, Pool.Poolable {
  public Body body;

   public float scale=1;
   // Steering data
   float maxLinearSpeed = 30f;	// stores the max speed the entity can go
   float maxLinearAcceleration = 20f;	// stores the max acceleration
   float maxAngularSpeed =50f;		// the max turning speed
   float maxAngularAcceleration = 12f;// the max turning acceleration
   float zeroThreshold = 0.01f;	// how accurate should checks be (0.0000001f will mean the entity must get within 0.0000001f of

   // target location. This will cause problems as our entities travel pretty fast and can easily over or undershoot this.)
   public SteeringBehavior<Vector2> steeringBehavior; // stors the action behaviour
   private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2()); // this is the actual steering vactor for our unit
   private float boundingRadius = 1f;   // the minimum radius size for a circle required to cover whole object
   private boolean tagged = true;		// This is a generic flag utilized in a variety of ways. (never used this myself)
   private boolean independentFacing = true; // defines if the entity can move in a direction other than the way it faces)

   public boolean isIndependentFacing(){
      return independentFacing;
   }

   public void setIndependentFacing(boolean independentFacing) {
      this.independentFacing = independentFacing;
   }

   /** Call this to update the steering behaviour (per frame)
    * @param deltaTime delta time between frames
    */
   public void update(float deltaTime){
      if(steeringBehavior!=null){
         steeringBehavior.calculateSteering(steeringOutput);
         applySteering(steeringOutput,deltaTime);
      }
   }



   private void applySteering(SteeringAcceleration<Vector2> steering, float deltaTime){
      boolean anyAccelerations  =false;
      //Update position and linear velocity.
      if(!steeringOutput.linear.isZero()){
         //This method internally scales the force by deltaTime
         body.applyForceToCenter(steeringOutput.linear.scl(scale),true);
         anyAccelerations=true;
      }
      //Update orientation and angular velocity
      if (isIndependentFacing()){
            //This method internally scales the torque by deltaTime
            if(steeringOutput.angular!=0){
               body.applyTorque(steeringOutput.angular,true);
               anyAccelerations=true;
            }
      }else{
         //If we have not got any velocity, then we can do nothing.
         Vector2 linVel = getLinearVelocity();
         if(!linVel.isZero(getZeroLinearSpeedThreshold())){
            float newOrientation = vectorToAngle(linVel);
            body.setAngularVelocity((newOrientation - getAngularVelocity())*deltaTime);
            body.setTransform(body.getPosition(),newOrientation);
         }
      }

      if(anyAccelerations){
         //Cap linear speed
         Vector2 velocity= body.getLinearVelocity();
         float currentSpeedSquare=velocity.len2();
         float maxLinearSpeed=getMaxLinearSpeed();
         if(currentSpeedSquare>(maxLinearSpeed*maxLinearSpeed)){
            body.setLinearVelocity(velocity.scl(maxLinearSpeed/(float) Math.sqrt(currentSpeedSquare)));
         }

         //Cap the angular speed
         float maxAngularVelocity = getMaxAngularSpeed();
         if(body.getAngularVelocity()>maxAngularVelocity){
            body.setAngularVelocity(maxAngularVelocity);
         }
      }
   }

   /**
    * Returns the vector indicating the linear velocity of this Steerable.
    */
   @Override
   public Vector2 getLinearVelocity() {
      return body.getLinearVelocity();
   }

   /**
    * Returns the float value indicating the the angular velocity in radians of this Steerable.
    */
   @Override
   public float getAngularVelocity() {
      return body.getAngularVelocity();
   }

   /**
    * Returns the bounding radius of this Steerable.
    */
   @Override
   public float getBoundingRadius() {
      return this.boundingRadius;
   }

   /**
    * Returns {@code true} if this Steerable is tagged; {@code false} otherwise.
    */
   @Override
   public boolean isTagged() {
      return this.tagged;
   }

   /**
    * Tag/untag this Steerable. This is a generic flag utilized in a variety of ways.
    *
    * @param tagged the boolean value to set
    */
   @Override
   public void setTagged(boolean tagged) {
      this.tagged=tagged;
   }

   /**
    * Returns the threshold below which the linear speed can be considered zero. It must be a small positive value near to zero.
    * Usually it is used to avoid updating the orientation when the velocity vector has a negligible length.
    */
   @Override
   public float getZeroLinearSpeedThreshold() {
      return this.zeroThreshold;
   }

   /**
    * Sets the threshold below which the linear speed can be considered zero. It must be a small positive value near to zero.
    * Usually it is used to avoid updating the orientation when the velocity vector has a negligible length.
    *
    * @param value
    */
   @Override
   public void setZeroLinearSpeedThreshold(float value) {
      this.zeroThreshold=value;
   }

   /**
    * Returns the maximum linear speed.
    */
   @Override
   public float getMaxLinearSpeed() {
      return this.maxLinearSpeed;
   }

   /**
    * Sets the maximum linear speed.
    *
    * @param maxLinearSpeed
    */
   @Override
   public void setMaxLinearSpeed(float maxLinearSpeed) {
      this.maxLinearSpeed=maxLinearSpeed;
   }

   /**
    * Returns the maximum linear acceleration.
    */
   @Override
   public float getMaxLinearAcceleration() {
      return this.maxLinearAcceleration;
   }

   /**
    * Sets the maximum linear acceleration.
    *
    * @param maxLinearAcceleration
    */
   @Override
   public void setMaxLinearAcceleration(float maxLinearAcceleration) {
      this.maxLinearAcceleration=maxLinearAcceleration;
   }

   /**
    * Returns the maximum angular speed.
    */
   @Override
   public float getMaxAngularSpeed() {
      return this.maxAngularSpeed;
   }

   /**
    * Sets the maximum angular speed.
    *
    * @param maxAngularSpeed
    */
   @Override
   public void setMaxAngularSpeed(float maxAngularSpeed) {
      this.maxAngularSpeed=maxAngularSpeed;
   }

   /**
    * Returns the maximum angular acceleration.
    */
   @Override
   public float getMaxAngularAcceleration() {
      return this.maxAngularAcceleration;
   }

   /**
    * Sets the maximum angular acceleration.
    *
    * @param maxAngularAcceleration
    */
   @Override
   public void setMaxAngularAcceleration(float maxAngularAcceleration) {
      this.maxAngularAcceleration=maxAngularAcceleration;
   }

   /**
    * Returns the vector indicating the position of this location.
    */
   @Override
   public Vector2 getPosition() {
      return body.getPosition();
   }

   /**
    * Returns the float value indicating the orientation of this location. The orientation is the angle in radians representing
    * the direction that this location is facing.
    */
   @Override
   public float getOrientation() {
      return body.getAngle();
   }

   /**
    * Sets the orientation of this location, i.e. the angle in radians representing the direction that this location is facing.
    *
    * @param orientation the orientation in radians
    */
   @Override
   public void setOrientation(float orientation) {
      body.setTransform(getPosition(),orientation);
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

   /**
    * Resets the object for reuse. Object references should be nulled and fields may be set to default values.
    */
   @Override
   public void reset() {
      body=null;
      steeringBehavior=null;
      maxLinearSpeed = 30f;	// stores the max speed the entity can go
      maxLinearAcceleration = 20f;	// stores the max acceleration
      maxAngularSpeed =50f;		// the max turning speed
      maxAngularAcceleration = 12f;// the max turning acceleration
      zeroThreshold = 0.1f;
   }
}
