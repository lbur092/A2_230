import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.SwingUtilities;

public class A2 extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public A2() {
		
	getContentPane().setLayout(null);
		
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem fileMenuOpen = new JMenuItem("Open");
		JMenuItem fileMenuQuit = new JMenuItem("Quit");
		fileMenu.add(fileMenuOpen);
		fileMenu.add(fileMenuQuit);
		
		fileMenuQuit.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
		});
		
		fileMenuOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				int returnValue = chooser.showOpenDialog(A2.this);
				File trace_file_path = null;
				
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					trace_file_path = chooser.getSelectedFile();
					openFile(trace_file_path);
				} 
				
								
			}
			
		});		
		JPanel hostsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		hostsPanel.setSize(200, 100);
		hostsPanel.setBackground(Color.ORANGE);
		
		
		JRadioButton source_hosts_button = new JRadioButton();
		source_hosts_button.setText("Source hosts");
	
		JRadioButton destination_hosts_button = new JRadioButton();
		destination_hosts_button.setText("Destination hosts");
		
		ButtonGroup hosts_group = new ButtonGroup();
		hosts_group.add(source_hosts_button);
		hosts_group.add(destination_hosts_button);

		hostsPanel.add(source_hosts_button);
		hostsPanel.add(destination_hosts_button);
		
		source_hosts_button.setSelected(true);
		
		JPanel addressesPanel = new JPanel();
		
		String[] dummyvalues = {"Hello"};
		JComboBox<String> ipComboBox = new JComboBox<String>(dummyvalues);
		addressesPanel.add(ipComboBox);
		
		addressesPanel.setLocation(500, 25);
		addressesPanel.setSize(100, 50);
		//ipComboBox.setLocation(500, 50);
	
		getContentPane().add(hostsPanel);
		hostsPanel.setLocation(0, 0);//not working
		hostsPanel.setSize(200, 100);;
		
		
		
		GraphPanel graphPanel = new GraphPanel();
		getContentPane().add(graphPanel);
		graphPanel.setBackground(Color.yellow);
		graphPanel.setLocation(0,  100);
		graphPanel.setSize(1000, 325);
		
//		JButton b = new JButton("Hello world");
//		this.add(b);
//		b.setLocation(600, 20);
//		
		//general setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Network Packet Transmission Visualizer");
		setSize(1000, 500);
		setVisible(true);
		getContentPane().revalidate();
		getContentPane().repaint();
		
		
	}

	public static void main(String[] args) {


		SwingUtilities.invokeLater(new Runnable() { //anonymous class implementing Runnable
			public void run() {
				new A2();
				
			}
		});
	}
	public void openFile(File trace_file_path) {
		Set<IPAddress> sourceIpAddressSet = new HashSet<IPAddress>();
		Set<IPAddress> destIpAddressSet = new HashSet<IPAddress>();
		
		Scanner input = null;
		String currentLine = "";
		try {
			input = new Scanner(trace_file_path);
		}
		catch(IOException e1) {
			// thrown if file doesn't exist or isn't readable
			return;
		}
		while(input.hasNext() ) {
			currentLine = input.nextLine();
			
			// NOW I NEED TO SPLIT ON TAB
			String[] tabPart = currentLine.split("\\t");
			System.out.println(tabPart[0]);
			
			String srcaddress = tabPart[2];
			String destaddress = tabPart[4];
			IPAddress sourceIpAddress = null;
			IPAddress destIpAddress = null;
			
			if(srcaddress != "")  {
				sourceIpAddress = new IPAddress(srcaddress);
			}
			 
			if(destaddress != "") {
				destIpAddress = new IPAddress(destaddress);

			} 					
			ArrayList<IPAddress> addresslist = new ArrayList<IPAddress>();

			addresslist.add(sourceIpAddress);
			destIpAddressSet.add(destIpAddress);
			
			Collections.sort(addresslist);
			
			// need to pop combo box
			//ipComboBox.addItem(destIpAddress);
			//ipComboBox.setVisible(true);

			
		}
		
//		
//		String fileContents = Files.readAllLines(trace_file_path.getAbsolutePath())
//		
		// We have a line with a series of numbers separated by tabs
		// want to split for 192.168.0.20 or for 10.0.0.2
		currentLine.split("192.168.*");
		//compile regex and get next occurrence
		
		Pattern sourcePattern = Pattern.compile("192\\.168\\.0\\.*");
		Pattern destinationPattern = Pattern.compile("10\\.0\\.*\\.*");
		Matcher sm = sourcePattern.matcher(currentLine);
		Matcher dm = sourcePattern.matcher(currentLine);

		input.close();


	}
	
	public void updateAddressList(boolean hosttype) {
		// 0 - source, 1 - destination
		// get saved list 
		// set contents of combo box
		
	}

}
