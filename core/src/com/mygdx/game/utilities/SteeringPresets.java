package com.mygdx.game.utilities;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.Box2DLocation;
import com.mygdx.game.components.SteeringComponent;

/**
 * A present of AI basic behavior.
 */
public class SteeringPresets {

   public static Arrive<Vector2> getArrive(SteeringComponent agent, SteeringComponent target){
      Arrive<Vector2> arrive=new Arrive<Vector2>(agent,target).setTimeToTarget(1f).setArrivalTolerance(0.5f).setDecelerationRadius(1f);
      return arrive;
   }

   public static Arrive<Vector2> getArrive(SteeringComponent agent, Box2DLocation target){
      Arrive<Vector2> arrive=new Arrive<Vector2>(agent,target).setTimeToTarget(1f).setArrivalTolerance(1f).setDecelerationRadius(1f);
      return arrive;
   }

   public static Arrive<Vector2>getArrive(SteeringComponent agent, float posX, float posY){
      Arrive<Vector2> arrive=new Arrive<Vector2>(agent,new Box2DLocation(posX,posY,0))
              .setTimeToTarget(0.1f)
              .setArrivalTolerance(0.5f)
              .setDecelerationRadius(10f)
              ;
      return arrive;
   }
   public static Seek<Vector2> getSeek(SteeringComponent agent, SteeringComponent target){
      Seek<Vector2> seek=new Seek<Vector2>(agent,target);
      return seek;
   }

   public static Wander<Vector2> getWander(SteeringComponent agent){
      Wander<Vector2> wander = new Wander<Vector2>(agent)
              .setFaceEnabled(false)
              .setWanderOffset(0.001f) // distance away from entity to set target
              .setWanderOrientation(3)
              .setWanderRadius(2f) // size of target
              .setWanderRate(MathUtils.PI2 * 2)// higher values = more spinning
              .setTimeToTarget(0.1f);
      return wander;
   }
}
