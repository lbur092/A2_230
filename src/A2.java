import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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


	private static final long serialVersionUID = 1L;

	private Object[] srcHosts;
	private Object[] destHosts;
	JRadioButton source_hosts_button;
	JRadioButton destination_hosts_button;
	JComboBox<String> ipComboBox;

	public A2() {


		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem fileMenuOpen = new JMenuItem("Open");
		fileMenu.add(fileMenuOpen);

		JMenuItem fileMenuQuit = new JMenuItem("Quit");
		fileMenu.add(fileMenuQuit);


		JPanel hostsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		hostsPanel.setSize(200, 100);
		hostsPanel.setBackground(Color.ORANGE);


		source_hosts_button = new JRadioButton();
		source_hosts_button.setText("Source hosts");

		destination_hosts_button = new JRadioButton();
		destination_hosts_button.setText("Destination hosts");

		ButtonGroup hosts_group = new ButtonGroup();
		hosts_group.add(source_hosts_button);
		hosts_group.add(destination_hosts_button);

		hostsPanel.add(source_hosts_button);
		hostsPanel.add(destination_hosts_button);

		source_hosts_button.setSelected(true);


		//	String[] dummyvalues = {"Hello"};
		ipComboBox = new JComboBox<String>();
		hostsPanel.add(ipComboBox);
		ipComboBox.setVisible(false);

		getContentPane().add(hostsPanel, BorderLayout.NORTH);
		hostsPanel.setSize(200, 100);;



		GraphPanel graphPanel = new GraphPanel();
		getContentPane().add(graphPanel, BorderLayout.CENTER);
		graphPanel.setBackground(Color.yellow);
		graphPanel.setSize(1000, 325);

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
				File f = null;

				if(returnValue == JFileChooser.APPROVE_OPTION) {
					f = chooser.getSelectedFile();
					ArrayList<Packet> packets = createValidPacketList(f);

					srcHosts = getUniqueSortedSourceHosts(packets);
					destHosts = getUniqueSortedDestHosts(packets);

					ipComboBox.setVisible(true);
					populateComboBox();
				}
			}







		});	

		source_hosts_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateComboBox();

			}

		});
		destination_hosts_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateComboBox();

			}

		});

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

	public static ArrayList<Packet> createValidPacketList(File f) {

		ArrayList<Packet> packetlist = new ArrayList<Packet>();
		Scanner input = null;
		String line = "";
		Pattern ippattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

		try {
			input = new Scanner(f);
		}
		catch(IOException e1) {
			// thrown if file doesn't exist or isn't readable
			System.out.printf("%s (The system cannot find the file specified)", f.toString());
			return packetlist;
		}


		while(input.hasNext() ) {
			line = input.nextLine();
			//	System.out.println(line);
			Packet linepacket = new Packet(line);
			Matcher m = ippattern.matcher(line);
			String ipstring = "";
			if(m.find()) {
				ipstring = m.group(0);
				packetlist.add(linepacket);
			}
			//	System.out.println(ipstring);

		}
		//			System.out.println("Here");
		//System.out.println(packetlist.size());
		input.close();
		return packetlist;
	}




	public void updateAddressList(boolean hosttype) {
		// 0 - source, 1 - destination
		// get saved list 
		// set contents of combo box

	}

	public static Object[] getUniqueSortedSourceHosts(ArrayList<Packet> packets) {
		HashSet<String> set = new HashSet<String>();
		Pattern ippattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

		for(Packet packet : packets){
			Matcher m = ippattern.matcher(packet.getSourceHost());
			String sourceHost = "";
			if(m.find()) {
				sourceHost = m.group(0);
				set.add(sourceHost);
			}
		}
		ArrayList<Host> list = new ArrayList<Host>();

		for(String sourceHost : set) {
			Host host = new Host(sourceHost);
			list.add(host);
		}

		Collections.sort(list);    
		return list.toArray();
	}

	public static Object[] getUniqueSortedDestHosts(ArrayList<Packet> packets) {
		HashSet<String> set = new HashSet<String>();
		Pattern ippattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

		for(Packet packet : packets){
			Matcher m = ippattern.matcher(packet.getDestinationHost());
			String destHost = "";
			if(m.find()) {
				destHost = m.group(0);
				set.add(destHost);
			}
		}
		ArrayList<Host> list = new ArrayList<Host>();

		for(String destHost : set) {
			Host host = new Host(destHost);
			list.add(host);
		}

		Collections.sort(list);    
		return list.toArray();
	}

	private void populateComboBox() {
		if(source_hosts_button.isSelected()) {
						if(srcHosts != null) {
			ipComboBox.setModel(new DefaultComboBoxModel(srcHosts));

						}

		} else {
			if(destHosts != null) {

				ipComboBox.setModel(new DefaultComboBoxModel(destHosts));
			}

		}


	} 


}
