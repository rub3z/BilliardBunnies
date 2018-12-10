package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.components.IsPlayerComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.utilities.LevelManager;
import com.mygdx.game.utilities.Utilities;

public class AimingReticleRenderingSystem extends IteratingSystem {

   private ComponentMapper<MovementComponent> mcm;
   private ShapeRenderer shapeRenderer;
   private Camera camera;

   public AimingReticleRenderingSystem(Camera camera) {
      super(Family.all(IsPlayerComponent.class, MovementComponent.class).get());
      mcm = ComponentMapper.getFor(MovementComponent.class);
      shapeRenderer = new ShapeRenderer();
      this.camera = camera;
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
      MovementComponent mc = mcm.get(entity);
      if (!MathUtils.isZero(mc.shootX, 0.00001f) || !MathUtils.isZero(mc.shootY, 0.000001f)) {
         camera.update();
         shapeRenderer.setProjectionMatrix(camera.combined);
         shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
         Vector2 bodyPosition = entity.getComponent(BodyComponent.class).body.getPosition();
         float x = bodyPosition.x;
         float y = bodyPosition.y;
         boolean draw = true;
         float rand = 0.01f;
         while (x >= 0 && x < Utilities.FRUSTUM_WIDTH && y >= 0 && y < Utilities.FRUSTUM_HEIGHT) {
            if (LevelManager.getManager().getTile(x + (rand * mc.shootX), y + (rand * mc.shootY)) != null && LevelManager.getManager().getTile(x + (rand * mc.shootX), y + (rand * mc.shootY)).getType() == LevelManager.Type.WALL) {
               break;
            }
            if (LevelManager.getManager().getTile(x+0.5f + (rand * mc.shootX), y + (rand * mc.shootY)) != null && LevelManager.getManager().getTile(x+0.5f + (rand * mc.shootX), y + (rand * mc.shootY)).getType() == LevelManager.Type.WALL) {
               break;
            }
            if (LevelManager.getManager().getTile(x-1f + (rand * mc.shootX), y + (rand * mc.shootY)) != null && LevelManager.getManager().getTile(x-1f + (rand * mc.shootX), y + (rand * mc.shootY)).getType() == LevelManager.Type.WALL) {
               break;
            }

            if (LevelManager.getManager().getTile(x + (rand * mc.shootX), y+0.5f + (rand * mc.shootY)) != null && LevelManager.getManager().getTile(x + (rand * mc.shootX), y+0.5f + (rand * mc.shootY)).getType() == LevelManager.Type.WALL) {
               break;
            }
            if (LevelManager.getManager().getTile(x + (rand * mc.shootX), y-0.5f + (rand * mc.shootY)) != null && LevelManager.getManager().getTile(x + (rand * mc.shootX), y-0.5f + (rand * mc.shootY)).getType() == LevelManager.Type.WALL ) {
               break;
            }

            if (draw && mc.shot) {
               shapeRenderer.rectLine(x, y, x + (rand * mc.shootX), y + (rand * mc.shootY), 0.001f);
               draw = false;
            } else {
               draw = true;
            }
            x = x + (rand * mc.shootX);
            y = y + (rand * mc.shootY);
         }
         shapeRenderer.end();
      }
   }
}
