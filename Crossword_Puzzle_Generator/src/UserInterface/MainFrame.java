package UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;


import Utility.Utils;

public class MainFrame extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer timer = new Timer();
	public BoardFrame boardFrame;
	
	    public MainFrame()  
	    {
	        initUI();
	        boardFrame = new BoardFrame();
	    }

	    private void initUI()  {
	        setLayout(null);
	        setTitle("Simple example");
	        setSize(960, 540);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        //setExtendedState(JFrame.MAXIMIZED_BOTH);
	        setUndecorated(true);
	        BufferedImage backgroundImage = Utils.getImage("resources/images/bg2.png", 960,540);
	        this.setContentPane(new ImagePanel(backgroundImage));
	        final Button button = new Button(Utils.getImage("resources/images/defter.png",195,70),Utils.getImage("resources/images/defter_glow.png",195,70));
	        add(button);
	        button.setBounds(65, 420, 195, 70);	  
	        button.addActionListener(new ActionListener()
	        {


			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stuff
				System.out.println("button clicked");
				
				timer.cancel(); 
			    timer = new Timer();

			    TimerTask action = new TimerTask() {
			        public void run() {
			        	Utils.logg("defter");
			            boardFrame.setVisible(true);
			            boardFrame.boardSelectionPanel.setVisibility(true);
			        }

			    };

			    timer.schedule(action, 1000); 
			}
	        });
	       
	        
	        
	        
	        final Button editButton = new Button(Utils.getImage("resources/images/edit_button.png",235,217),Utils.getImage("resources/images/edit_button_glow.png",235,217));
	        add(editButton);
	        editButton.setBounds(49, 95, 235, 217);	
	        editButton.addActionListener(new ActionListener()
	        {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stuff
				System.out.println("button clicked");
				
				timer.cancel(); //this will cancel the current task. if there is no active task, nothing happens
			    timer = new Timer();

			    TimerTask action = new TimerTask() {
			        public void run() {
			        	boardFrame.remove(boardFrame.boardSelectionPanel);
			        	boardFrame.add(boardFrame.boardEditorPanel);
			            boardFrame.setVisible(true);
			            boardFrame.boardEditorPanel.editNewBoard();
			            boardFrame.boardEditorPanel.setVisible(true);
			            System.out.println("timertask worked");
			        }

			    };

			    timer.schedule(action, 1000); //this starts the task
			}
	        });     
	    }
	    
	    
	    

}
