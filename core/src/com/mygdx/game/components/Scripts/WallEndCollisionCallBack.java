package com.mygdx.game.components.Scripts;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.components.IsBulletComponent;
import com.mygdx.game.components.ParticleEffectComponent;
import com.mygdx.game.components.ParticleEffectDataComponent;

public class WallEndCollisionCallBack implements CollisionCallback {
    private ComponentMapper<IsBulletComponent> isBulletComponentComponentMapper = ComponentMapper.getFor(IsBulletComponent.class);

    @Override
    public void run(Entity thisObject, Entity otherObject) {
        if (isBulletComponentComponentMapper.get(otherObject) != null) {
                if (otherObject.getComponent(ParticleEffectComponent.class) != null) {
                    otherObject.getComponent(ParticleEffectComponent.class).effect.getComponent(ParticleEffectDataComponent.class).isHidden = false;
                    otherObject.getComponent(ParticleEffectComponent.class).effect.getComponent(ParticleEffectDataComponent.class).particleEffect.reset(false);
                }
        }
    }
}
