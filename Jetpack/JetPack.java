import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class JetPack
{
   private final int WIDTH = 400;
   private final int HEIGHT = 200;
   private int scale = 6;
   private JFrame window;
   private int totalFrames = 0;
   private int spriteFrames = 0;
   private int translation = 0;
   private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
   private Player player;
   
   public void run()
   {
      for (int i = 300; i < 1000; i += 250)
      {//(int)(Math.random()*160 + 20)   (int)(Math.random()*i)
         int ranX = (int)(Math.random()*200+i);
         int ranY = (int)(Math.random()*140+20);
         obstacles.add(new Obstacle(i, ranY, "spinner.png", this));
         
         ranX = (int)(Math.random()*200+i);
         ranY = (int)(Math.random()*140+20);
         obstacles.add(new Obstacle(i + 60, ranY, "zapper.png", this));

         obstacles.add(new Obstacle((int)(Math.random()*200+i), (int)(Math.random()*180+10), "laser.png", this));
         //obstacles.add(new Obstacle((int)(Math.random()*200+i), (int)(Math.random()*180+10), "rocket.png", 5, this));
      }
      player = new Player(100, 100, this);
      
      // window creating
      window = new JFrame("Jet Pack");
      window.setSize(WIDTH*scale + 10, HEIGHT*scale + 50);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setLocationRelativeTo(null);
      window.setResizable(false);
      
      Panel panel = new Panel(this);
      panel.setFocusable(true);
      panel.requestFocusInWindow();
      window.add(panel);   
      window.setVisible(true);
      
      playSound("letsgroove.wav");
      
      double time1 = System.nanoTime()/1E9; // System.nanoTime()/1E9 will go up by 1 every sec
      double time2;                          
      double timePassed = 0;
      double unprocessedTime = 0;
      
      boolean running = true;
      while (running)
      {
         time2 = System.nanoTime()/1E9; 
         timePassed = time2 - time1;
         time1 = time2;
         unprocessedTime += timePassed;
                             
         if (unprocessedTime >= 1/20.0)
         {
            update();
            window.repaint();
            
            // game will run at 20fps, but sprites will be at 10 fps
            totalFrames++;
            if (totalFrames%2 == 0) spriteFrames++; 
                                 
            unprocessedTime = 0;
         }   
      }

   }
   
   public void playSound(String url)
   {
      Clip sound = null;
      try
      {
         sound = AudioSystem.getClip();
         sound.open(AudioSystem.getAudioInputStream(new File( url)));
         sound.start();
      }
      catch (Exception e)
      {
         System.out.println("Oops! SOUND failed to load (" + url + "):\t]t" + e);
      }
   }
   
   public void update()         
   {
      translation = (int)(Math.pow(totalFrames, 1.29));
      /*
      for (int i = 0; i < obstacles.size(); i++)
      {
         if (obstacles.get(i).getX() < translation-300)
         {
            obstacles.remove(i);
         }
      }/*
      for (int i = 300; i < 2000; i += 250)
      {//(int)(Math.random()*160 + 20)   (int)(Math.random()*i)
         int ranX = (int)(Math.random()*200+i);
         int ranY = (int)(Math.random()*140+20);
         obstacles.add(new Obstacle(ranX, ranY, "spinner.png", this));
         
         ranX = (int)(Math.random()*200+i);
         ranY = (int)(Math.random()*140+20);
         obstacles.add(new Obstacle(ranX, ranY, "zapper.png", this));
         /*obstacles.add(new Obstacle((int)(Math.random()*i), (int)(Math.random()*50+50), "zapper.png", this));
         obstacles.add(new Obstacle((int)(Math.random()*i), (int)(Math.random()*50+100), "rocket.png", 5, this));
         obstacles.add(new Obstacle((int)(Math.random()*i), (int)(Math.random()*50+150), "laser.png", this));
      }*/

      player.update();
      
      /*do
      {
         if (player.getX() + translation + 10 > o.width
      }
      while (true); */
      
   }
   
   public int totalFrames()
   {
      return totalFrames;
   }

   public int spriteFrames()
   {
      return spriteFrames;
   }
   
   public int scale()
   {
      return scale;
   }
   
   public int translation()
   {
      return translation;
   }
   
   public ArrayList<Obstacle> obstacles()
   {
      return obstacles;
   }
   
   public Player player()
   {
      return player;
   }
   
   public static void main(String[] args)
   {
      new JetPack().run();
   }
}