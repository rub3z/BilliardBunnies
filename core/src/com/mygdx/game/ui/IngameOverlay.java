package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.utilities.Utilities;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

public class IngameOverlay extends Stage {

   private int playerCount;
   private Table table;
   private Skin skin;
   private Array<Label> labels;
   public IngameOverlay(int playerCount){
      this.playerCount=playerCount;
      labels=new Array<Label>();
      //Load skin in
      skin = new Skin(Gdx.files.internal("Skins/lml/skin/skin.json"));



      if( playerCount>=1){
          createUI(Utilities.FRUSTUM_WIDTH*0/4+10f, Utilities.FRUSTUM_HEIGHT-4,true);
      }
      if(playerCount>=2){
          createUI(Utilities.FRUSTUM_WIDTH*1/4+10f,Utilities.FRUSTUM_HEIGHT-4,false);
      }
      if(playerCount>=3){
          createUI(Utilities.FRUSTUM_WIDTH*2/4+10f,Utilities.FRUSTUM_HEIGHT-4,true);
      }
      if(playerCount>=4){
          createUI(Utilities.FRUSTUM_WIDTH*3/4+10f,Utilities.FRUSTUM_HEIGHT-4,true);
      }
   }

   private void createUI(float posX, float posY, boolean leftAlign){
      Label label =new Label("LOL U HAVE NO POINTS NOW",skin);
      label.setFontScale(2f);
      label.setPosition(Utilities.MetersToPixels(posX),Utilities.MetersToPixels(posY));
      label.setColor(label.getColor().r,label.getColor().g,label.getColor().b,1f);
      this.addActor(label);
      labels.add(label);
   }
   public void updateScore(int player, int score){
      if(player >=0 && player<labels.size){
         labels.get(player).setText(Integer.toString(score));
      }
   }
}
