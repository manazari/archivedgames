/**********************************************************
   NAME:   Matthew Nazari
   DATE:   December 2017
   CLASS:  Comp Sci / WHAP
   PERIOD: 1 / 7
   ASGMT:  Arrays-A5a / WHAP Extra Credit
   
   Day #, hr: what was acheived
   -------------------------------------
   Day 1, 3h: research
   Day 2, 4h: sorta working game loop
   Day 3, 3h: game loop and controls
   Day 4, 3h: control shape, jumping, do images
   Day 5, 4h: font, bckgrnd, sound, pause, scaling
   Day 6, 2h: inertia/acceleration, jumpbar
   
   Citation
   --------------------------------------
   Font loader:
   stackoverflow.com/questions/15953489/how-to-increase-font-size
***********************************************************/
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.lang.Math;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

/**
   ok so TheGame holds the game loop in its main method and
   has the important variables like framerate, size of window,
   and also what keys are being held
*/
public class Game
{   
   public static boolean running = true;                 // for the game loop
   public static final double SCALE = 4.0;               // goal is for this to adapt to any monitor
   public static final int WIDTH = (int) (480 * SCALE);  // this is base size, 16:9 ratio
   public static final int HEIGHT = (int) (270 * SCALE);
   public static boolean paused = false;
   public static boolean[] keysHeld = {false, false, false}; // left, right, crouch
   public static int[] controlKeys = {65, 68, 16, 87, 32};   // change control keys here
   //                                  A   D  Shft W  Space

   // these are just random resources im just testing to see how to use (images, fonts, sounds)
   public static Player player;
   public static BufferedImage img = null;
   public static BufferedImage crossweapon = null;
   public static BufferedImage jumpbar = null;
   public static BufferedImage background = null;
   public static Font myfont;
   public static Clip jumpSound;
   public static Clip backgroundMusic;
   
   public void go()
   {
      // font loader (I did borrow this from stackoverflow.com/questions/15953489/how-to-increase-font-size)
      try
      {
         InputStream is = new BufferedInputStream(new FileInputStream("resources/font.ttf"));
         myfont = Font.createFont(Font.TRUETYPE_FONT, is);
         myfont = myfont.deriveFont(20f);
      } catch(IOException | FontFormatException e) {e.printStackTrace();}
      
      // sound loader
      try
      {
         jumpSound = AudioSystem.getClip();
         jumpSound.open(AudioSystem.getAudioInputStream(new File("resources/jump.wav")));
        
         backgroundMusic = AudioSystem.getClip();
         backgroundMusic.open(AudioSystem.getAudioInputStream(new File("resources/music.wav")));
         backgroundMusic.start();
      } catch (Exception e) {e.printStackTrace();}
      
      
      // image loader
      try
      {
         // img = ImageIO.read(new File("resources/LUTHER.png"));
         // crossweapon = ImageIO.read(new File("resources/crossweapon.png"));
         jumpbar = ImageIO.read(new File("resources/jumpbar.png"));
         background = ImageIO.read(new File("resources/background.png"));
      } catch (IOException e) {}
   
      player = new Player(this, 200, 200);
            
      JFrame window = new JFrame(""); // creates a window
      window.setSize(WIDTH, HEIGHT); // sets window dimensions
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes code end-able
      window.setLocationRelativeTo(null); // start in middle of screen
      window.setResizable(false); // so it can't be resized
      window.setVisible(true); // so you can see it
      Panel panel = new Panel(this); // makes the JPanel
      panel.setFocusable(true); // makes sure keys are being picked up
      panel.requestFocusInWindow(); // same here
      window.add(panel); // adds pic (JPanel) to window (JFrame)               
      
      
      /**
         game loop         
         the game loop first sets time2 to time now in seconds. passedTime
         now equals time between now (time2) and then (time1) and this adds
         to unprocessedTime (the total time). If this hits time for 1 frame
         (1/60sec) update game, render game, and reset unprocessed time
      */
      double time1 = System.nanoTime()/1E9; // System.nanoTime()/1E9 will go up by 1 every sec
      double time2;                          
      double timePassed = 0;
      double unprocessedTime = 0;
      double frameCount = 0;
            
      while(running)
      {
         time2 = System.nanoTime()/1E9; 
         timePassed = time2 - time1;
         time1 = time2;
         unprocessedTime += timePassed;
                             
         if (unprocessedTime >= 1/60.0)
         {
            update();
            window.repaint();
            
            frameCount++;
            /*if (!paused)
            {
               if (frameCount == 15) player.spriteIndex[0]++;
               if (frameCount == 30) player.spriteIndex[0]--;
               if (frameCount == 45) player.spriteIndex[0]++;
               if (frameCount == 60) player.spriteIndex[0]--;
               // just in case if this becomes -1 or 2
               if (player.spriteIndex[0] != 0 && player.spriteIndex[0] != 1) player.spriteIndex[0] = 0;
            }*/
            if (frameCount >= 60) frameCount = 0;
                        
            unprocessedTime = 0;
         }   
      }
   }
   
