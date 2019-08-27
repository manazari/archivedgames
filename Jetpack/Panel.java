import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

class Panel extends JComponent 
{
   JetPack gameReference;
   
   public Panel(JetPack g)
   {
      gameReference = g;
            
      addKeyListener(new KeyListener() {
         public void keyPressed(KeyEvent e)
         {
            int code = e.getKeyCode();
            
            if (code == 32)
            {
               gameReference.player().setJetIsOn(true);
            }
         }
         
         public void keyReleased(KeyEvent e)
         {   
            int code = e.getKeyCode();
            
            if (code == 32)
            {
               gameReference.player().setJetIsOn(false);
            }
         }
         
         public void keyTyped(KeyEvent e) {}
      });

   }

   public void paintComponent(Graphics g)
   {
      int translation = gameReference.translation();
      BufferedImage background = null;
      BufferedImage scorePlate = null;
      try
      {
         background = ImageIO.read(new File("back.png"));
         scorePlate = ImageIO.read(new File("score.png"));
      }
      catch (IOException e)
      {
         System.out.println("Background failed to load:\t\t" + e);
      }      
      Graphics2D g2 = (Graphics2D) g;
      
      g2.scale(gameReference.scale(), gameReference.scale());      
      g2.translate(-translation*.75, 0);
      g2.drawImage(background, (translation*3/4)/400*400, 0, null);
      g2.drawImage(background, ((translation*3/4)/400+1)*400, 0, null);
      g2.translate(-translation*.25, 0);
      
      
      for (Obstacle o : gameReference.obstacles())
      {
         o.draw(g2);
      }

      g2.translate(translation, 0);
      g2.drawImage(scorePlate, 0, 0, null);
      gameReference.player().draw(g2);

      Font font = null;
      try
      {
         InputStream is = new BufferedInputStream(new FileInputStream("font.ttf"));
         font = Font.createFont(Font.TRUETYPE_FONT, is);
      }
      catch(IOException | FontFormatException e)
      {
         System.out.println("Font failed to load:\t\t" + e);      
      }

      g2.setColor(new Color(220, 210, 200));
      g2.setFont(font.deriveFont(20f));
      g2.drawString(""+translation/100, 20, 39);       
   }
}
