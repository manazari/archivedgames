import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

class Player
{
   private int x;
   private int y;
   private double yVel;
   JetPack gameReference;
   private BufferedImage bodySprite;
   private BufferedImage jetSprite;
   private BufferedImage flameSprite;
   private boolean jetIsShining;
   private boolean jetIsOn;
   private boolean dead;

   public Player(int x, int y, JetPack g)
   {
      this.x = x;
      this.y = y;
      yVel = 0;
      boolean isJetShining = false;
      jetIsOn = false;
      
      bodySprite = null;
      jetSprite = null;
      flameSprite = null;
      try
      {
         bodySprite = ImageIO.read(new File("shru.png"));
         jetSprite = ImageIO.read(new File("jet.png"));
         flameSprite = ImageIO.read(new File("flame.png"));
      }
      catch (IOException e)
      {
         System.out.println("Player image failed to load:\t\t" + e);
      }
      
      gameReference = g;
   }
   
   public void draw(Graphics2D g2)
   {
      long spriteFrames = gameReference.spriteFrames();
      
      // DRAWING JETPACK
      if (spriteFrames%6 == 0 && Math.random() > .9)
      {
         jetIsShining = true;
         System.out.println("The jetpack shall now shimmer!");
      }
      
      if (jetIsShining)
      {
         int sprIndx = (int)((spriteFrames)%6);
         int sprWidth = jetSprite.getWidth()/6;
         int sprHeight = jetSprite.getHeight()/2;
         g2.drawImage(jetSprite, x, y, x+9, y+14, sprIndx*9, 0, (sprIndx+1)*9, 14, null);
         
         if (spriteFrames%6 == 5)
         {
            jetIsShining = false;
         }
      }
      else
      {
         int sprWidth = jetSprite.getWidth()/6;
         int sprHeight = jetSprite.getHeight()/2;

         // single frame on second row of the jet sprite
         g2.drawImage(jetSprite, x, y, x+9, y+14, 0, 14, 9, 14*2, null);
      }
      
      // DRAWING THE FLAME
      if (true)
      {
         int sprIndx = (int)((spriteFrames)%4);
         int sprWidth = flameSprite.getWidth()/4;
         int sprHeight = flameSprite.getHeight()/2;
         g2.drawImage(flameSprite, x, y+12, x+sprWidth, y+12+sprHeight, sprIndx*sprWidth, 0, (sprIndx+1)*sprWidth, sprHeight, null);
      }
      
      // DRAWING THE BODY
      int sprIndx = (int)((spriteFrames)%4);
      int sprWidth = bodySprite.getWidth()/4;
      int sprHeight = bodySprite.getHeight();
      g2.drawImage(bodySprite, x+7, y-2, x+7+sprWidth, y+sprHeight-2, sprIndx*sprWidth, 0, (sprIndx+1)*sprWidth, sprHeight, null);

      /*
      int sprIndx = (int)(gameReference.spriteFrames()%12);
      int sprWidth = sprite.getWidth()/12;
      int sprHeight = sprite.getHeight();
      g2.drawImage(sprite, x, y, x+sprWidth, y+sprHeight, sprIndx*sprWidth, 0, (sprIndx+1)*sprWidth, sprHeight, null);*/
   }
   
   public boolean jetIsOn()
   {
      return jetIsOn;
   }
   
   public void setJetIsOn(boolean bool)
   {
      jetIsOn = bool;
   }
   
   public void update()
   {
      if (jetIsOn)
      {
         if (yVel < 0) yVel /= 1.2;
         yVel += .75;
      }
      else
      {
         if (yVel > 0) yVel /= 1.2;
         yVel -= .75;
      }
      y -= yVel;
      
      if (y < 0)
      {
         y = 0;
         yVel = 0;
      }
      else if (y > 180)
      {
         y = 180;
         yVel = 0;
      }

   }
   
   public int getX()
   {
      return x;
   }
   
   public int getY()
   {
      return y;
   }

}