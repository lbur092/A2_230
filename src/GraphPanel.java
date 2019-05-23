import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {

	public GraphPanel() {
		repaint();
	}
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawString("Volume [bytes]", 10, 20);
		g.drawLine(50, 25, 50, 275); // y axis
		g.drawLine(45,  270,  950,  270); // x axis
		g.drawString("Time [s]", 490, 310);
		
		for(int x1 = 50; x1 <= 950; x1 += 75) {
			int i = (int) ((x1 - 50) / 1.5); 
			g.drawLine(x1, 270, x1, 275);
			g.drawString(Integer.toString(i), x1 - 10, 290);
		}
		
		
			g.drawString("0", 15, 270);
		}
		

	}

