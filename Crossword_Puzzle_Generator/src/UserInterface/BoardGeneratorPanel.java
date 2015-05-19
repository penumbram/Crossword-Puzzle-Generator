package UserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;

import Generator.Board;
import Generator.Dictionary;
import Observer.BoardObserver;
import Utility.Singleton;
import Utility.Utils;

public class BoardGeneratorPanel extends FadingPanel implements BoardObserver {
	SpringLayout springLayout;
	private BoardPanel bp;
	BufferedImage loadingImage = Utils.getImage("resources/images/loading.png", 50, 50);
	String[][] cells;
	Button buttonGenerate;
	Button buttonBack;
	Board board = null;
	boolean regenerate = false;
	Thread thread;
	
	Timeline timeline;
	int rotation = 0;
	boolean loading = false;
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		if (loading)
			g.drawImage(getFilter(loadingImage).filter(loadingImage, null), 865, 440, null);
	}

	private AffineTransformOp getFilter(BufferedImage image)
	{
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(this.rotation), locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}
	
	public BoardGeneratorPanel(String[][] cells)
	{
		this.cells = cells;
		setSize(920, 500);
		setOpaque(false);
		setBounds(20, 20, 920, 500);
		springLayout = new SpringLayout();
		setLayout(springLayout);
		int i = ((int)(400 / this.cells.length));
		bp = new BoardPanel(400,400,cells.length,cells.length,i,i,0,0);
		
		bp.setBoard(cells);
		bp.setClickable(false);
		add(bp);
		bp.setVisible(true);
		setVisible(true);
		
		springLayout.putConstraint(SpringLayout.EAST, bp, 710, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, bp, 95, SpringLayout.NORTH, this);
		
		buttonGenerate = new Button(Utils.getImage("resources/images/buttons.png",161,90),Utils.getImage("resources/images/buttons_glow.png",161,90));
		buttonGenerate.setText("Generate");
		add(buttonGenerate);
		buttonGenerate.setPreferredSize(new Dimension(100, 30));
		buttonGenerate.setVisible(true);
		buttonGenerate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.logg("Gonna generate board here");
				setGenerationParameters();
				generate();
				buttonGenerate.setVisible(false);
			}
		});
		
		buttonBack = new Button(Utils.getImage("resources/images/buttons.png",161,90),Utils.getImage("resources/images/buttons_glow.png",161,90));
		buttonBack.setText("Back");
		add(buttonBack);
		buttonBack.setPreferredSize(new Dimension(100, 30));
		buttonBack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.logg("Back");
				goBack();
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, buttonBack, 0, SpringLayout.SOUTH, this);
		timeline = new Timeline(this);
		timeline.addPropertyToInterpolate("rotation", 0, 360);
		timeline.setDuration(1000);
	}
	
	public void setRotation(int rot)
	{
		this.rotation = rot;
		repaint();
	}
	
	private void goBack()
	{
		Singleton.getInstance().boardFrame.setVisible(false);
		Singleton.getInstance().boardFrame.remove(this);
		Singleton.getInstance().boardFrame.add(Singleton.getInstance().boardFrame.boardSelectionPanel);
		Singleton.getInstance().boardFrame.boardSelectionPanel.setVisible(true);
		if (thread != null)
		{
			if (thread.isAlive())
			{
				thread.stop();
			}
		}
	}
	
	private void setGenerationParameters()
	{
		board = new Board(cells.length,cells.length);
		Dictionary dict = new Dictionary();
		dict.fillDictionary();
		board.setCells(cells);
		board.saveCells(cells);
		board.registerObserver(this);
	}
	
	private void generate()
	{
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() 
			{
				loading = true;
				board.searchLongestPaths();
				board.fillLongestWords();
				board.fillRestOfWords();
				fillEmptyCellsWithBlackCells();
				board.fillEmptyCellsWithBlackCells();
				Utils.logg("AFTER");
				board.printBoard(board.cells);
				ArrayList<int[]> validation = board.validate();
				timeline.cancel();
				loading = false;
				if (validation == null)
				{
					regenerate = false;
					JOptionPane.showMessageDialog(null, board.getWordsInBoard());
				}
				else
				{
					String problem = "";
					for (int[] acell : validation)
					{
						problem += board.cells[acell[0]][acell[1]];	
					}
					Utils.logg("PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM");
					Utils.logg(problem);
					board.ruledOutWords.add(problem);
					regenerate = true;
				}
			}
		};
		thread = new Thread(runnable);
		thread.start();
		timeline.playLoop(RepeatBehavior.LOOP);
		if (regenerate)
		{
			board.clearAll();
			generate();
		}
	}
	
	private void fillEmptyCellsWithBlackCells()
	{
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				if (cells[i][j] == " ")
				{
					bp.tiles[i][j].setBackground(Color.BLACK);
					bp.tiles[i][j].invalidate();
					bp.tiles[i][j].repaint();
					Utils.logg("here1");
				}
			}
		}
	}
	
	@Override
	public void update(ArrayList<int[]> list, String word) {
		Utils.logg(word);
		for (int i = 0; i < list.size(); i++)
		{
			Utils.logg("[" + list.get(i)[0] + "][" + list.get(i)[1] + "]");
			bp.tiles[list.get(i)[0]][list.get(i)[1]].label.setText(String.valueOf(word.charAt(i)));
		}
		
	}
}
