package com.mygdx.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.game.components.*;
import com.mygdx.game.components.Scripts.*;
import com.mygdx.game.systems.*;
import com.mygdx.game.systems.CollisionCallbackSystem;
import com.mygdx.game.systems.PhysicsDebugSystem;
import com.mygdx.game.systems.PhysicsSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utilities.*;

/***
 * A factory that handles all the creation of entity, system, and assets loading.
 */
public class Factory {


   /**
    * A singleton to this object.
    */
   private static Factory factory;

   /**
    * Manager of all assets.
    */
   private AssetManager assetManager;

   /**
    * Pooled Engine of Ashely. It used to keep tracks of entities, components, and systems.
    */
   private PooledEngine engine;

   /**
    * World of Box2D. It keeps track of all box2d bodies
    */
   private World world;

   /**
    * Use to draw on the screen.
    */
   private SpriteBatch spriteBatch;

   /**
    * Camera
    */
   private OrthographicCamera camera;

   /**
    * Loader for hitbox
    */

   private BodyEditorLoader bodyEditorLoader;
   /**
    * Default Constructor
    */

   /**
    * Manager to create and pool particle effects
    */
   private ParticleEffectManager particleEffectManager;

   /**
    * Array contains players in our engine.
    */
   public ImmutableArray<Entity> players;

   /**
    * Array contains boss in our engine.
    */
   public ImmutableArray<Entity> boss;

   /**
    * Animation manager contains all of our pooled animations.
    */
   private AnimationManager animationManager;


   private Factory() {
      assetManager = new AssetManager(); //Declare AssetManager
      loadAssets();// Load assets

      world = new World(Vector2.Zero, true);//Declare World

      spriteBatch = new SpriteBatch();//Declare SpriteBatch

      //Set up camera
      camera = new OrthographicCamera(Utilities.FRUSTUM_WIDTH, Utilities.FRUSTUM_HEIGHT);
      camera.position.set(Utilities.FRUSTUM_WIDTH / 2f, Utilities.FRUSTUM_HEIGHT / 2f, 0);

      bodyEditorLoader = new BodyEditorLoader(Gdx.files.internal("GameScreen/HitboxData.json"));//Declare hitbox loader
      engine = new PooledEngine(10,500,500,500); //Ashely engine
       //Load systems into engine
      loadSystemsIntoEngine();
      loadParticleEffects();
      players=engine.getEntitiesFor(Family.all(IsPlayerComponent.class).get());
      boss=engine.getEntitiesFor(Family.all(IsBossComponent.class).get());
      animationManager=new AnimationManager();
   }

   /**
    * Get a singleton Factory object.
    *
    * @return Factory object
    */
   public static Factory getFactory() {
      if (factory == null) {
         factory = new Factory();
      }
      return factory;

   }

   /**
    * Get access to the AssetManager
    *
    * @return AssetManager
    */
   public AssetManager getAssetManager() {
      return assetManager;
   }

   /**
    * Load predetermine assets into AssetManager.
    */
   private void loadAssets() {
      //Loading normal texture
      assetManager.load("GameScreen/TexturesPack.atlas",TextureAtlas.class);

      //Loading assets
      ParticleEffectLoader.ParticleEffectParameter particleEffectParameter= new ParticleEffectLoader.ParticleEffectParameter();
      particleEffectParameter.atlasFile="GameScreen/TexturesPack.atlas";
      assetManager.load("GameScreen/Effects/CandyCornExplode.p",ParticleEffect.class,particleEffectParameter);
      assetManager.load("GameScreen/Effects/SmokeTrail.p",ParticleEffect.class,particleEffectParameter);
      assetManager.finishLoading();
   }

   /**
    * Call this function to access World of Box2D
    *
    * @return Box2D World
    */
   public World getWorld() {
      return world;
   }

   /**
    * Call this method to access SpriteBatch
    *
    * @return SpriteBatch.
    */
   public SpriteBatch getSpriteBatch() {
      return spriteBatch;
   }

