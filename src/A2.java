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

	ArrayList<Packet> packets;

	public A2() {


		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
		fileMenu.add(fileMenuOpen);

		JMenuItem fileMenuQuit = new JMenuItem("Quit");
		fileMenu.add(fileMenuQuit);


		JPanel hostsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		hostsPanel.setSize(200, 100);
		//hostsPanel.setBackground(Color.ORANGE); // for testing only


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


		ipComboBox = new JComboBox<String>();
		hostsPanel.add(ipComboBox);
		ipComboBox.setVisible(false);

		getContentPane().add(hostsPanel, BorderLayout.NORTH);
		hostsPanel.setSize(200, 100);;



		GraphPanel graphPanel = new GraphPanel();
		getContentPane().add(graphPanel, BorderLayout.CENTER);
		graphPanel.setBackground(Color.white); // 
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
					packets = createValidPacketList(f);

					srcHosts = getUniqueSortedSourceHosts(packets);
					destHosts = getUniqueSortedDestHosts(packets);

					ipComboBox.setVisible(true);
					populateComboBox();
					graphPanel.update(packets, ipComboBox.getSelectedItem().toString(), source_hosts_button.isSelected(), calculateEndTime());

				}
			}

		});	

		source_hosts_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateComboBox();
				if(packets != null) {
					graphPanel.update(packets, ipComboBox.getSelectedItem().toString(), source_hosts_button.isSelected(), calculateEndTime());

				}


			}

		});
		destination_hosts_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateComboBox();
				if(packets != null) {
					graphPanel.update(packets, ipComboBox.getSelectedItem().toString(), source_hosts_button.isSelected(), calculateEndTime());

				}

			}

		});

		ipComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(packets != null) {
					graphPanel.update(packets, ipComboBox.getSelectedItem().toString(), source_hosts_button.isSelected(), calculateEndTime());

				}
			}
		});

		//general setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Flow volume viewer");
		setSize(1000, 500);
		setVisible(true);
		getContentPane().revalidate();
		getContentPane().repaint();


	}

	/**
	 * The main function of the program. Starts the event dispatch thread in Swing and creates an instance
	 * of this A2 class, which will then draw the GUI and set up event handlers for the components.
	 *
	 * @param  args String array The standard command line arguments. Not used in this program.
	 */
	public static void main(String[] args) {


		SwingUtilities.invokeLater(new Runnable() { //anonymous class implementing Runnable
			public void run() {
				new A2();

			}
		});
	}

	/**
	 * This function takes the file argument (representing the file the user wishes to open) and opens it as a text file.
	 * it searches for an IP address in the line, and if one exists, it creates a packet and adds it to the array list 
	 * which it returns. 
	 * If the user specifies a file which cannot be found, the program will print an error message to the console.
	 * @param  file File object to open, given by the file open dialogue box.
	 * @return      Returns a list of packet objects, one for each list of the file with an IP address.
	 */
	public static ArrayList<Packet> createValidPacketList(File file) {

		ArrayList<Packet> packetlist = new ArrayList<Packet>();
		Scanner input = null;
		String line = "";
		Pattern ippattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

		try {
			input = new Scanner(file);
		}
		catch(IOException e1) {
			// thrown if file doesn't exist or isn't readable
			System.out.printf("%s (The system cannot find the file specified)", file.toString());
			return packetlist;
		}


		while(input.hasNext() ) {
			line = input.nextLine();
			Packet linepacket = new Packet(line);
			Matcher m = ippattern.matcher(line);
			String ipstring = "";
			if(m.find()) {
				ipstring = m.group(0);
				packetlist.add(linepacket);
			}

		}
		input.close();
		return packetlist;
	}

	/**
	 * This method takes the list of packets created in createValidPacketList and checks each one for a source host ip address,
	 * using a regular expression.
	 * It adds the IP address into the HashSet we create to ensure it is unique. Then, it moves each item into an ArrayList, 
	 * for sorting. After it has sorted the ArrayList, it converts this into an array and returns the array.
	 * @param  packets ArrayList of packets that we obtain the hosts from.
	 * @return returns an array of Host objects containing source host IP addresses in sorted order.
	 */
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

	/**
	 * Similar to getUniqueSortedSourceHosts above, but instead gets destination hosts from the packets in the arraylist 
	 * argument. It uses a HashSet to get a unique list, sorts them by means of an ArrayList and the Collections.sort method,
	 * and finally returns the Host objects in an array. 
	 * @param  packets ArrayList of packets that we obtain the host addresses from.
	 * @return returns an array of Host objects containing destination host IP addresses in sorted order.
	 */
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

	/**
	 * Called when the ip addresses combo box needs to be updated. It checks if the source, or destination hosts radio button
	 * is selected, and creates a new ComboBoxModel using the appropriate array of hosts. It will only attempt to update if the 
	 * respective array has been initialised.
	 * 	 */
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

	/**
	 * Obtains the maximum packet time stamp by iterating through the packets in the arraylist, and updating the end time
	 * if a larger value is found.
	 * @return returns the end time, i.e. the maximum value on the x-axis.
	 * 	 */
	public double calculateEndTime() {
		double endTime = 0;
		if(packets != null) {
			for(Packet packet : packets) {
				if (packet.getTimeStamp() > endTime) {
					endTime = packet.getTimeStamp();
				}

			}
		}

		return endTime;
	}

}
