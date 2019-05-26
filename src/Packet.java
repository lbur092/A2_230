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
          //      System.out.println(timestamp);

        
        
    }
    
    public String getSourceHost() {
        return srcHost;
    }
    
    public String getDestinationHost() {
        return destHost;
    }
    public double getTimeStamp() {
        return timestamp;
    }
    
    public double getIpPacketSize() {
        return ipPacketSize;
    }
    
    public String toString() {
        return String.format("src=%s, dest=%s, time=%.2f, size=%d", srcHost, destHost, timestamp, ipPacketSize);
    }
}