   public void update()
   {
      player.update();
   }
   
   public static void main(String args[])
   {
      Game game = new Game();
      game.go();
   }
}







class Panel extends JPanel 
{
   Game game;
   
   public Panel(Game g)
   {
      game = g;
      
      /**
         KEY LISTENERS
         these run when a key is pressed/released and receives which key
         it is the code integer holds its ASCII code value. if it matches
         to a controlKey's (a, s, d, w) ASCII code, then make it true/false 
      */
      addKeyListener(new KeyListener() { // makes it hear key stuffs
         public void keyPressed(KeyEvent e)
         {                        
            int code = e.getKeyCode();
            for (int i = 0; i < game.keysHeld.length; i++)
            {
               if (code == game.controlKeys[i]) game.keysHeld[i] = true;
            }
            if (code == 80) game.paused = !game.paused; // make-shift pause
            if (code == game.controlKeys[3]) game.player.jump();
            if (code == game.controlKeys[4]) game.player.crossaxe.shoot();
            
            if (!game.paused)
            {
               if (code == game.controlKeys[0]) game.player.facingRight = false;
               if (code == game.controlKeys[1]) game.player.facingRight = true;
            }

         }
         
         public void keyReleased(KeyEvent e)
         {   
            int code = e.getKeyCode();
            for (int i = 0; i < game.keysHeld.length; i++)
            {
               if (code == game.controlKeys[i]) game.keysHeld[i] = false;
            }
            if (code == game.controlKeys[2]) game.player.jumpLevel = 0;
         }
         
         public void keyTyped(KeyEvent e) {} // we dont use dis
      }); 

   }
   
   public void paintComponent(Graphics g)
   {
      setFocusable(true);
      requestFocusInWindow();
      Graphics2D g2 = (Graphics2D) g; 
        
      g2.setFont(game.myfont);
      g2.drawString("HEY YO", (int) (game.player.x - 5), (int) (game.player.y - 10));
      
      g2.drawImage(game.background,
                  0, 0, game.WIDTH, game.HEIGHT,
                  0, 0, game.background.getWidth(), game.background.getHeight(), null);
               
      game.player.draw(g2);
                        
      g2.setFont(game.myfont.deriveFont((float) (12*game.SCALE)));
      g2.drawString("Reformation Retaliation", (int) (100*game.SCALE), (int) (25*game.SCALE));
      
      if (game.paused)
      {
         g2.setColor(new Color(50, 50, 50, 150));
         g2.fill(new Rectangle (0, 0, game.WIDTH, game.HEIGHT));
         g2.setColor(new Color(30, 30, 30));
         g2.setFont(game.myfont.deriveFont((float) (60*game.SCALE)));
         g2.drawString("Paused", (int) (40*game.SCALE), (int) (180*game.SCALE));
      }

   }
}






class Player
{
   public Game game;
   public Sprite sprite;
   public double x, y;
   public int w, h;
   public boolean facingRight;
   
   public double speed;
   public double xVel;
   public double xAccelerant;
   public double yVel;
   public double jumpVel;
   public boolean inAir; 
   public double jumpLevel;
   public Sprite jumpbarSprite;
   
