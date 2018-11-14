package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.utilities.Utilities;
import com.sun.javafx.binding.StringFormatter;

import java.lang.StringBuilder;
import java.util.Comparator;


public class Gameover extends Stage {

   private String path= "GameOverScreen/PlayerScores.json";

   private Json json;
   private FileHandle handle;
   private Array<Player> players;
   private Skin skin;//initiate skin -jesse
   private List<Player> list;
   public Gameover (){

      skin = new Skin(Gdx.files.internal("skins/shade/skin/uiskin.json"));//-load skin - jesse
      Label label = new Label ("Game Over", skin, "title");
      label.setPosition(Utilities.MetersToPixels(Utilities.FRUSTUM_WIDTH/2),
              Utilities.MetersToPixels(Utilities.FRUSTUM_HEIGHT/2));
      label.setOrigin(label.getWidth()/2,label.getHeight()/2);
      this.addActor(label);
      json=new Json();
      handle = Gdx.files.local(path);
      players=new Array<Player>();
      if(!handle.exists()){
         handle.writeString("[]",false);
      }

      label.setAlignment(Align.center);
      readScoreFromFile();
      list= new List<Player>(skin, "dimmed");
      list.setItems(players);
      list.setAlignment(Align.left);
      ScrollPane scrollPane = new ScrollPane(list,skin);

       Table table= new Table();
       table.setFillParent(true);
       table.setDebug(false,false);

       //Create UI
      table.add(label).width(1000).height(100);
      table.row();
      table.add(scrollPane).fillX().fillY();
      table.row();
      this.addActor(table);
      table.padBottom(500);
   }

   public void addScore(String playerName, long score){
      players.add(new Player(playerName,score));
      players.sort(new Comparator<Player>() {
         @Override
         public int compare(Player o1, Player o2) {
            return (int)(o2.score-o1.score);
         }
      });
      list.setItems(players);
      writeScoreToFile();
   }
   /**
    * Write score data to Array.
    */
   private void writeScoreToFile(){
      String playerString=json.prettyPrint(players);
      handle.writeString(playerString,false);
   }

   /**
    * Read from files and update Array
    */
   private void readScoreFromFile(){
      JsonValue jsonValue=new JsonReader().parse(handle);
      players.clear();
      for (JsonValue j :jsonValue.iterator()){
         players.add(new Player().setName(j.getString("name")).setScore(j.getInt("score")));
      }
   }


   /**
    * Internal data object used to represent player's score.
    */
   public class Player{
      private String name;
      private long score;

      public Player(){
         name="";
         score=0;
      }
      public Player(String name, long score) {
         this.name = name;
         this.score = score;
      }

      public String getName() {
         return name;
      }

      public Player setName(String name) {
         this.name = name;
         return this;
      }
      public long getScore() {
         return score;
      }

      public Player setScore(int score) {
         this.score = score;
         return this;
      }
      @Override
      public String toString(){
         return String.format( "%-22s%-25s",name,Long.toString(score));
      }
   }
}
