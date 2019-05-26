import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {

	int basicX = 50;
	int xChange = 900;
	int basicY = 300;
	int yChange = -250;
	double interval = 2;
	static double[] timeArray = {};
	static double[] byteArray = {};
	static double maxX = 600;
	static double maxY = 500;

	private static final long serialVersionUID = 1L;

	public GraphPanel() {
		repaint();
	}
	
	/**
	 * Draws the graph panel, first of all the axes and the labels. It then calculates the number of ticks to draw
	 * and the tick interval. It draws the ticks and tick labels.
	 * Draws a series of red rectangle outlines to represent the graph bars.
	 * Uses methods getGraphicX and getGraphixY and fields such as basicX to translate the JPanel graphics coordinate 
	 * system into the coordinate system we wish to represent.
	 * 
	 * @param  g Graphics object provided by swing which we use to do the drawing.
	 */
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		//		draw axes.

		g.drawLine(basicX, basicY, basicX, basicY + yChange); // y axis
		g.drawLine(basicX - 5,  basicY,  basicX + xChange,  basicY); // x axis

		// draw labels
		g.drawString("Volume [bytes]", 10, 20);
		g.drawString("Time [s]", 490, basicY + 40);

		Scanner s = new Scanner(System.in);
		//		draw ticks - 4 to 10 y axis, x axis  8 to 24 ticks



		int xtickinterval;


		if (maxX < 24 * 1) {
			xtickinterval = 1;
		} else if (maxX < 24 * 2) {
			xtickinterval = 2;

		} else if (maxX < 24 * 5) {
			xtickinterval = 5;
		} else if (maxX < 24 * 10) {
			xtickinterval = 10;
		} else if (maxX < 24 * 20) {
			xtickinterval = 20;
		} else if (maxX < 24 * 50) {
			xtickinterval = 50;
		} else {
			xtickinterval = 100;
		}

		int ytickinterval;

		double temp = maxY;
		int poweroften = 1;
		while(temp > 10) {
			temp = temp / 10;
			poweroften = poweroften * 10;
		}

		poweroften = poweroften / 10;
		if (temp < 2) {
			ytickinterval = poweroften * 2;
		} else if (temp < 5) {
			ytickinterval = poweroften * 5; 
		} else {
			ytickinterval = poweroften * 10;
		}
	
		int numyticks = (int) maxY / ytickinterval + 1;
		int numxticks = (int) maxX / xtickinterval + 1;

		System.out.printf("maxY %f\n", maxY);

		for (int i = 0; i < numyticks; i++) { // y axis ticks
			int startX = basicX;

			int x = (int)getGraphicX(0, maxX);
			int y = (int)getGraphicY(i * ytickinterval, maxY);
			//tick 
			g.drawLine(x, y, x - 5, y);

			// label - 5 below the ticks 
			if(ytickinterval >= 1000) {
				if(i * ytickinterval >= 1000000) {
					g.drawString((double)i * ytickinterval / 1000000 + "M", x - 40, y + 5) ; // 40 to the left, 5 below.

				} else {

					g.drawString(i * ytickinterval / 1000 + "k", x - 40, y + 5) ; // 40 to the left, 5 below.
				}


			} else {
				g.drawString("" + i * ytickinterval, x - 40, y + 5) ; // 40 to the left, 5 below.

			}
		}




		for (int i = 0; i < numxticks; i++) { // x ticks

			int x = (int)getGraphicX(i * xtickinterval, maxX);

			int y = (int)getGraphicY(0, maxY);
			//ticks
			g.drawLine(x, y, x, y + 5) ;

			//labels
			g.drawString("" + i * xtickinterval, x - 10, y + 20) ; // 10 to the left, 20 below

		}



		// draw rectangles
		g.setColor(Color.red);

		if(byteArray != null) {
			int width = (int) (xChange / maxX * 2);
			for(int i = 0; i < byteArray.length; i++) {

				int leftside = getGraphicX(timeArray[i] - interval ,  maxX);
				int height = (int)(byteArray[i] * -1 * yChange / maxY);
				int top = basicY - height; 



				g.drawRect(leftside, top, width, height);


				//print statements for testing
				//			System.out.printf("leftside %d\n", leftside);
				//			System.out.printf("top %d\n", leftside);
				//			System.out.printf("height %d\n", height);
				//			System.out.printf("width %d\n", width);

			

			}
		}


	}

	/**
	 * Method updates the graph by updating the values in the time array (for the x values) and the byte array (for the y values).
	 * It also resets maxX and maxY to give the bounds for the axes. It then calls repaint which triggers the paintComponent
	 * method to call the graphics drawing methods.
	 * @param packets ArrayList of packets containing values such as the IP addresses and packet size.
	 * @param ipFilter String containing the IP address selected in the ip address comboBox
	 * @param isSrcHost boolean - true if the source hosts radio button is selected, false if destination hosts selected
	 * @param endTime double number - the maximum time value in the packet data file
	 */
	public void update(ArrayList<Packet> packets, String ipFilter, boolean isSrcHost, double endTime) {
		maxX = endTime + interval;
		int slots = (int)Math.ceil(endTime / interval);
		timeArray = new double[slots];
		byteArray = new double[slots];
		if(packets != null) {
			for(Packet packet: packets) {
 
				if(isSrcHost && (packet.getSourceHost().equals(ipFilter)) || !isSrcHost && (packet.getDestinationHost().equals(ipFilter))) {
 					int slot= (int)Math.floor(packet.getTimeStamp() / interval);

					byteArray[slot] += packet.getIpPacketSize();
				}

			}
		}

		for(int i = 0; i < slots; i++) {
			timeArray[i] = interval * (i+1);
		}

 
 

		maxY = 0;
		for(double bytes : byteArray) {
			if (bytes > maxY) {
				maxY = bytes;
			}
		}

		repaint();
	}

	/**
	 * Converts the x value seen by the user to an x value of the JPanel graphics system, for use with methods such as 
	 * g.DrawRect
	 * @param actualX double - the time value we wish to represent (graph x coordinate to present) 
	 * @param maxX double - maximum x value - the largest time value represented on the x axis.
	 * @return      integer x value corresponding to JPanel graphics coordinates
	 */
	public int getGraphicX(double actualX, double maxX) {
		int endX = basicX + xChange;
		double graphicX = (actualX / maxX) * xChange + basicX;

		return (int)graphicX;

	}
	
	/**
	 * Converts value on y axis drawn to y value for use with Java Graphics API methods
	 * @param actualY double - the byte value we wish to represent (graph y coordinate we present) 
	 * @param maxY double - maximum y value - the largest byte value represented on the y axis.
	 * @return      Returns y value corresponding to JPanel graphics coordinates
	 */
	public int getGraphicY(double actualY, double maxY) {
		int endY = basicY + yChange;

		double graphicY = (actualY / maxY) * yChange + basicY;
		return (int)graphicY;
	}


}

