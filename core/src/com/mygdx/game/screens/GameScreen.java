package com.mygdx.game.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.SpookyShooter;
import com.mygdx.game.components.BodyComponent;
import com.mygdx.game.entities.Factory;
import com.mygdx.game.ui.IngameOverlay;
import com.mygdx.game.utilities.Utilities;

/**
 * This is the game screen. It has access to all the game play that will occur.
 */
public class GameScreen extends ScreenAdapter {
   /** Singleton for this GameScreen.*/
   private static GameScreen gameScreen;

   SpriteBatch batch;
   public IngameOverlay ui;
   long timer = 0;
   public int score0, score1, score2, score3 = 0;
   int numPlayers;
   Texture background;
   /**
    * This is the reference to the game object.
    */
   private Game myGame;
   private PooledEngine engine;
   private float counter = 1f;

   /**
    * Constructor for this screen
    *
    * @param myGame
    */
   public GameScreen(Game myGame, int playerCount) {
      this.myGame = myGame;
      engine = Factory.getFactory().getEngine();
      ui=new IngameOverlay(playerCount);
      Factory.getFactory().createEntities(playerCount);
      numPlayers = playerCount;
      gameScreen = this;
      background = new Texture("GameScreen/sexyBackground.png");
      batch = Factory.getFactory().getSpriteBatch();
      counter++;
//GameScreen/Gfx/fourthScreen.jpg
   }

   /**
    * Get this screen.
    *
    * @return GameScreen object
    */
   public static GameScreen getGameScreen() {
      return gameScreen;
   }

   /**
    *
    * This the main loop of this screen.*
    * @param delta time between current frame and last frame
    */
   @Override
   public void render(float delta) {
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      engine.update(delta);
      batch.begin();
      batch.draw(background,0, counter,Utilities.FRUSTUM_WIDTH, Utilities.FRUSTUM_HEIGHT+10);
      batch.end();
      timer += (delta * MathUtils.random(100));
      ui.draw();
      if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
         endGame();
      }
      counter -= 0.2;
      if(counter < -2.2){
         counter = 1f;
      }
   }

   /**
    * This method resets all Entities and Hitboxes
    *
    */
   @Override
   public void dispose(){
      Factory.getFactory().getEngine().removeAllEntities();
      Array<Body> array=new Array<Body>();
      Factory.getFactory().getWorld().getBodies(array);
      for(Body body:array){//kills hit boxes
         Factory.getFactory().getWorld().destroyBody(body);
      }
   }

   /**
    * Call this method to end game.
    */
   public void endGame(){
      ((SpookyShooter) myGame).changeScreen(4, numPlayers);
      GameOverScreen.getScreen(myGame).addScore("Player 0",score0);
      this.dispose();
   }
}
