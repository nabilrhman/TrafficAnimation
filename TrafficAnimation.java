import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * CS 121 Project 1: Traffic Animation
 *
 * An animated graphical scene consisting of trucks, sun, sky, roads, grass and an avatar. 
 * This project provides demonstration of various uses of the Java Graphics API.
 *
 * @author BSU CS 121 Instructors
 * @author Nabil Rahman
 */
@SuppressWarnings("serial")
public class TrafficAnimation extends JPanel
{
	// This is where you declare constants and variables that need to keep their
	// values between calls	to paintComponent(). Any other variables should be
	// declared locally, in the method where they are used.

	/**
	 * A constant to regulate the frequency of Timer events.
	 * Note: 100ms is 10 frames per second - you should not need
	 * a faster refresh rate than this
	 */
	private final int DELAY = 50; //milliseconds

	/**
	 * The anchor coordinate for drawing / animating. All of your vehicle's
	 * coordinates should be relative to this offset value.
	 */
	private int xOffset = 0;
	private int xOffsetColor = 0;
	
	/**
	 * The number of pixels added to xOffset each time paintComponent() is called.
	 */
	private int stepSize = 8;
	
	private final Color BACKGROUND_COLOR = Color.WHITE;
	
	/**
	 * The global variables used to implement several internal functionalities.
	 */
	private int sunMoonFlag = 0;
	private Color truckBodyColor = Color.RED;
	private Color truck2BodyColor = new Color(153, 0, 255);