   public Crossaxe crossaxe;
   
   
   public Player(Game game, double x, double y)
   {
      this.game = game;
      sprite = new Sprite(game, "resources/LUTHER.png", 4, 40, 48);
      this.x = x;
      this.y = y;
      w = (int) (sprite.getWidth() * game.SCALE);
      h = (int) (sprite.getHeight() * game.SCALE);
      facingRight = true;
      speed = 2.5 * game.SCALE;
      xVel = 0;
      yVel = 0;
      jumpVel = 4.5 * game.SCALE;
      inAir = false;
      jumpLevel = 0;
      jumpbarSprite = new Sprite(game, "resources/jumpbar.png", 8, 7, 34);
      crossaxe = new Crossaxe(game);
   }
   
   public void jump()
   {
      if (!inAir && !game.paused)
      {
         game.jumpSound.start();
         game.jumpSound.setFramePosition(0); // makes it so sound clip can be .start()'ed again
         System.out.println("Jump");
         inAir = true;
         yVel = jumpVel;
         if (game.keysHeld[2])
         {
            yVel *= 1 + .4*jumpLevel;
            jumpLevel = 0;
         }
      }
   }
   
   public void update()
   {
      sprite.setFacingRight(facingRight);
      sprite.update();
      
      if (!game.paused)
      {
         // gravity
         if (inAir) yVel -= .25 * game.SCALE; // the first number there is gravity
         
         
         // x position calculating
         double accelerant = .35 * game.SCALE;
         double deccelerant = .045*game.SCALE;
         
         if (game.keysHeld[0]) xVel -= accelerant;
         else if (game.keysHeld[1]) xVel += accelerant;
         
         if (!inAir) xVel /= 1 + deccelerant; //this decelerates and acts as inertia
         else xVel /= 1 + deccelerant/5; //if in air, you decelerate slower (no friction)
         
         if (xVel < .3 && xVel > -.3) xVel = 0; //if its too small just make it 0
         else if (xVel > speed) xVel = speed; //dont make speed going right too fast
         else if (xVel < -speed) xVel = -speed; //dont make speed going left too fast
         
         
         // x moving, y moving, jumping
         if (game.keysHeld[2] && !inAir) // if on ground and crouching
         {
            jumpLevel += 1/120.0; // 2 seconds until full jump
            jumpbarSprite.update();
            if (jumpLevel > 1) // reset jumpLevel if meter full
            {
               jumpLevel = 0; 
               System.out.println("Jumpbar reseted");
            }
            x += xVel/2; // move at half speed
         }
         else
         {
            x += xVel; // if not on ground and not crouching move normally
            jumpbarSprite.reset();
         }
         y -= yVel;
         
         
         // border restricting
         if (x + w > game.WIDTH) x = game.WIDTH - w;
         if (x < 0)   x = 0;
         if (y + h > game.HEIGHT - 50 * game.SCALE)  y = game.HEIGHT - 50 * game.SCALE - h;
         if (y < 0)   y = 0;
         
         if (y + h < game.HEIGHT - 50 * game.SCALE && !inAir)
         {
            inAir = true;
            yVel = 0;
         }

         if (y + h >= game.HEIGHT - 50 * game.SCALE)
         {
            inAir = false;
            yVel = 0;
         }
         
         
         // sprite updating
         if (game.keysHeld[0] || game.keysHeld[1]) sprite.setPhase(1);
         else if (xVel != 0) sprite.setPhase(6);
         else if (xVel == 0) sprite.setPhase(0);
         if (game.keysHeld[2] && !game.keysHeld[0] && !game.keysHeld[1]) sprite.setPhase(2);
         if (game.keysHeld[2] && (game.keysHeld[0] || game.keysHeld[1])) sprite.setPhase(3);
         if (inAir && yVel > 0) sprite.setPhase(4);
         if (inAir && yVel < 0) sprite.setPhase(5);
         
         crossaxe.update();
      }
   }
   
   public void draw(Graphics2D g2)
   {
      Color theColorBefore = g2.getColor();
            
      sprite.draw((int)x, (int)y, w, h, g2);
           
      if (game.keysHeld[2] && !inAir)
      {
           jumpbarSprite.draw((int)(x-10*game.SCALE), (int)(y+8*game.SCALE), g2);
      }
      crossaxe.draw(g2);
      
      g2.setColor(theColorBefore);
   }
}

