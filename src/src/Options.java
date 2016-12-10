package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Options extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // just to avoid a yellow

	/**
	 * To avoid rounding errors
	 */
	private static final double EPSILON = 1e-14;

	int xClick;
	int yClick;

	/**
	 * number of x's (and the line) that must be drawn
	 */

	int numberOfXForDisplay = 0;

	boolean inGame;

	/**
	 * PVP if player vs player, PVC if player vs computer, null if not assigned
	 * yet
	 */
	String gameMode;

	/**
	 * Unknown TODO
	 */
	String nameP1;
	/**
	 * Unknown TODO
	 */
	String nameP2;

	/**
	 * [circle 1,2.....n][x,y,radius]
	 * TODO change this to just be a list of circles
	 * Or rework entirely
	 * Unreadable
	 */
	int[][] gameModeCircles;

	/**
	 * Unknown TODO
	 */
	String firstPlayer;

	/**
	 * Easy Medium or Hard
	 */
	String computerDifficulty;

	private Timer timer;
	private final int DELAY;

	public Options() // parameter constructor
	{
		xClick = -1;
		yClick = -1;
		numberOfXForDisplay = 0;
		inGame = true;
		gameMode = null;
		nameP1 = null;
		nameP2 = null;
		gameModeCircles = null;
		// nameBoxes = new ArrayList<int[]>();
		firstPlayer = null;
		computerDifficulty = null;
		timer = null;
		DELAY = 1200;
		
		
		setBackground(Color.white);
		setFocusable(true);

		// both magic convenient numbers for now
		// will be based on final layout
		// minimum font size, item spacing, etc.
		setPreferredSize(new Dimension(1200, 800));

		addMouseListener(new MousePressListener());
		initGame();
	}

	/**
	 * This displays the contents of the panel to the frame.
	 * 
	 * @param g
	 *            is the graphics component to draw the frame
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		doDrawing(g);
		// debugging: outline panel
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	/**
	 * Display the title of the panel with the given graphics component
	 * 
	 * @param g
	 *            the graphics to draw with
	 */
	public void AddTitle(Graphics g)
	{
		// declare font
		Graphics2D g2 = (Graphics2D)g;
		int fontSize = (int)Math.round(getWidth() * 0.15);
		g2.setFont(new Font("Helvetica", Font.BOLD, fontSize));
		g2.setColor(Color.black);
		
		// draws Options in the top left
		String title = "Options";
		int leftMargin = 15; // the left margin, in pixels, for the header
		int beginY = g2.getFontMetrics().getAscent(); // display font at the top
		g2.drawString(title, leftMargin, beginY);
	}

	/**
	 * TODO what does this does exactly?
	 * @param startX
	 * @param startY
	 * @param width
	 * @param height
	 * @param g
	 * @param color
	 */
	public void drawBoxSurroundingWord(int startX, int startY, int width,
			int height, Graphics g, Color color)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(color);

		// TODO why "startY - 4 * height / 5"? I don't like magic!
		g2.drawRect(startX, startY - 4 * height / 5, width, height);
	}

	/**
	 * Draws a hollowed out circle and a message below it.
	 * May surround the message with a rect
	 * @param g the graphics to draw with
	 * @param startX the X coordinate of the center of the circle
	 * @param startY the Y coordinate of the center of the circle
	 * @param circleDiameter the diameter of the circle
	 * @param msg the message to write below the circle
	 * @param color the color of the circle
	 * @param drawRect whether to draw a rectangle around the message
	 */
	public void DrawCircle(Graphics g, int startX, int startY,
			int circleDiameter, String msg, Color color, boolean drawRect)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(color);
		// filled outer circle
		g2.fillArc(startX, startY, circleDiameter, circleDiameter, 0, 360);

		// empty inner circle
		g2.setColor(Color.white);
		double hollowPercent = 0.75; // the percent of this that stays empty: 0-1
		int innerDiameter = (int)(hollowPercent * circleDiameter); // round down
		g2.fillArc(startX + innerDiameter / 6,
				startY + innerDiameter / 6, 
				innerDiameter,
				innerDiameter, 0, 360);// sorry about the 6

		/**
		 * Add Msg if needed
		 * TODO move this to a new method: one for circle, one for message
		 * Much easier to modify, maintain, rework, etc.
		 */
		if (msg != null)
		{
			g2.setColor(Color.BLACK);
			int fontSize = (int)Math.round(getWidth() * .05);
			Font msgFont = new Font("Helvetica", Font.BOLD, fontSize);
			FontMetrics msgFontMetrics = getFontMetrics(msgFont);
			g2.setFont(msgFont);

			g2.drawString(msg,
					startX + circleDiameter / 2
							- msgFontMetrics.stringWidth(msg) / 2,
					startY + 3 * circleDiameter / 2);

			if (drawRect) // draw rectangle around circle
			{
				drawBoxSurroundingWord(
						startX + circleDiameter / 2
								- msgFontMetrics.stringWidth(msg) / 2,
						startY + 3 * circleDiameter / 2,
						msgFontMetrics.stringWidth(msg),
						msgFontMetrics.getHeight(), g, Color.black);
			}
		}
	}

	/**
	 * Add a row of circles
	 * @param g the graphics to draw the circles with
	 * @param startY the top of the circles
	 * @param numOfCircles the number of circles to draw
	 * @param circleDiameter the diameter of the circles to draw
	 * @param msgs the msg to display under the corresponding circle
	 * @param color the color of the circles
	 * @boolean drawRect
	 */
	public void AddOptionCircles(Graphics g, int startY, int numOfCircles,
			int circleDiameter, String[] msgs, Color color, boolean drawRect)
	{
		for(int i = 0; i < numOfCircles; i++)
		{
			DrawCircle(g, getWidth() * (i + 1) / (numOfCircles + 1) - circleDiameter / 2, startY,
					circleDiameter, msgs[i], color, drawRect);
		}
	}

	/**
	 * Implements the timer in order to draw the x's at timed intervals
	 */
	private void initGame()
	{
		timer = new Timer(DELAY, this);
		timer.start();
	}

	/**
	 * Draws all the elements on the startup menu
	 */
	private void doDrawing(Graphics g)
	{
		// Graphics2D g2 = (Graphics2D) g;
		AddTitle(g);
		AddGameModeOptions(g);

		// once game mode has been selected
		if (gameMode != null)
		{
			AddNamesStartOptions(g);

			AddFillInCircleGameMode(g);
		}

		// once firstPlayer has been named
		if (firstPlayer != null)
		{
			if (gameMode.equals("PVC"))
			{
				AddDifficultyCircles(g);
				// once computer difficulty has been selected
				if (computerDifficulty != null)
				{
					AddPlayButton(g);
				}
			}
			else
				AddPlayButton(g);
		}
	}

	public void AddDifficultyCircles(Graphics g)
	{
		// Graphics2D g2 = (Graphics2D) g;
		int startOfFirstOptionsY = (int)(getHeight() * .65);
		int numOfCircles = 3;
		int circleDiameter = (int)(.15 * getHeight());

		String[] msgs =
		{ "Easy", "Medium", "Hard" };

		AddOptionCircles(g, startOfFirstOptionsY, numOfCircles, circleDiameter,
				msgs, Color.red, false);

		// nameBoxes = new ArrayList<int[]>();

	}

	public void AddFillInCircleStartingPlayer(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(Color.black);
		if (firstPlayer.equals("p1"))
		{
			int rad = gameModeCircles[0][2];
			g2.fillArc(gameModeCircles[0][0] - rad,
					gameModeCircles[0][1] - rad
							+ (int)(.25 * getHeight()),
					2 * rad, 2 * rad, 0, 360);
		}
		else // p2 starts
		{
			int rad = gameModeCircles[1][2];
			g2.fillArc(gameModeCircles[1][0] - rad,
					gameModeCircles[1][1] - rad
							+ (int)(.25 * getHeight()),
					2 * rad, 2 * rad, 0, 360);
		}
	}

	/**
	 * Fills in the radio button selected for game mode (PvP or PvAI)
	 * @param g
	 */
	public void AddFillInCircleGameMode(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(Color.green);
		
		// the relative radius of the fill circle to the button itself
		double radiusFactor = 0.6;
		
		if (gameMode.equals("PVP"))
		{
			// Make the fillingIn color smaller.
			int rad = (int)(gameModeCircles[0][2]*radiusFactor);
			g2.fillArc(gameModeCircles[0][0] - rad,
					gameModeCircles[0][1] - rad, 2 * rad, 2 * rad, 0,
					360);
		}
		else // PVC
		{
			int rad = (int)(gameModeCircles[1][2]*radiusFactor);
			g2.fillArc(gameModeCircles[1][0] - rad,
					gameModeCircles[1][1] - rad, 2 * rad, 2 * rad, 0,
					360);
		}
	}

	public void AddPlayButton(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		int widthOfPlayButton = getWidth() / 10;
		int startOfFirstOptionsY = (int)(getHeight() * .9);

		g2.setColor(Color.black);
		g2.fillRect(getWidth() / 2 - widthOfPlayButton / 2,
				startOfFirstOptionsY, widthOfPlayButton, widthOfPlayButton);
		AddTriangleToPlayButton(getWidth() / 2 - widthOfPlayButton / 2,
				startOfFirstOptionsY, widthOfPlayButton, g);

	}

	public void AddTriangleToPlayButton(int startX, int startY, int widthOfBox,
			Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(Color.green);
		// triangle starts a portion into the square
		double percentage = .2;
		int startTriangleX = startX + (int)Math.round(widthOfBox * percentage);
		int[] xs =
		{ startTriangleX, startTriangleX, startX + widthOfBox };
		int[] ys =
		{ startY, startY + widthOfBox, startY + widthOfBox / 2 };

		g2.fillPolygon(xs, ys, 3);
	}

	public void AddNamesStartOptions(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		int startOfFirstOptionsY = (int)(getHeight() * .6);
		// int numOfCircles=2;
		// int circleDiameter=(int)(.15*height);

		String name1 = nameP1 != null ? nameP1 : "name1";
		String name2 = nameP2 != null ? nameP2 : "name2";
		String[] msgs =
		{ name1, name2 };

		// nameBoxes = new ArrayList<int[]>();

		g2.setColor(Color.BLACK);
		int fontSize = (int)Math.round(getWidth() * .075);
		Font msgFont = new Font("Helvetica", Font.BOLD, fontSize);
		FontMetrics msgFontMetrics = getFontMetrics(msgFont);
		g2.setFont(msgFont);

		int portionFromMiddle = (int)(getWidth() * .1);
		g2.drawString(msgs[0],
				getWidth() / 2 - portionFromMiddle
						- msgFontMetrics.stringWidth(msgs[0]),
				startOfFirstOptionsY);
		// surrounding rect

		drawBoxSurroundingWord(
				getWidth() / 2 - portionFromMiddle
						- msgFontMetrics.stringWidth(msgs[0]),
				startOfFirstOptionsY, msgFontMetrics.stringWidth(msgs[0]),
				msgFontMetrics.getHeight(), g, Color.black);

		g2.drawString(msgs[1], getWidth() / 2 + portionFromMiddle,
				startOfFirstOptionsY);
		// surrounding rect

		drawBoxSurroundingWord(getWidth() / 2 + portionFromMiddle,
				startOfFirstOptionsY, msgFontMetrics.stringWidth(msgs[1]),
				msgFontMetrics.getHeight(), g, Color.black);

	}

	public void AddGameModeOptions(Graphics g)
	{
		// y-position of options circles
		int startOfFirstOptionsY = (int)(getHeight() * .25);
		int numOfCircles = 2;
		// circle diameter dependent on height
		int circleDiameter = (int)(.15 * getHeight());
		String[] msgs =
		{ "PVP", "PVC" };
		AddOptionCircles(g, startOfFirstOptionsY, numOfCircles, circleDiameter,
				msgs, Color.blue, false);

		// Update references
		gameModeCircles = new int[numOfCircles][3];// 2 circles,
																// [x,y,radius]

		gameModeCircles[0][0] = getWidth() / 3;// PVP
		gameModeCircles[0][1] =
				startOfFirstOptionsY + circleDiameter / 2;// PVP circle 1 Y
															// vertex
		gameModeCircles[0][2] = circleDiameter / 2;// PVP circle 1
																// radius

		gameModeCircles[1][0] = 2 * getWidth() / 3; // PVC
		
		gameModeCircles[1][1] =
				startOfFirstOptionsY + circleDiameter / 2;// PVC circle 2 Y
															// vertex
		gameModeCircles[1][2] = circleDiameter / 2;// PVP circle 2
																// radius

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		repaint();
	}

	public boolean inCircle(int startX, int startY, int vertexX, int vertexY,
			int radius)
	{
		double xDistance = vertexX - startX;
		double yDistance = vertexY - startY;

		double distance =
				Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
		return distance < radius || Math.abs(distance - radius) <= EPSILON;

	}

	/**
	 * Kept for when clicking is used
	 */

	public class MousePressListener implements MouseListener
	{
		public void mousePressed(MouseEvent event)
		{
			xClick = event.getX();
			yClick = event.getY();

			System.out.println("CLICK: " + xClick + " " + yClick);

			if (gameMode == null)
			{

				if (inCircle(xClick, yClick, gameModeCircles[0][0],
						gameModeCircles[0][1],
						gameModeCircles[0][2]))// In Circle 1
				{
					gameMode = "PVP";
				}

				else if (inCircle(xClick, yClick,
						gameModeCircles[1][0],
						gameModeCircles[1][1],
						gameModeCircles[1][2]))// In Circle 1
				{
					gameMode = "PVC";
				}
			}
			else if (firstPlayer == null)
			{

				if (inCircle(xClick, yClick, gameModeCircles[0][0],
						gameModeCircles[0][1]
								+ (int)(.25 * getHeight()),
						gameModeCircles[0][2]))// In Circle 1
				{
					// firstPlayer="p1";
				}

				else if (inCircle(xClick, yClick,
						gameModeCircles[1][0],
						gameModeCircles[1][1]
								+ (int)(.25 * getHeight()),
						gameModeCircles[1][2]))// In Circle 1
				{
					// firstPlayer="p2";
				}

				firstPlayer = "p1";

			}
			else if (gameMode.equals("PVC") && computerDifficulty == null)
			{

				computerDifficulty = "EASY";
			}

			repaint();
		}

		public void mouseReleased(MouseEvent event)
		{
		}

		public void mouseClicked(MouseEvent event)
		{
		}

		public void mouseEntered(MouseEvent event)
		{
		}

		public void mouseExited(MouseEvent event)
		{
		}

	}
}
