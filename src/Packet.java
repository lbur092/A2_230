class Packet {
	private double timestamp;
	private String srcHost;
	private String destHost;
	private int ipPacketSize;

	public Packet (String data) {
		String[] datasplit = data.split("\\t");

		try{
			srcHost = datasplit[2];
			destHost = datasplit[4];
			timestamp = Double.parseDouble(datasplit[1]);
			ipPacketSize = Integer.parseInt(datasplit[7]);

		} catch (Exception e) {

		}

	}

	/**
	 * Getter method for other classes to obtain the source host IP address string.
	 * @return Returns the source host IP address identified in the constructor.
	 */
	public String getSourceHost() {
		return srcHost;
	}

	/**
	 * Getter method for other classes to obtain the destination host IP address string.
	 * @return Returns the destination host IP address identified in the constructor.
	 */

	public String getDestinationHost() {
		return destHost;
	}
	
	/**
	 * Getter method for other classes to obtain the destination the time stamp of the packet.
	 * @return Returns the time stamp of the packet, in seconds, as a double.
	 */

	public double getTimeStamp() {
		return timestamp;
	}

	/**
	 * Getter method for other classes to obtain the packet size, in bytes.
	 * @return Returns double number, the size of the packet represented.
	 */
	public double getIpPacketSize() {
		return ipPacketSize;
	}

	/**
	 * Returns a string containing the source, destination, time and size of the packet, in string, floating point and double
	 * format.
	 * @return Returns string with the source, destination, time and size of the packet.
	 */
	public String toString() {
		return String.format("src=%s, dest=%s, time=%.2f, size=%d", srcHost, destHost, timestamp, ipPacketSize);
	}
}