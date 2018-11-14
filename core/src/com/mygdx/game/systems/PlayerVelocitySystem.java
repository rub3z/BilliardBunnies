package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.components.IsPlayerComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerVelocityStatComponent;
import com.mygdx.game.utilities.Utilities;

/**
 * A system responsible for updating players' velocity
 */
public class PlayerVelocitySystem extends IntervalSystem {
    private ImmutableArray<Entity> entities;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<PlayerVelocityStatComponent> pvm;

    public PlayerVelocitySystem() {
        super(Utilities.MAX_STEP_TIME);
        mm = ComponentMapper.getFor(MovementComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        pvm = ComponentMapper.getFor(PlayerVelocityStatComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine){
        entities=engine.getEntitiesFor(Family.all(BodyComponent.class,MovementComponent.class,IsPlayerComponent.class).get());
    }

    /**
     * The processing logic of the system should be placed here.
     */
    @Override
    protected void updateInterval() {
        for(Entity entity: entities){
            BodyComponent bC= bm.get(entity);
            MovementComponent mC = mm.get(entity);
            PlayerVelocityStatComponent pVC = pvm.get(entity);
            bC.body.setLinearVelocity(
/*            (mC.moveRight ? pVC.movingSpeed : 0) - (mC.moveLeft ? pVC.movingSpeed : 0),
            (mC.moveUp    ? pVC.movingSpeed : 0) - (mC.moveDown ? pVC.movingSpeed : 0));*/
             mC.moveX > 0.2 || mC.moveX < -0.2 ? mC.moveX * pVC.movingSpeed : 0,
             mC.moveY > 0.2 || mC.moveY < -0.2 ? mC.moveY * pVC.movingSpeed : 0);

        }
    }
}
