import javax.swing.JFrame;

public class MinefieldTest
{  
  public static void main (String[] args)
  { 
        Minefield mineFrame = new Minefield();
        mineFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mineFrame.setSize(600,500);
        mineFrame.setVisible(true);
       
    
  }
}