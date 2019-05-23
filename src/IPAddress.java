
public class IPAddress implements Comparable<IPAddress> {

	String address;
	private int octet3, octet4;
	String[] addressparts;
	int seqno;
	public IPAddress(String address) {
		this.address = address;
		String[] addressparts = address.split(".");
		
		if(addressparts.length == 4) {
			octet3 = Integer.parseInt(addressparts[2]);
			octet4 = Integer.parseInt(addressparts[3]);
		
		}
		
		int seqno = 256 * octet3 + octet4;
		
	}
	@Override
	public int compareTo(IPAddress other) {
		return seqno - other.seqno;
	}

}