class Crossaxe
{
   public Game game;
   public Sprite sprite;
   public boolean visible;
   public double x, y;
   public int w, h;
   public boolean facingRight;
   public boolean inAir;
   public double speed;
   public double yVel;
   public double jumpVel;

   public Crossaxe(Game g)
   {
      game = g;
      sprite = new Sprite(game, "resources/crossweapon.png", 16, 22, 22);
      visible = false;
      x = 0;
      y = 0;
      w = (int) (sprite.getWidth() * game.SCALE);
      h = (int) (sprite.getHeight() * game.SCALE);
      facingRight = true;
      inAir = false;
      speed = 5.5 * game.SCALE;
      yVel = 0;
      jumpVel = 6 * game.SCALE;
   }

   public void shoot()
   {
      if (!game.paused && !inAir)
      {
         visible = true;
         inAir = true;
         sprite.setAnimated(true);
         facingRight = game.player.facingRight;
         x = game.player.x;
         y = game.player.y;
         yVel = 12;
         System.out.println("Crossaxe thrown");
         if (facingRight)
         {
            game.player.xVel -= .8*game.SCALE;
            x += game.player.w - 10*game.SCALE;
         }
         else game.player.xVel += .8*game.SCALE;
      }
   }
   
   public void update()
   {
      sprite.update();
      if (inAir && visible)
      {
         if (facingRight) x += speed;
         else x -= speed;
         yVel -= .2 * game.SCALE;
         y -= yVel;
         if (y > game.HEIGHT - 70*game.SCALE)
         {
            inAir = false;
            sprite.setAnimated(false);
         }
      }
   }
   
   public void draw(Graphics2D g2)
   {
      if (visible)
      {
         sprite.draw((int)x, (int)y, w, h, g2);
      }
   }
}

class Sprite
{
   private Game game;
   private BufferedImage img = null;
   private int[] spriteGrid;
   private int fps, w, h, xDem, yDem, frameCount;
   private boolean facingRight;
   private boolean animated;
   
   public Sprite(Game g, String source, int theFps, int width, int height)
   {
      game = g;
      try
      {
         img = ImageIO.read(new File(source));
      }
      catch (IOException e)
      {
         System.out.print("Image at '" + source + "' doesn't exist :(");
      }
      spriteGrid = new int[] {0, 0};
      fps = theFps;
      w = width;
      h = height;
      xDem = img.getWidth()/w;
      yDem = img.getHeight()/h;
      frameCount = 0;
      facingRight = true;
      animated = true;
   }
   
   public void update()
   {
      if (animated)
      {
         frameCount ++;
         for (int i = 0; i < xDem; i++)
         {
            if (frameCount >= i*60/fps) spriteGrid[0] = i;            
         }
         if (frameCount >= xDem * 60/fps) frameCount = 0;
      }
   }
   
   public void draw(int x, int y, int w, int h, Graphics2D g2)
   {
      if (facingRight)
      {
         g2.drawImage(img, x, y, x+w, y+h,
                      spriteGrid[0]*this.w, spriteGrid[1]*this.h,
                      spriteGrid[0]*this.w+this.w, spriteGrid[1]*this.h+this.h, null);
      }
      else
      {
         g2.drawImage(img, x, y, x+w, y+h,
                      spriteGrid[0]*this.w+this.w, spriteGrid[1]*this.h,
                      spriteGrid[0]*this.w, spriteGrid[1]*this.h+this.h, null);
      }
   }
   
   public void draw(int x, int y, Graphics2D g2)
   {
      draw(x, y, (int)(w*game.SCALE), (int)(h*game.SCALE), g2);
   }
   
   public void reset()
   {
      frameCount = 0;
   }
   
   public int getWidth() { return w; }
   
   public int getHeight() { return h; }
   
   public int getFps() { return fps; } 
   
   public void setFps(int f) { fps = f; }
   
   public void setPhase(int p) { spriteGrid[1] = p; }
   
   public void setAnimated(boolean a) { animated = a; }
   
   public void setFacingRight(boolean b) { facingRight = b; }
   
   
}