	/* This method draws on the panel's Graphics context.
	 *
	 * (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;	
		
		// Gets the current width and height of the window.
		int width = getWidth(); // panel width
		int height = getHeight(); // panel height
		
		// Calculates the new xOffset position of the moving object.
		xOffset = (xOffset + stepSize) % (2 * width);
		
		// Fills the graphics page with the background color.
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, width, height);
		
		//==============================================================
		// Background
		//==============================================================
		
		// This draws the sky
		
		xOffsetColor = (xOffsetColor + stepSize); // The offset used for changing the sky color.
		
		Color lightBlue = new Color(153, 204, 255);
		float lightBlueHSB[] = Color.RGBtoHSB(153, 204, 255, null);
		float lightBlueBOriginal = lightBlueHSB[2];
		
		if((lightBlueHSB[2]-(float)(Math.sin(xOffsetColor/2200.0)))<=lightBlueBOriginal)
			lightBlue = Color.getHSBColor(lightBlueHSB[0], lightBlueHSB[1], lightBlueBOriginal-(float)(Math.sin(xOffsetColor/2200.0)));
		
		GradientPaint lightBlueToBlue = new GradientPaint(0, 0, lightBlue, 200, height, new Color(0, 0, 255));
		g2d.setPaint(lightBlueToBlue);
		g2d.fill(new Rectangle(0, 0, width, height-height/2));
		
		// This draws the sun
		
		int sunX = width/16;
		int sunY = height-(height/2);
		int sunDiameter = height/5;
		double sunRotationAngle = 0.0;
		
		if(sunRotationAngle == 3.995 || sunRotationAngle == 3.994)
			++sunMoonFlag;
		
		sunRotationAngle = Math.floor(((sunRotationAngle + (xOffsetColor/2000.0)) % Math.floor(Math.toRadians(230))) * 1000) / 1000;
				
		if(sunMoonFlag%2==0)
			g2d.setColor(new Color(255, 255, 153));
		else
			g2d.setColor(Color.ORANGE);
		
		// This draws the sun and rotates it
		
		AffineTransform transformationBeforeRotationSun = g2d.getTransform();
		g2d.rotate(sunRotationAngle, width/2, height/2);
		g2d.fillOval(sunX, sunY, sunDiameter, sunDiameter);
		g2d.setTransform(transformationBeforeRotationSun);
		
		if(sunRotationAngle == 0) // Changes the flag when the sun completes moving one round
			sunMoonFlag += 1; 
				
		// This draws the road
		
		int roadWidth = width;
		int roadHeight = height/4;
		int roadX = 0;
		int roadY = height/2;
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(roadX, roadY, roadWidth, roadHeight);
		
		// This draws the road lines
		
		int roadLineHeight = roadHeight/30;
		int roadLineY = roadY+roadHeight/2;
		
		g.setColor(Color.WHITE);
		g.fillRect(roadX, roadLineY, roadWidth, roadLineHeight);	
		
		// This draws the grass field using textures from online
		
		BufferedImage grassTexture = null;
		
		try 
		{
			grassTexture = ImageIO.read(new File("grass_texture.jpg"));
		} 
		catch (IOException e) 
		{
			
		}
		
		TexturePaint grassTexturePaint = new TexturePaint(grassTexture, new Rectangle(0, 0, grassTexture.getWidth(), grassTexture.getHeight()));
		g2d.setPaint(grassTexturePaint);
		g2d.fill(new Rectangle(0, roadY+roadHeight, width, height-roadY));
		
		// This prints the title text
		
		String title = "ROCK 'N' ROLL";
		g.setFont(new Font("Verdana", Font.BOLD, (height/15)));
				
		FontMetrics metrics = g.getFontMetrics();
		int x = (width - metrics.stringWidth(title)) / 2;
		int y = (height + metrics.getHeight()) / 4;
		
		g.setColor(Color.RED);
		g.drawString(title, x, y);

		//==============================================================
		// Truck 1
		//==============================================================
		
		// This draws the body of the truck.
		
		int truckFrontHeight = height/6; //100
		int truckFrontWidth = truckFrontHeight+truckFrontHeight/2; //150
		int truckFrontX = xOffset-width/2;
		int truckFrontY = roadY-roadHeight/2; //50
		
		int trailerWidth = truckFrontWidth;
		int trailerHeight = truckFrontHeight/2;
		int trailerX = truckFrontX-truckFrontWidth-(truckFrontWidth/5);
		int trailerY = truckFrontY+truckFrontHeight-trailerHeight;
		
		int truckHoodWidth = truckFrontWidth/2;
		int truckHoodHeight = truckFrontHeight/2;
		int truckHoodX = truckFrontX+truckFrontWidth;
		int truckHoodY = truckFrontY+truckFrontHeight-trailerHeight;
		
		int chainWidth = truckFrontWidth/5;
		int chainHeight = trailerHeight/6;
		int chainX = trailerX+trailerWidth;
		int chainY = trailerY+trailerHeight/2;
		
		// This randomizes the colors of both trucks
		
		Random randomGenerator = new Random();
				
		if(xOffset == 0)
		{
			truckBodyColor = new Color(randomGenerator.nextInt((255-100)+1)+100, randomGenerator.nextInt(101), 0);
			truck2BodyColor = new Color(randomGenerator.nextInt((255-153)+1)+153, randomGenerator.nextInt(101), randomGenerator.nextInt((255-100)+100)+1);
		}
				
		g.setColor(truckBodyColor);
		
		// This draws the body, the trailer and the chain
		
		g.fillRect(truckFrontX, truckFrontY, truckFrontWidth, truckFrontHeight);
		g.fillRect(truckHoodX, truckHoodY, truckHoodWidth, truckHoodHeight);
		
		g.setColor(new Color(255, 153,0));
		g.fillRect(trailerX, trailerY, trailerWidth, trailerHeight);
		
		g.setColor(Color.YELLOW);
		g.fillRect(chainX, chainY, chainWidth, chainHeight);
		
		// This draws the wheels of truck 1
		
		int wheelDiameter = truckFrontWidth/3;
		int frontWheel1X = truckHoodX;
		int frontWheel1Y = truckHoodY+truckHoodHeight-truckFrontHeight/5;
		
		int frontWheel2X = truckFrontX+truckFrontWidth/10;
		int frontWheel2Y = truckHoodY+truckHoodHeight-truckFrontHeight/5;
		
		int trailerWheelX = trailerX+truckFrontWidth/10;
		int trailerWheelY = truckHoodY+truckHoodHeight-truckFrontHeight/5;
		
		g.setColor(Color.GRAY);
		
		g.fillOval(frontWheel1X,frontWheel1Y, wheelDiameter, wheelDiameter);
		g.fillOval(frontWheel1X+wheelDiameter/2-(wheelDiameter/4), frontWheel1Y+wheelDiameter/2-(wheelDiameter/4), wheelDiameter/2, wheelDiameter/2);
		
		g.fillOval(frontWheel2X,frontWheel2Y, wheelDiameter, wheelDiameter);
		
		g.fillOval(trailerWheelX, trailerWheelY, wheelDiameter, wheelDiameter);
		
		// This draws the rims of truck 1
		
		g.setColor(Color.BLUE);
		
		g.fillOval(trailerWheelX+wheelDiameter/2-(wheelDiameter/4), trailerWheelY+wheelDiameter/2-(wheelDiameter/4), wheelDiameter/2, wheelDiameter/2);
		g.fillOval(frontWheel2X+wheelDiameter/2-(wheelDiameter/4), frontWheel2Y+wheelDiameter/2-(wheelDiameter/4), wheelDiameter/2, wheelDiameter/2);
		g.fillOval(frontWheel1X+wheelDiameter/2-(wheelDiameter/4), frontWheel1Y+wheelDiameter/2-(wheelDiameter/4), wheelDiameter/2, wheelDiameter/2);
		
		// This draws rectangles covering rims of truck 1
		
		int rimRectangleWidth = wheelDiameter;
		int rimRectangleHeight = wheelDiameter/5;
		
		int trailerRimRectangleX = trailerWheelX;
		int trailerRimRectangleY = trailerWheelY+wheelDiameter/2-(rimRectangleHeight/2);
		
		int frontWheel2RimRectangleX = frontWheel2X;
		int frontWheel2RimRectangleY = frontWheel2Y+wheelDiameter/2-(rimRectangleHeight/2);
		
		int frontWheel1RimRectangleX = frontWheel1X;
		int frontWheel1RimRectangleY = frontWheel1Y+wheelDiameter/2-(rimRectangleHeight/2);
		
		// This draws the rim rectangles of truck 1 and rotates them
		
		g.setColor(Color.BLUE);
		
		AffineTransform transformationBeforeRotationTruck1 = g2d.getTransform();
		
		g2d.rotate(Math.toRadians(xOffset), trailerRimRectangleX+(rimRectangleWidth/2), trailerRimRectangleY+(rimRectangleHeight/2));
		g2d.fillRect(trailerRimRectangleX, trailerRimRectangleY, rimRectangleWidth, rimRectangleHeight);
		g2d.setTransform(transformationBeforeRotationTruck1);
		
		g2d.rotate(Math.toRadians(xOffset), frontWheel2RimRectangleX+(rimRectangleWidth/2), frontWheel2RimRectangleY+(rimRectangleHeight/2));
		g2d.fillRect(frontWheel2RimRectangleX, frontWheel2RimRectangleY, rimRectangleWidth, rimRectangleHeight);
		g2d.setTransform(transformationBeforeRotationTruck1);
		
		g2d.rotate(Math.toRadians(xOffset), frontWheel1RimRectangleX+(rimRectangleWidth/2), frontWheel1RimRectangleY+(rimRectangleHeight/2));
		g2d.fillRect(frontWheel1RimRectangleX, frontWheel1RimRectangleY, rimRectangleWidth, rimRectangleHeight);
		g2d.setTransform(transformationBeforeRotationTruck1);
		
		// This draws rims' center circle of truck 2
		
		g.setColor(Color.BLACK);
		g.fillOval(frontWheel2X+wheelDiameter/2-(wheelDiameter/6), frontWheel2Y+wheelDiameter/2-(wheelDiameter/6), wheelDiameter/3, wheelDiameter/3);
		g.fillOval(frontWheel1X+wheelDiameter/2-(wheelDiameter/6), frontWheel1Y+wheelDiameter/2-(wheelDiameter/6), wheelDiameter/3, wheelDiameter/3);
		g.fillOval(trailerWheelX+wheelDiameter/2-(wheelDiameter/6), trailerWheelY+wheelDiameter/2-(wheelDiameter/6), wheelDiameter/3, wheelDiameter/3);
		
		//==============================================================
		// Truck 2
		//==============================================================

		// This flips the previously drawn truck
		
		AffineTransform originalTransformation = g2d.getTransform();
		AffineTransform flipTransformer = new AffineTransform();
		flipTransformer.scale( -1.0, 1.0 );
		g2d.setTransform (flipTransformer);
		
		// This draws the body of truck 2.
		
		int truck2FrontHeight = height/6; //100
		int truck2FrontWidth = truck2FrontHeight+truck2FrontHeight/2; //150
		int truck2FrontX = xOffset-width*2;
		int truck2FrontY = roadY; //50
		
		int truck2HoodWidth = truck2FrontWidth/2;
		int truck2HoodHeight = truck2FrontHeight/2;
		int truck2HoodX = truck2FrontX+truck2FrontWidth;
		int truck2HoodY = truck2FrontY+truck2FrontHeight-truck2FrontHeight/2;
			
		g2d.setColor(truck2BodyColor);
		g2d.fillRect(truck2FrontX, truck2FrontY, truck2FrontWidth, truck2FrontHeight);
		g2d.fillRect(truck2HoodX, truck2HoodY, truck2HoodWidth, truck2HoodHeight);
		
		// This draws the wheels of truck 2
				
		int truck2FrontWheel1X = truck2HoodX;
		int truck2FrontWheel1Y = truck2HoodY+truck2HoodHeight-truck2FrontHeight/5;
		
		int truck2FrontWheel2X = truck2FrontX+truck2FrontWidth/10;
		int truck2FrontWheel2Y = truck2HoodY+truck2HoodHeight-truck2FrontHeight/5;
		
		g2d.setColor(Color.GRAY);	
		g2d.fillOval(truck2FrontWheel1X,truck2FrontWheel1Y, wheelDiameter, wheelDiameter);
		g2d.fillOval(truck2FrontWheel2X,truck2FrontWheel2Y, wheelDiameter, wheelDiameter);
		g2d.fillOval(truck2FrontWheel1X+wheelDiameter/2-(wheelDiameter/4), truck2FrontWheel1Y+wheelDiameter/2-(wheelDiameter/4), wheelDiameter/2, wheelDiameter/2);
			
		// This draws the rims of truck 2
		
		g2d.setColor(Color.MAGENTA);
		g2d.fillOval(truck2FrontWheel2X+wheelDiameter/2-(wheelDiameter/4), truck2FrontWheel2Y+wheelDiameter/2-(wheelDiameter/4), wheelDiameter/2, wheelDiameter/2);
		g2d.fillOval(truck2FrontWheel1X+wheelDiameter/2-(wheelDiameter/4), truck2FrontWheel1Y+wheelDiameter/2-(wheelDiameter/4), wheelDiameter/2, wheelDiameter/2);
				
		// This draws rectangles covering rims of truck 2
		
		int truck2RimRectangleWidth = wheelDiameter;
		int truck2RimRectangleHeight = wheelDiameter/5;
		
		int truck2FrontWheel2RimRectangleX = truck2FrontWheel2X;
		int truck2FrontWheel2RimRectangleY = truck2FrontWheel2Y+wheelDiameter/2-(rimRectangleHeight/2);
		
		int truck2FrontWheel1RimRectangleX = truck2FrontWheel1X;
		int truck2FrontWheel1RimRectangleY = truck2FrontWheel1Y+wheelDiameter/2-(rimRectangleHeight/2);
							
		// This draws the rim rectangles of truck 2 and rotates them
		
		g2d.setColor(Color.MAGENTA);
		
		AffineTransform transformationBeforeRotationTruck2 = g2d.getTransform();
		
		g2d.rotate(Math.toRadians(xOffset), truck2FrontWheel2RimRectangleX+(truck2RimRectangleWidth/2), truck2FrontWheel2RimRectangleY+(truck2RimRectangleHeight/2));
		g2d.fillRect(truck2FrontWheel2RimRectangleX, truck2FrontWheel2RimRectangleY, truck2RimRectangleWidth, truck2RimRectangleHeight);
		g2d.setTransform(transformationBeforeRotationTruck2);
		
		g2d.rotate(Math.toRadians(xOffset), truck2FrontWheel1RimRectangleX+(truck2RimRectangleWidth/2), truck2FrontWheel1RimRectangleY+(truck2RimRectangleHeight/2));
		g2d.fillRect(truck2FrontWheel1RimRectangleX, truck2FrontWheel1RimRectangleY, truck2RimRectangleWidth, truck2RimRectangleHeight);
		g2d.setTransform(transformationBeforeRotationTruck2);
				
		// This Draws rims' center circle of truck 2
		
		g2d.setColor(Color.BLACK);
		g2d.fillOval(truck2FrontWheel2X+wheelDiameter/2-(wheelDiameter/6), truck2FrontWheel2Y+wheelDiameter/2-(wheelDiameter/6), wheelDiameter/3, wheelDiameter/3);
		g2d.fillOval(truck2FrontWheel1X+wheelDiameter/2-(wheelDiameter/6), truck2FrontWheel1Y+wheelDiameter/2-(wheelDiameter/6), wheelDiameter/3, wheelDiameter/3);
	
		// This resets to previous transformation
		
		g2d.setTransform(originalTransformation);
		
		//==============================================================
		// Avatar
		//==============================================================
		
		// This draws an avatar by using image from online
		
		BufferedImage avatar = null;
		
		try
		{
			avatar = ImageIO.read(new File("avatar.png"));
							
		}
		catch (IOException e)
		{
							
		}
		
		ImageIcon avatarIcon = new ImageIcon(avatar);

		int avatarHeight = height/2;
		int avatarWidth = avatarHeight/3;
		int avatarX = width/2-avatarWidth/2;
		int avatarY = height-avatarHeight;

		g.drawImage(avatarIcon.getImage(), avatarX, avatarY, avatarWidth, avatarHeight, null);
						
		// This makes the drawing smoother.
		Toolkit.getDefaultToolkit().sync();
	}


	//==============================================================
	// You don't need to modify anything beyond this point.
	//==============================================================


	/**
	 * Starting point for this program. Your code will not go in the
	 * main method for this program. It will go in the paintComponent
	 * method above.
	 *
	 * DO NOT MODIFY this method!
	 *
	 * @param args unused
	 */
	public static void main (String[] args)
	{
		// DO NOT MODIFY THIS CODE.
		JFrame frame = new JFrame ("Traffic Animation");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new TrafficAnimation());
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Constructor for the display panel initializes necessary variables.
	 * Only called once, when the program first begins. This method also
	 * sets up a Timer that will call paint() with frequency specified by
	 * the DELAY constant.
	 */
	public TrafficAnimation()
	{
		// Do not initialize larger than 800x600. I won't be able to
		// grade your project if you do.
		int initWidth = 800;
		int initHeight = 600;
		setPreferredSize(new Dimension(initWidth, initHeight));
		this.setDoubleBuffered(true);

		//Start the animation - DO NOT REMOVE
		startAnimation();
	}

	/**
	 * Create an animation thread that runs periodically.
	 * DO NOT MODIFY this method!
	 */
	private void startAnimation()
	{
		ActionListener timerListener = new TimerListener();
		Timer timer = new Timer(DELAY, timerListener);
		timer.start();
	}

	/**
	 * Repaints the graphics panel every time the timer fires.
	 * DO NOT MODIFY this class!
	 */
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	}
}
