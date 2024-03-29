package com.mygdx.game.components.Scripts;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.*;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.utilities.Utilities;

public class WallBeginCollisionCallback implements CollisionCallback {
    private ComponentMapper<IsBulletComponent> isBulletComponentComponentMapper= ComponentMapper.getFor(IsBulletComponent.class);
    @Override
    public void run(Entity thisObject, Entity otherObject) {
        if(isBulletComponentComponentMapper.get(otherObject)!=null) {
            float angle = Utilities.vectorToAngle(otherObject.getComponent(BodyComponent.class).body.getLinearVelocity());
            angle += MathUtils.random(0, 0.523599f)-0.523599f*3;
            Vector2 normalizeVector = Vector2.Zero;
            Utilities.angleToVector(normalizeVector, angle);
            otherObject.getComponent(BodyComponent.class).body.setLinearVelocity(30 * normalizeVector.x, 30 * normalizeVector.y);

            if(otherObject.getComponent(ParticleEffectComponent.class)!=null){
                otherObject.getComponent(ParticleEffectComponent.class).effect.getComponent(ParticleEffectDataComponent.class).isHidden=true;
            }
            if (otherObject.getComponent(BulletVelocityStatComponent.class).numBounces > 0) {
               otherObject.getComponent(BulletVelocityStatComponent.class).numBounces--;
            }
           if (otherObject.getComponent(BulletVelocityStatComponent.class).numBounces == 0) {
              otherObject.add(Factory.getFactory().getEngine().createComponent(NeedToRemoveComponent.class));
           }
        }
    }
}