   /**
    * Call this method to create Player entity
    *
    * @return a player.
    */
   public Entity createPlayer(String player, float posx, float posy, int playerNum) {
      Entity entity = engine.createEntity();
      entity.add(engine.createComponent(IsHeroComponent.class));
      entity.add(engine.createComponent(MovementComponent.class));
      entity.add(engine.createComponent(PlayerVelocityStatComponent.class));
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.add(engine.createComponent(IsPlayerComponent.class));
      entity.add(engine.createComponent(SteeringComponent.class));
      entity.add(engine.createComponent(CollisionCallbackComponent.class));
      entity.getComponent(CollisionCallbackComponent.class).beginContactCallback=Pools.get(PlayerCollisionCallback.class).obtain();
      entity.getComponent(IsPlayerComponent.class).playerNum = playerNum;
      entity.add(engine.createComponent(BulletVelocityStatComponent.class));
      entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture( player, 5f);
      entity.getComponent(TextureComponent.class).name=player;
      entity.getComponent(BodyComponent.class).body = createBody("Circle", posx, posy, 1.5f,false);
      entity.getComponent(TransformComponent.class).scale.x = 2f;
      entity.getComponent(TransformComponent.class).scale.y = 2f;
      entity.getComponent(BodyComponent.class).body.setUserData(entity);
      switch (playerNum){
         case 0:
            applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_PLAYER_ONE, Utilities.MASK_PLAYER_ONE,false);
//            Utilities.MASK_SEED= (short) (Utilities.MASK_SEED|Utilities.CATEGORY_PLAYER_ONE);
            entity.getComponent(IsPlayerComponent.class).isEnemy = false;
            entity.getComponent(IsPlayerComponent.class).health = 10;
            break;
         case 1:
            applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_PLAYER_TWO, Utilities.MASK_PLAYER_TWO,false);
//            Utilities.MASK_SEED= (short) (Utilities.MASK_SEED|Utilities.CATEGORY_BULLET_TWO);
            entity.getComponent(IsPlayerComponent.class).isEnemy = true;
            entity.getComponent(IsPlayerComponent.class).health = 100;
            entity.add(engine.createComponent(EnemyStatsComponent.class));
            entity.add(engine.createComponent(IsRabbitComponent.class));
            entity.getComponent(TextureComponent.class).textureRegionAnimation=createTexture("Bunny_2",10f);
             entity.getComponent(TextureComponent.class).name="Bunny_2";
            break;
         case 2:
            applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_PLAYER_THREE, Utilities.MASK_PLAYER_THREE,false);
//            Utilities.MASK_SEED= (short) (Utilities.MASK_SEED|Utilities.CATEGORY_PLAYER_THREE);
            entity.getComponent(IsPlayerComponent.class).isEnemy = true;
            entity.getComponent(IsPlayerComponent.class).health = 100;
            entity.add(engine.createComponent(EnemyStatsComponent.class));
            entity.add(engine.createComponent(IsRabbitComponent.class));
            entity.getComponent(TextureComponent.class).textureRegionAnimation=createTexture("Bunny_2",10f);
             entity.getComponent(TextureComponent.class).name="Bunny_2";

            break;
            default:
               applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_PLAYER_FOUR, Utilities.MASK_PLAYER_FOUR,false);
