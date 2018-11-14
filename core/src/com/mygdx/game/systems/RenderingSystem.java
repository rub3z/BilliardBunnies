package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.utilities.Utilities;

import java.util.Comparator;

/**
 * A system responsible for rendering entities
 */
public class RenderingSystem extends SortedIteratingSystem {

   /**
    * Mappers uses to quickly access the component of an entity.
    */
   private ComponentMapper<TransformComponent> transformMapper;
   private ComponentMapper<TextureComponent> textureMapper;
   private OrthographicCamera camera;

   private float accumulator;
   /**
    * SpriteBatch uses to draw.
    */
   private SpriteBatch spriteBatch;

   private float stateTime;

   /**
    * Constructor
    */
   public RenderingSystem(SpriteBatch spriteBatch, OrthographicCamera camera) {
      super(Family.all(BodyComponent.class, TextureComponent.class).get(), new LayerComparator());

      transformMapper = ComponentMapper.getFor(TransformComponent.class);
      textureMapper = ComponentMapper.getFor(TextureComponent.class);

      this.spriteBatch = spriteBatch;
      this.camera = camera;
      accumulator=0f;
      stateTime=0f;
   }

   @Override
   public void update(float deltaTime) {
      camera.update();
      spriteBatch.setProjectionMatrix(camera.combined);
      spriteBatch.enableBlending();

      float frameTime=Math.min(deltaTime,0.25f);
      accumulator+=frameTime;
      while (accumulator>=Utilities.MAX_STEP_TIME){
          accumulator-=Utilities.MAX_STEP_TIME;
      }
      stateTime+=deltaTime;
      super.update(accumulator/deltaTime);
   }

   /**
    * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
    * specific processing.
    *
    * @param entity    The current Entity being processed
    * @param deltaTime The delta time between the last and current frame
    */
   @Override
   protected void processEntity(Entity entity, float deltaTime) {
      spriteBatch.begin();
      TextureComponent textureComponent = textureMapper.get(entity);
      TransformComponent transformComponent = transformMapper.get(entity);
      if (textureComponent != null && !transformComponent.isHidden) {
         TextureRegion textureRegion= textureComponent.textureRegionAnimation.getKeyFrame(stateTime,true);
         float width = textureRegion.getRegionWidth();
         float height = textureRegion.getRegionHeight();
         float originX = width / 2f;
         float originY = height / 2f;
         spriteBatch.draw(textureRegion,
                 Interpolation.linear.apply(transformComponent.previousPosition.x,transformComponent.position.x,deltaTime) - originX,
                 Interpolation.linear.apply(transformComponent.previousPosition.y,transformComponent.position.y,deltaTime) - originY,
                 originX,
                 originY,
                 width,
                 height,
                 Utilities.PixelsToMeters(transformComponent.scale.x),
                 Utilities.PixelsToMeters(transformComponent.scale.y),
                 transformComponent.rotation);
      }
      spriteBatch.end();
   }

   /**
    * A comparator used to compare which texture to render first....Layering .
    */
   private static class LayerComparator implements Comparator<Entity> {
      private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

      @Override
      public int compare(Entity e1, Entity e2) {
         return (int) Math.signum(tm.get(e1).position.z - tm.get(e2).position.z);
      }
   }
}

