class Host implements Comparable<Host> {
	private String ip;
	long wvalue = 0;

	public Host(String ip) {
		this.ip = ip;
		String[] thisoctetlist = ip.split("\\.");

		for (int i = 0; i < 4; i++) {

			int power = 3 - i;
			wvalue += Integer.parseInt(thisoctetlist[i]) * Math.pow(256, power); 

		}

	}

	/**
	 * Getter method to return the string version of the IP address for use in other parts of the application.
	 * @return Returns the string version of the IP address assigned to the host.
	 */
	public String toString() {
		return ip;
	}

	/**
	 * Method to enable us to sort IP addresses by numeric value from first to last octet, based on wvalue, the weighted value assigned
	 * in the constructor. We will use Collections.sort which will invoke this method.
	 * @param other Host object. We are sorting this Host's IP address against the other host's IP address.
	 * @return Returns integer, positive if this IP address is greater than the other, zero if the same, or negative if less than
	 * the other IP address
	 */
	public int compareTo(Host other) {
		return (int) (wvalue - other.wvalue);

	}
}