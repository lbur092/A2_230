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
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		//		draw axes.

		g.drawLine(basicX, basicY, basicX, basicY + yChange); // y axis
		g.drawLine(basicX - 5,  basicY,  basicX + xChange,  basicY); // x axis

		// draw labels
		g.drawString("Volume [bytes]", 10, 20);
		g.drawString("Time [s]", 490, basicY + 40);

		Scanner s = new Scanner(System.in);
		//		draw ticks - 4 to 10 y axis, x axis  8 and 24 ticks



		int xtickinterval;
		//= (temp / 50) * 50; // will be 50.

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
			ytickinterval = poweroften;
		} else if (temp < 5) {
			ytickinterval = poweroften * 2; 
		} else {
			ytickinterval = poweroften * 5;
		}
////		
////
////	} else if (maxY < 24 * 5) {
////		ytickinterval = 5;
////	} else if (maxY < 24 * 10) {
////		ytickinterval = 10;
////	} else if (maxY < 24 * 20) {
////		ytickinterval = 20;
////	} else if (maxY < 24 * 50) {
////		ytickinterval = 50;
////	} else {
////		ytickinterval = 100;
////	}	
////
////	if (maxY < 24 * 1) {
////		ytickinterval = 1;
////	} else if (maxY < 24 * 2) {
////		ytickinterval = 2;
////
////	} else if (maxY < 24 * 5) {
////		ytickinterval = 5;
////	} else if (maxY < 24 * 10) {
////		ytickinterval = 10;
////	} else if (maxY < 24 * 20) {
////		ytickinterval = 20;
////	} else if (maxY < 24 * 50) {
////		ytickinterval = 50;
////	} else {
////		ytickinterval = 100;
////	}	
//
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
		if(ytickinterval > 1000) {
			g.drawString(i * ytickinterval / 1000 + "k", x - 40, y + 5) ; // 40 to the left, 5 below.

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
			// for i in range (0 to bytearray.length)
			int leftside = getGraphicX(timeArray[i] - interval ,  maxX);
			int height = (int)(byteArray[i] * -1 * yChange / maxY);
			int top = basicY - height; // - height in future


			//drawRect(Left, top, 
			g.drawRect(leftside, top, width, height);


//			System.out.printf("leftside %d\n", leftside);
//			System.out.printf("top %d\n", leftside);
//			System.out.printf("height %d\n", height);
//			System.out.printf("width %d\n", width);

			// width will be 2? 		

		}
	}


}

public void plotGraph(Host host, ArrayList<Packet> packetlist) {

}

public void update(ArrayList<Packet> packets, String ipFilter, boolean isSrcHost, double endTime) {
	maxX = endTime + interval;
	int slots = (int)Math.ceil(endTime / interval);
	timeArray = new double[slots];
	byteArray = new double[slots];
	if(packets != null) {
		for(Packet packet: packets) {
			// System.out.println(ipFilter);

			if(isSrcHost && (packet.getSourceHost().equals(ipFilter)) || !isSrcHost && (packet.getDestinationHost().equals(ipFilter))) {
				// System.out.println("if clause");
				int slot= (int)Math.floor(packet.getTimeStamp() / interval);

				byteArray[slot] += packet.getIpPacketSize();
			}

		}
	}

	for(int i = 0; i < slots; i++) {
		timeArray[i] = interval * (i+1);
	}

//	System.out.println(Arrays.toString(timeArray));

//	System.out.println(Arrays.toString(byteArray));

	// starting here
maxY = 0;
	for(double bytes : byteArray) {
		if (bytes > maxY) {
			maxY = bytes;
		}
	}

	repaint();
}


public int getGraphicX(double actualX, double maxX) {
	int endX = basicX + xChange;
	double graphicX = (actualX / maxX) * xChange + basicX;

	return (int)graphicX;

}
public int getGraphicY(double actualY, double maxY) {
	int endY = basicY + yChange;

	double graphicY = (actualY / maxY) * yChange + basicY;
	return (int)graphicY;
}


}