//               Utilities.MASK_SEED= (short) (Utilities.MASK_SEED|Utilities.CATEGORY_PLAYER_FOUR);
               entity.getComponent(IsPlayerComponent.class).isEnemy = true;
               entity.getComponent(IsPlayerComponent.class).health = 100;
               entity.add(engine.createComponent(EnemyStatsComponent.class));
               entity.add(engine.createComponent(IsRabbitComponent.class));
               entity.getComponent(TextureComponent.class).textureRegionAnimation=createTexture("Bunny_2",10f);
                entity.getComponent(TextureComponent.class).name="Bunny_2";
               break;
      }
      entity.getComponent(SteeringComponent.class).body=entity.getComponent(BodyComponent.class).body;
      return entity;
   }


   public Entity createPlayerEnemy(String player, float posx, float posy, int playerNum) {
      Entity entity = engine.createEntity();
      //entity.add(engine.createComponent(MovementComponent.class));
      entity.add(engine.createComponent(PlayerVelocityStatComponent.class));
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.add(engine.createComponent(EnemyStatsComponent.class));

      entity.add(engine.createComponent(IsPlayerComponent.class));
      entity.add(engine.createComponent(SteeringComponent.class));
      entity.add(engine.createComponent(CollisionCallbackComponent.class));
      entity.getComponent(CollisionCallbackComponent.class).beginContactCallback=Pools.get(PlayerCollisionCallback.class).obtain();
      entity.getComponent(IsPlayerComponent.class).playerNum = playerNum;
      entity.add(engine.createComponent(BulletVelocityStatComponent.class));
      entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture( player, 5f);
      entity.getComponent(TextureComponent.class).name=player;
      entity.getComponent(BodyComponent.class).body = createBody("Circle", posx, posy, 1.45f,false);
      entity.getComponent(TransformComponent.class).scale.x = 2f;
      entity.getComponent(TransformComponent.class).scale.y = 2f;
      entity.getComponent(BodyComponent.class).body.setUserData(entity);
      applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_PLAYER_FOUR, Utilities.MASK_PLAYER_FOUR,false);
      entity.getComponent(SteeringComponent.class).body=entity.getComponent(BodyComponent.class).body;
      entity.getComponent(IsPlayerComponent.class).health = 100000;
      entity.getComponent(EnemyStatsComponent.class).aimedAtTarget=true;
      entity.getComponent(EnemyStatsComponent.class).target=players.get(0);
      entity.getComponent(EnemyStatsComponent.class).rof=MathUtils.random(0.5f,2f);
      entity.getComponent(IsPlayerComponent.class).isEnemy = true;
      entity.add(engine.createComponent(IsRabbitComponent.class));
      entity.getComponent(TextureComponent.class).textureRegionAnimation=createTexture("Bunny_2",10f);
       entity.getComponent(TextureComponent.class).name="Bunny_2";

      return entity;
   }

   /**
    * Call this method to create Bullet entity
    *
    * @return a bullet.
    */
   public Entity shoot(float x, float y, int playerNum) {
      Entity entity = engine.createEntity();
      entity.add(engine.createComponent(MovementComponent.class));
      entity.add(engine.createComponent(BulletVelocityStatComponent.class));
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.add(engine.createComponent(IsBulletComponent.class));
      entity.getComponent(IsBulletComponent.class).playerNum = playerNum;
      entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture("Bullet_2", 1);
      entity.getComponent(TextureComponent.class).name="Bullet_2";
      entity.getComponent(BodyComponent.class).body = createBody("Circle", x, y, 1f,true);
      entity.getComponent(BodyComponent.class).body.setBullet(true);
      entity.getComponent(TransformComponent.class).scale.x = 0.5f;
      entity.getComponent(TransformComponent.class).scale.y = 0.5f;
      entity.getComponent(BodyComponent.class).body.setUserData(entity);

      switch (playerNum+1){
         case 1:
            applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_BULLET_ONE, Utilities.MASK_BULLET_ONE,false);

            break;
         case 2:
            applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_BULLET_TWO, Utilities.MASK_BULLET_TWO,false);

            break;
         case 3:
            applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_BULLET_THREE, Utilities.MASK_BULLET_THREE,false);

            break;
         default:
            applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_BULLET_FOUR, Utilities.MASK_BULLET_FOUR,false);

            break;
      }
       engine.addEntity(entity);

       //Add particle to bullet
      entity.add(engine.createComponent(ParticleEffectComponent.class));
      Entity particle=createParticleEffect(ParticleEffectManager.SMOKETRIAL,entity.getComponent(BodyComponent.class));
      particle.getComponent(ParticleEffectDataComponent.class).isLooped=true;
      entity.getComponent(ParticleEffectComponent.class).effect= particle;
       particle.getComponent(ParticleEffectDataComponent.class).angleOffset=(float)Math.PI;


       return entity;
   }

   /**
    * Call this method to create an Enemy entity
    *
    * @return an enemy.
    */
   public Entity spawnEnemy(float x, float y, int type) {
      Entity entity = engine.createEntity();
      entity.add(engine.createComponent(EnemyStatsComponent.class));
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.add(engine.createComponent(IsEnemyComponent.class));
      entity.add(engine.createComponent(CollisionCallbackComponent.class));

      entity.getComponent(CollisionCallbackComponent.class).beginContactCallback =
       Pools.get(EnemyCollisionCallback.class).obtain();
      entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture("Enemies_0", 5);
      entity.getComponent(TextureComponent.class).name="Enemies_0";
      entity.getComponent(BodyComponent.class).body = createBody("Enemies_0", x, y, 4,false);
      entity.getComponent(BodyComponent.class).body.setType(BodyDef.BodyType.DynamicBody);
      entity.getComponent(TransformComponent.class).scale.x = 2f;
      entity.getComponent(TransformComponent.class).scale.y = 2f;
      entity.getComponent(BodyComponent.class).body.setUserData(entity);
//      applyCollisionFilter(entity.getComponent(BodyComponent.class).body,
//       Utilities.CATEGORY_ENEMY, Utilities.MASK_ENEMY,true);
      entity.getComponent(EnemyStatsComponent.class).aimedAtTarget=true;
      entity.getComponent(EnemyStatsComponent.class).target=players.get(0);
      entity.add(engine.createComponent(SteeringComponent.class));
      entity.getComponent(SteeringComponent.class).body=entity.getComponent(BodyComponent.class).body;
      entity.add(engine.createComponent(BehaviorComponent.class));
      entity.getComponent(SteeringComponent.class).setMaxLinearSpeed(50f);
      entity.getComponent(EnemyStatsComponent.class).rof=MathUtils.random(0.5f,2f);
      entity.getComponent(SteeringComponent.class).scale=4f;
      engine.addEntity(entity);
      entity.getComponent(TransformComponent.class).position.set( entity.getComponent(TransformComponent.class).position.x, entity.getComponent(TransformComponent.class).position.y,2);

      return entity;
   }



   /**
    * Access Orthographic Camera  for this game.
    *
    * @return Orthographic Camera
    */
   public OrthographicCamera getCamera() {
      return camera;
   }

   /**
    * Get the engine of Ashely
    *
    * @return pooled engine.
    */
   public PooledEngine getEngine() {
      return engine;
   }

   /**
    * Load systems into the Ashley engine.
    */
   private void loadSystemsIntoEngine() {
      engine.addEntityListener(new RenderingSystem(spriteBatch, camera));
      engine.addSystem(new PhysicsSystem(world));
      engine.addSystem(new RenderingSystem(spriteBatch, camera));
      //engine.addSystem(new PhysicsDebugSystem(world, camera));
      engine.addSystem(new PlayerControlSystem());
      engine.addSystem(new PlayerVelocitySystem());
      engine.addSystem(new EntityRemovingSystem(world,engine));
      engine.addSystem(new BulletVelocitySystem());
      engine.addSystem(new SteeringSystem());
      //engine.addSystem(new EnemiesSpawnSystem());
      engine.addSystem(new BuffSystem());
      engine.addSystem(new BehaviorSystem());
      new CollisionCallbackSystem(world);
      engine.addSystem(new DetectEndGameSystem());
      engine.addSystem(new EnemyFireSystem());
      engine.addSystem(new ParticleEffectSystem(spriteBatch,camera));
      //engine.addSystem(new AISystem());
      engine.addSystem(new AimingReticleRenderingSystem(camera));
      engine.addSystem(new RabbitSpriteSwitcher());
   }

   /**
    * Call this function to create TextureRegion
    *
    * @param name  name of the texture in the atlas
    * @param fps how many frame should be playing
    * @return TextureRegion
    */
   public Animation<TextureRegion> createTexture(String name, float fps) {
      TextureAtlas atlas = assetManager.get("GameScreen/TexturesPack.atlas", TextureAtlas.class);
      Animation<TextureRegion> animation= animationManager.getPooledAnimation(name,atlas.findRegions(name));
      animation.setFrameDuration(1f/fps);
      return animation;
   }

   /**
    * Call this function to create Box2D body
    *
    * @param nameOfBody of the body
    * @return Box2D body
    */
   public Body createBody(String nameOfBody, float posX, float posY,  float scale, boolean isElastic) {
      BodyDef bodyDef = Pools.get(BodyDef.class).obtain();
      bodyDef.type = BodyDef.BodyType.DynamicBody;
      bodyDef.position.set(posX, posY);
      Body body = world.createBody(bodyDef);
      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.density = 1;
      fixtureDef.isSensor=true;
      fixtureDef.friction=0f;
      fixtureDef.restitution=isElastic?1f:0f;
      if(nameOfBody.compareTo("Circle")==0){
         CircleShape circle = new CircleShape();
         circle.setRadius(scale/2);
         fixtureDef.shape = circle;
         body.createFixture(fixtureDef);
         body.setAngularVelocity(2f);
      }else{
         bodyEditorLoader.attachFixture(body, nameOfBody, fixtureDef, scale);
      }
      body.setFixedRotation(true);
      Pools.get(BodyDef.class).free(bodyDef);
      return body;
   }

   /**
    * Call this method to create entities for the start of the game.d
    */
   public void createEntities(int playerCount) {
      spawnWalls();
      spawnPlayerBoundary();


      for(int i = 0; i < 4; i++){
         Tile t=LevelManager.getManager().getCenter();
         float x =0;
         float y =0;
         switch (i){
            case 0:
               t=LevelManager.getManager().getCenter();
               break;
            case 1:
               t=LevelManager.getManager().getTile(2,2);
               break;
            case 2:
               t=LevelManager.getManager().getTile(57,2);
               break;
            case 3:
               t=LevelManager.getManager().getTile(57,42);
               break;
            case 4:
               t=LevelManager.getManager().getTile(2,42);
         }
         x=t.getX();
         y=t.getY();
         int num = 2+i;
         String s ="Player_"+num;
         if(i < playerCount)
            engine.addEntity(createPlayer(s, x, y, i));
//         else{
//            engine.addEntity(createPlayerEnemy(s, x, y, i));
//         }

      }


      //Spawning seeds in four quadrants
      Tile t =  LevelManager.getManager().getARandomEmptyTileInQuadrantOne();
      spawnSeed(t.getX(), t.getY());

      Tile t2 = LevelManager.getManager().getARandomEmptyTileInQuadrantTwo();
      spawnSeed(t2.getX(), t2.getY());

      Tile t3 = LevelManager.getManager().getARandomEmptyTileInQuadrantThree();
      spawnSeed(t3.getX(), t3.getY());

      Tile t4 = LevelManager.getManager().getARandomEmptyTileInQuadrantFour();
      spawnSeed(t4.getX(), t4.getY());

   }

   /**
    * Apply collision filter to this body
    * @param body which body to apply to
    * @param categoryBits which category is this body belong to. LOOK AT UTILITIES for more detail.
    * @param maskingBits whiich category should this body collide with. LOOK AT UTILITIES for more detail.
    */
   public void applyCollisionFilter(Body body, short categoryBits, short maskingBits, boolean isSensor) {
      Array<Fixture> fixtures = body.getFixtureList();
      for (Fixture fixture : fixtures) {
         Filter filter = fixture.getFilterData();
         filter.categoryBits = categoryBits;
         filter.maskBits = maskingBits;
         fixture.setSensor(isSensor);
         fixture.setFilterData(filter);
      }
   }

   /**
    * Create an invisible block of wall
    * @param x x-coordinate
    * @param y y-coordinate
    * @param scale block scale
    * @return a created block of wall
    */
   private Entity createAnInvisibleWall(float x, float y, float scale, int type){
       Entity entity = engine.createEntity();
       entity.add(engine.createComponent(BodyComponent.class));
       entity.add(engine.createComponent(CollisionCallbackComponent.class));
       entity.getComponent(CollisionCallbackComponent.class).beginContactCallback=Pools.get(InvisibleWallCollisionCallback.class).obtain();
       entity.getComponent(BodyComponent.class).body = createBody("Wall_0",x, y, scale,false);
       entity.getComponent(BodyComponent.class).body.setUserData(entity);
       entity.getComponent(BodyComponent.class).body.setType(BodyDef.BodyType.StaticBody);
       if(type==1){
          applyCollisionFilter(entity.getComponent(BodyComponent.class).body,
                  Utilities.CATEGORY_PLAYER_BOUNDARY, Utilities.MASK_PLAYER_BOUNDARY,false);
       }else {
          applyCollisionFilter(entity.getComponent(BodyComponent.class).body,
                  Utilities.CATEGORY_BULLET_BOUNDARY, Utilities.MASK_BULLET_BOUNDARY,false);
       }

        return  entity;
   }

    /**
     *Call this method to create boundary that delete bullet and prevent player from passing through it.
     * @param x x-coordinate of the boundary wall in meter
     * @param y y-coordinate of the boundary wall in meter
     * @param width width of the boundary wall in meter
     * @param height height of the boundary wall in meter
     * @param scale how big should each block of the wall be. Default value in 1 meter
     */
   private void createInvisibleWall(float x, float y, float width, float height, float scale, int type){
       //Top and Buttom
       for (float i=x; i<=x+width;i=i+scale){
           createAnInvisibleWall(i,y,scale,type);
           createAnInvisibleWall(i,y+height,scale,type);
       }
       //Left and right wall
       for (float i = y+scale; i<=y+height;i=i+scale){
           createAnInvisibleWall(x,i,scale,type);
           createAnInvisibleWall(x+width,i,scale,type);
       }
   }

   /**
    * Spawn a bullet for an enemy to use
    * @param x x-coordinate
    * @param y y-coordinate
    * @param type type of bullet
    * @return a bullet
    */
   public Entity createEnemyBullet (float x, float y, int type){
      Entity entity = engine.createEntity();
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.add(engine.createComponent(IsEnemyBulletComponent.class));
      if(type==0){
         entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture( "Bullet_0", 8);
         entity.getComponent(TextureComponent.class).name="Bullet_0";
         entity.getComponent(BodyComponent.class).body = createBody("Bullet_0", x, y, 2f,false);
         entity.getComponent(TransformComponent.class).scale.x = 1f;
         entity.getComponent(TransformComponent.class).scale.y = 1f;
      }else if(type==1){
         entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture( "Bullet_1", 8);
         entity.getComponent(TextureComponent.class).name="Bullet_1";
         entity.getComponent(BodyComponent.class).body = createBody("Bullet_1", x, y, 2.4f,false);
         entity.getComponent(TransformComponent.class).scale.x = 0.2f;
         entity.getComponent(TransformComponent.class).scale.y = 0.2f;
      }else if(type==2){
         entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture( "Bullet_1", 8);
         entity.getComponent(TextureComponent.class).name="Bullet_1";
         entity.getComponent(BodyComponent.class).body = createBody("Bullet_1", x, y, 12f,false);
         entity.getComponent(TransformComponent.class).scale.x = 1f;
         entity.getComponent(TransformComponent.class).scale.y = 1f;
      }


      entity.getComponent(BodyComponent.class).body.setUserData(entity);
      applyCollisionFilter(entity.getComponent(BodyComponent.class).body, Utilities.CATEGORY_BULLET_FOUR, Utilities.MASK_BULLET_FOUR, true);
      engine.addEntity(entity);
      entity.getComponent(TransformComponent.class).position.set( entity.getComponent(TransformComponent.class).position.x, entity.getComponent(TransformComponent.class).position.y,0);
      return entity;
   }

   /**
    * Load particle presets from file into our game.
    */
   public void loadParticleEffects(){
      particleEffectManager=new ParticleEffectManager();
      particleEffectManager.addParticleEffect(
              ParticleEffectManager.CANDYCORNEXPLOSION,assetManager.get("GameScreen/Effects/CandyCornExplode.p",
                      ParticleEffect.class),
              1/5f);
      particleEffectManager.addParticleEffect(
              ParticleEffectManager.SMOKETRIAL,assetManager.get("GameScreen/Effects/SmokeTrail.p",
                      ParticleEffect.class),
              1/5f);
   }

   /**
    * Create a particle effect
    * @param type type of effect
    * @param x x-coordinate
    * @param y y-coordinate
    * @return a particle effect
    */
   public Entity createParticleEffect(int type, float x, float y){
      Entity entity=engine.createEntity();
      ParticleEffectDataComponent particleEffectComponent=engine.createComponent(ParticleEffectDataComponent.class);
      particleEffectComponent.particleEffect=particleEffectManager.getPooledParticleEffect(type);
      particleEffectComponent.particleEffect.setPosition(x,y);
      entity.add(particleEffectComponent);
      engine.addEntity(entity);
      return entity;
   }

   /**
    * Crate a particle effect and attach it to another entity
    * @param type type of effect
    * @param bodyComponent body in which to attach the particle effect to
    * @return a particle effect
    */
   public Entity createParticleEffect(int type, BodyComponent bodyComponent){
      return createParticleEffect(type,bodyComponent,0f,0f);
   }

   /**
    * Crate a particle effect and attach it to another entity
    * @param type type of effect
    * @param bodyComponent body in which to attach the particle effect to
    * @param xOffSet x-offset from the body
    * @param yOffset y-offset from the body
    * @return a particle effect
    */
   public Entity createParticleEffect(int type, BodyComponent bodyComponent, float xOffSet, float yOffset) {
      Entity entity = engine.createEntity();
      ParticleEffectDataComponent particleEffectComponent = engine.createComponent(ParticleEffectDataComponent.class);
      particleEffectComponent.particleEffect = particleEffectManager.getPooledParticleEffect(type);
      particleEffectComponent.particleEffect.setPosition(bodyComponent.body.getPosition().x, bodyComponent.body.getPosition().y);
      particleEffectComponent.particleEffect.getEmitters().first().setAttached(true); //manually attach for testing
      particleEffectComponent.xOffset = xOffSet;
      particleEffectComponent.yOffset = yOffset;
      particleEffectComponent.isAttached = true;
      particleEffectComponent.attachedBody = bodyComponent.body;
      entity.add(particleEffectComponent);
      engine.addEntity(entity);
      return entity;
   }

   /**
    * Create an enemy that randomly target a player
    * @param x x-coordinate
    * @param y y-coordinate
    * @param behavior enemy's behavior
    * @return an enemy
    */
   public Entity spawnEnemy2(float x, float y, String behavior) {
      Entity entity = engine.createEntity();
      entity.add(engine.createComponent(EnemyStatsComponent.class));
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.add(engine.createComponent(IsEnemyComponent.class));
      entity.add(engine.createComponent(CollisionCallbackComponent.class));

      entity.getComponent(CollisionCallbackComponent.class).beginContactCallback =
              Pools.get(EnemyCollisionCallback.class).obtain();
      entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture( "Enemies_1", 5);
      entity.getComponent(TextureComponent.class).name="Enemies_1";
      entity.getComponent(BodyComponent.class).body = createBody("Enemies_1", x, y, 4,false);
      entity.getComponent(BodyComponent.class).body.setType(BodyDef.BodyType.DynamicBody);
      entity.getComponent(TransformComponent.class).scale.x = 1.2f;
      entity.getComponent(TransformComponent.class).scale.y = 1.2f;
      entity.getComponent(BodyComponent.class).body.setUserData(entity);
//      applyCollisionFilter(entity.getComponent(BodyComponent.class).body,
//              Utilities.CATEGORY_ENEMY, Utilities.MASK_ENEMY,true);

      entity.add(engine.createComponent(SteeringComponent.class));
      entity.getComponent(SteeringComponent.class).body=entity.getComponent(BodyComponent.class).body;
      entity.getComponent(SteeringComponent.class).setMaxLinearSpeed(10f);
      entity.add(engine.createComponent(BehaviorComponent.class));
      entity.getComponent(BehaviorComponent.class).behaviors= BehaviorBuilder.getInstance().load(behavior);
      if(players.size()>0){
         entity.getComponent(EnemyStatsComponent.class).aimedAtTarget=true;
         entity.getComponent(EnemyStatsComponent.class).target=players.get((MathUtils.random(0,players.size()-1)));
      }
      entity.getComponent(EnemyStatsComponent.class).bulletType=1;
      entity.getComponent(EnemyStatsComponent.class).speed=20f;
      entity.getComponent(EnemyStatsComponent.class).rof=4f;
      engine.addEntity(entity);
      entity.getComponent(SteeringComponent.class).scale=2f;
      entity.getComponent(TransformComponent.class).position.set( entity.getComponent(TransformComponent.class).position.x, entity.getComponent(TransformComponent.class).position.y,3);
      return entity;
   }

   /**
    * Spawn boss that aim at a target
    * @param x x-coordinate
    * @param y y-coordinate
    * @param behavior behavior of the boss
    * @return boss entity
    */
   public Entity spawnBoss(float x, float y, String behavior){
      Entity entity = engine.createEntity();
      entity.add(engine.createComponent(EnemyStatsComponent.class));
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.add(engine.createComponent(IsEnemyComponent.class));
      entity.add(engine.createComponent(CollisionCallbackComponent.class));

      entity.getComponent(CollisionCallbackComponent.class).beginContactCallback =
              Pools.get(EnemyCollisionCallback.class).obtain();
      entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture( "Enemies_2", 5);
      entity.getComponent(TextureComponent.class).name="Enemies_2";
      entity.getComponent(BodyComponent.class).body = createBody("Enemies_2", x, y, 105f,false);
      entity.getComponent(BodyComponent.class).body.setType(BodyDef.BodyType.DynamicBody);
      entity.getComponent(TransformComponent.class).scale.x = 1f;
      entity.getComponent(TransformComponent.class).scale.y = 1f;
      entity.getComponent(BodyComponent.class).body.setUserData(entity);
//      applyCollisionFilter(entity.getComponent(BodyComponent.class).body,
//              Utilities.CATEGORY_ENEMY, Utilities.MASK_ENEMY,true);

      entity.add(engine.createComponent(SteeringComponent.class));
      entity.getComponent(SteeringComponent.class).body=entity.getComponent(BodyComponent.class).body;
      entity.getComponent(SteeringComponent.class).setMaxLinearSpeed(50f);
      entity.add(engine.createComponent(BehaviorComponent.class));
      entity.getComponent(BehaviorComponent.class).behaviors= BehaviorBuilder.getInstance().load(behavior);
      entity.getComponent(EnemyStatsComponent.class).aimedAtTarget=true;
      entity.getComponent(EnemyStatsComponent.class).target=players.get((MathUtils.random(0,players.size()-1)));
      entity.getComponent(EnemyStatsComponent.class).bulletType=2;
      entity.getComponent(EnemyStatsComponent.class).speed=25f;
      entity.getComponent(EnemyStatsComponent.class).rof=4f;
      entity.add(engine.createComponent(IsBossComponent.class));
      entity.getComponent(TransformComponent.class).position.set( entity.getComponent(TransformComponent.class).position.x, entity.getComponent(TransformComponent.class).position.y,1);
      entity.getComponent(SteeringComponent.class).scale=105f;
      engine.addEntity(entity);

      return entity;
   }

   public AnimationManager getAnimationManager() {
      return animationManager;
   }

   public void spawnWalls(){
      int desiredCellWidth=5;
      int desiredCellHeight=5;
      float tileScale=1.5f;
      int resultedColumnNumber =12;
      int resultedRowNumber = 9;
      LevelManager.getManager().generateLevel(resultedColumnNumber,resultedRowNumber,desiredCellWidth,desiredCellHeight,tileScale,true,true);
      ImmutableArray<Tile> wallTiles = LevelManager.getManager().getWallTitles();
      for(Tile t: wallTiles){
         Entity entity = engine.createEntity();
         entity.add(engine.createComponent(BodyComponent.class));
         entity.getComponent(BodyComponent.class).body = createBody("Wall_0",
                 t.getX(),
                 t.getY(),
                 tileScale,false);
         entity.getComponent(BodyComponent.class).body.setUserData(entity);
         entity.getComponent(BodyComponent.class).body.setType(BodyDef.BodyType.StaticBody);
         applyCollisionFilter(entity.getComponent(BodyComponent.class).body,
          Utilities.CATEGORY_WALL, Utilities.MASK_WALL,false);
         entity.add(engine.createComponent(CollisionCallbackComponent.class));
         entity.getComponent(CollisionCallbackComponent.class).beginContactCallback=Pools.get(WallBeginCollisionCallback.class).obtain();
          entity.getComponent(CollisionCallbackComponent.class).endContactCallback=Pools.get(WallEndCollisionCallBack.class).obtain();
         entity.add(engine.createComponent(TransformComponent.class));
         entity.add(engine.createComponent(TextureComponent.class));
         entity.getComponent(TextureComponent.class).textureRegionAnimation=createTexture("Walls_0",0.1f);
         entity.getComponent(TextureComponent.class).name="Walls_0";

          engine.addEntity(entity);
      }
   }
   public void spawnPlayerBoundary(){
      ImmutableArray<Tile> playerBoundaryTiles = LevelManager.getManager().getPlayerWallTiles();
      for(Tile t: playerBoundaryTiles){
         Entity entity=createAnInvisibleWall(t.getX(),t.getY(),1.5f,1);
         engine.addEntity(entity);
      }
   }


   public Entity spawnSeed(float x, float y){
      Entity entity = engine.createEntity();
      entity.add(engine.createComponent(TransformComponent.class));
      entity.add(engine.createComponent(BodyComponent.class));
      entity.add(engine.createComponent(TextureComponent.class));
      entity.getComponent(TextureComponent.class).textureRegionAnimation = createTexture("Seed_0", 1);
      entity.getComponent(TextureComponent.class).name="Seed_0";
      entity.getComponent(BodyComponent.class).body = createBody("Circle", x, y, 1f,true);
      applyCollisionFilter(entity.getComponent(BodyComponent.class).body,Utilities.CATEGORY_SEED,Utilities.MASK_SEED,true);
      entity.getComponent(BodyComponent.class).body.setBullet(true);
      entity.getComponent(TransformComponent.class).scale.x = 1f;
      entity.getComponent(TransformComponent.class).scale.y = 1f;
      entity.getComponent(BodyComponent.class).body.setUserData(entity);
      entity.add(engine.createComponent(IsSeedComponent.class));
        engine.addEntity(entity);
      return entity;
   }
}