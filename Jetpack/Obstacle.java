import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

class Obstacle
{
   private int x;
   private int y;
   private int w;
   private int h;
   JetPack gameReference;
   private BufferedImage sprite;
   private int framesInSprite;
   
   public Obstacle(int x, int y, String url, int numFrames, JetPack g)
   {
      this.x = x;
      this.y = y;
      framesInSprite = numFrames;
      
      sprite = null;
      try
      {
         sprite = ImageIO.read(new File(url));
      }
      catch (IOException e)
      {
         System.out.println("Oops! IMAGE failed to load (" + url + "):\t\t" + e);
      }
      
      w = sprite.getWidth() / numFrames;
      h = sprite.getHeight();
      gameReference = g;
   }
   
   public Obstacle(int x, int y, String url, JetPack g)
   {
      this(x, y, url, 12, g);
   }
   
   public void draw(Graphics2D g2)
   {
      int sprIndx = (int)(gameReference.spriteFrames()%framesInSprite);
      int sprWidth = sprite.getWidth()/framesInSprite;
      int sprHeight = sprite.getHeight();
      g2.drawImage(sprite, x, y, x+sprWidth, y+sprHeight, sprIndx*sprWidth, 0, (sprIndx+1)*sprWidth, sprHeight, null);
   }
   
   public int getX()
   {
      return x;
   }
   
   public int getY()
   {
      return y;
   }
   
   public int getW()
   {
      return w;
   }
   
   public int getH()
   {
      return h;
   }
}