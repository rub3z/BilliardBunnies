package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.SpookyShooter;

/**
 * This is the main menu. It will show all options available to players.
 */
public class MainMenuScreen extends ScreenAdapter {
   SpriteBatch batch;
   Texture img;
   Controller p1;
   /**
    * This is the reference to the game object.
    */
   private Game myGame;


   /**
    * Constructor for this screen
    *
    * @param myGame
    */
   public MainMenuScreen(Game myGame) {
      this.myGame = myGame;
      batch = new SpriteBatch();
      img = new Texture("GameScreen/Gfx/secondScreen.jpg");
      if (Controllers.getControllers().size >= 1)
         p1 = Controllers.getControllers().get(0);
   }

   /**
    * This is the main loop of this screen.
    *
    * @param delta time between current frame and last frame
    */
   @Override
   public void render(float delta) {
      Gdx.gl.glClearColor(0, 0, 0, 0);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      batch.begin();
      batch.draw(img, Gdx.graphics.getWidth() / 2 - img.getWidth() / 2, Gdx.graphics.getHeight() / 2 - img.getHeight() / 2);
      batch.end();
      if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
         System.out.println("Press captured");
         ((SpookyShooter) myGame).changeScreen(3, 1);
      }
      if (p1 != null) {
         if (p1.getButton(0)) {
            System.out.println("Press captured");
            ((SpookyShooter) myGame).changeScreen(3, 1);
         }
      }
      if (Controllers.getControllers().size >= 2 && p1.getButton(2)) {
         System.out.println("Press captured");
         ((SpookyShooter) myGame).changeScreen(3, 2);
      }
      if (Controllers.getControllers().size >= 3 && p1.getButton(1)) {
         System.out.println("Press captured");
         ((SpookyShooter) myGame).changeScreen(3, 3);
      }
      if (Controllers.getControllers().size >= 4 && p1.getButton(3)) {
         System.out.println("Press captured");
         ((SpookyShooter) myGame).changeScreen(3, 4);
      }

   }
}
