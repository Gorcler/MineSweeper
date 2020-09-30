
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.util.Random;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

public class Minefield extends JFrame
{
	//initialize variables
	int ROWS = 7;
	int COLS = 7;
	int BOMBS = 0;
	int cellsRevealed = 0;
	
	
	
	
	private MSCell field[][];
	private JButton[][] grid = new JButton[ROWS+2][COLS+2];
	JPanel gridPanel;
	JPanel gridPanel2;
	JPanel gridPanel3;
	JLabel tempLabel;
	private final GridBagLayout layout;
	private final GridBagConstraints gbc;
	
	public Minefield()
	{
		
				
		super("Minesweeper");
		MouseHandler h = new MouseHandler();
		gridPanel = new JPanel();
		gridPanel2 = new JPanel();
		gridPanel3 = new JPanel();
		
		String b = JOptionPane.showInputDialog(gridPanel,"Enter level between 1-3");
		System.out.println(b);
		int c = Integer.valueOf(b);
		if(c == 1)
		{
			System.out.println(b);
			BOMBS = BOMBS + 3;
		}
		else if(c == 2)
		{
			BOMBS = BOMBS + 5;
		}
		else if(c == 3)
		{
			BOMBS = BOMBS + 10;
		}
		else
		{
			JOptionPane.showMessageDialog(gridPanel,"Error");
			System.exit(0);
		}
		
		
		
		layout = new GridBagLayout();
		gridPanel.setLayout(layout);
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		
		int row, col;
		for (row=0; row<ROWS+2; row++)
			for (col = 0; col<COLS+2; col++)
			{
				gbc.gridx = row;
				gbc.gridy = col;
				grid[row][col] = new JButton(row +","+col);
				gridPanel.add(grid[row][col],gbc);
				grid[row][col].addMouseListener (h);			
			}
			
		
		

		setLayout(new BorderLayout());

		final JLabel countdown = new JLabel("00:00");
		JLabel Minesweeper = new JLabel("MineSweeper");
		gridPanel3.add(Minesweeper,gbc);

		
		countdown.setFont (countdown.getFont ().deriveFont (32.0f));
		Minesweeper.setFont (Minesweeper.getFont ().deriveFont (32.0f));


		



		final Timer t = new Timer(1000, new ActionListener()
		{
			int time = 0;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				time++;
				countdown.setText((time/60) + ":" + (time%60) );
				
				if(field[1][1].isRevealed() == true)
				{
					final Timer timer =(Timer) e.getSource();
					timer.stop();
				}
			}
		});

		
		gridPanel2.add(countdown,gbc);
		t.start();
		add(gridPanel3,BorderLayout.PAGE_START);
		add(gridPanel2,BorderLayout.SOUTH);
		add(gridPanel,BorderLayout.CENTER);
		
		
		field= new MSCell[ROWS+2][COLS+2];
		initialiseField();
		plantBombs();
		computeNumbers();
		displayField();
	}

	public void initialiseField()
	{
		for (int i=0; i<ROWS+2;i++)
		{
			for (int j=0; j<COLS+2; j++)
			{
				field[i][j]=new MSCell();
			}
		}
	}
	
	public void plantBombs()
	{
		Random randomNumbers = new Random();
		
		int bombsPlanted=0;
		int bombRow = 0;
		int bombCol = 0;
		while (bombsPlanted < BOMBS)
		{
			bombRow = randomNumbers.nextInt(ROWS)+1;
			bombCol = randomNumbers.nextInt(COLS)+1;
			System.out.println( bombRow +" " + bombCol);
			if (!field[bombRow][bombCol].isBomb())
			{
				 field[bombRow][bombCol].setBomb();
				 bombsPlanted++;
			}
			
		}
		
	}

	
	public void computeNumbers()
	{
		int countBombs= 0;
		for (int i=1; i<=ROWS;i++)
		{
			for (int j=1; j<=COLS; j++)
			{	
	            countBombs=0;
				if (!field[i][j].isBomb())
				{
					for (int a=-1; a<=1; a++)
					{
						for (int b=-1; b<=1; b++)
						{
							if (field[i+a][j+b].isBomb() )
							{
								countBombs++;
							}
						}
							
					}	
					field[i][j].setValue(countBombs);
				}
			}
			
		}
	}
	
	
	public void displayField()
	{
		for (int i=0; i<=ROWS+1;i++)
		{
			for (int j=0; j<=COLS+1; j++)
			{  
				grid[i][j].setText(field[i][j].toString());
			}
			repaint();
			revalidate();
		}
	}
	
	public void Zero(int i,int j)
	{
		System.out.println( i +" " + j);
		for (int a=-1; a<=1; a++)
		{
			for (int b=-1; b<=1; b++)
			{
				if ((field[i+a][j+b].getValue() ==0) && (!field[i+a][j+b].isRevealed()))
				{
					field[i+a][j+b].setRevealed();
					if (field[i+a][j+b].getValue() !=-1){
						cellsRevealed++;
					} 
					gridPanel.remove(grid[i+a][j+b]);
					gbc.gridx = i+a;
					gbc.gridy = j+b;
					tempLabel = new JLabel(field[i+a][j+b].toString());
					
					System.out.println(tempLabel.getText());
					gridPanel.add(tempLabel,gbc);
					revalidate();
					repaint();
					
					Zero(i+a,j+b); 
				}
				else
				{
					if (!field[i+a][j+b].isRevealed() && field[i+a][j+b].getValue() != -1)
					{
						field[i+a][j+b].setRevealed();
						gbc.gridx = i+a;
						gbc.gridy = j+b;
						tempLabel = new JLabel(field[i+a][j+b].toString());
						gridPanel.remove(grid[i+a][j+b]);
						gridPanel.add(tempLabel,gbc);
						
						revalidate();
						repaint();
					}
				}
			}
							
		}	
	}

	public int Score()
	{
		int score = 0;
		for (int r=1; r<=ROWS; r++)
		{
			for (int c = 1; c<=COLS; c++)
			{
				if(field[r][c].isFlagged() == true && field[r][c].isBomb() == true)
				{
					score = score +1;
				}
			}
		}
		return score;
	}
	


	
	private class MouseHandler implements MouseListener
	{
		@Override 
		 
		 public void mouseClicked(MouseEvent e) 
		 {
				
			if (e.getButton()== MouseEvent.BUTTON1)
			{
				Object o= e.getSource();
				tempLabel= new JLabel("@");
				for (int r=1; r<=ROWS; r++)
					for (int c = 1; c<=COLS; c++)
					{
						if 	(grid[r][c] == (JButton) o)
						{
							field[r][c].setRevealed();
														
							if (field[r][c].isBomb())
							{
								
								int a = JOptionPane.showConfirmDialog(gridPanel, "You found "+ Score()+" out of "+BOMBS  +" bombs.","GAME OVER",JOptionPane.DEFAULT_OPTION);
								if(a == JOptionPane.OK_OPTION)
								{
									System.exit(0);
								}
								
								
							}
							if (!field[r][c].isBomb())
							{
								if(field[r][c].getValue()==0)
							    {
								    Zero(r,c);
									gbc.gridx = r;
									gbc.gridy = c;
									gridPanel.remove(grid[r][c]);
									gridPanel.add(tempLabel,gbc);
									cellsRevealed++;
									
							    }
								else
								{
					              cellsRevealed++;
								}
							}
							tempLabel.setText("  "+field[r][c].toString());
							gbc.gridx = r;
							gbc.gridy = c;
							gridPanel.remove(grid[r][c]);
							gridPanel.add(tempLabel,gbc);
							if (cellsRevealed==((ROWS*COLS)-BOMBS))
							{
								int a = JOptionPane.showConfirmDialog(gridPanel," You Won","YOU WON",JOptionPane.DEFAULT_OPTION);
								if(a == JOptionPane.OK_OPTION)
								{
									System.exit(0);
								}
								
							}
							revalidate();
							repaint();
						}
					}
			}
			
			if (e.getButton()== MouseEvent.BUTTON3)
			{
				Object o= e.getSource();
				tempLabel= new JLabel("@");
				for (int r=1; r<=ROWS; r++)
					for (int c = 1; c<=COLS; c++)
					{
						if 	(grid[r][c] == (JButton) o)
						{
							gbc.gridx = r;
							gbc.gridy = c;
							if (field[r][c].isFlagged())
							  field[r][c].setFlagged(false);
						    else
								field[r][c].setFlagged(true);
							grid[r][c].setText("  "+field[r][c].toString());
							
							revalidate();
							repaint();
						}
					
					}
			}	 
		 }
		 public void mousePressed(MouseEvent e) {}
		 public void mouseReleased(MouseEvent e) {}
		 public void mouseEntered(MouseEvent e) {}
		 public void mouseExited(MouseEvent e) {}
	}
	
		
		
}

